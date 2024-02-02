package com.example.intuitmoviebooking.services;

import com.example.intuitmoviebooking.Utils.HelperUtil;
import com.example.intuitmoviebooking.model.*;
import com.example.intuitmoviebooking.model.Requests.BookSeatsRequest;
import com.example.intuitmoviebooking.model.Responses.BookSeatResponse;
import com.example.intuitmoviebooking.model.Responses.TheatreResponse;
import com.example.intuitmoviebooking.repository.TheatreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    private final UserService userService;
    private final TheatreService theatreService;
    private final HelperUtil helperUtil;
    private final TheatreRepository theatreRepository;

    BookingService(UserService userService,
                   TheatreService theatreService,
                   HelperUtil helperUtil,
                   TheatreRepository theatreRepository){
        this.userService = userService;
        this.theatreService = theatreService;
        this.helperUtil = helperUtil;
        this.theatreRepository = theatreRepository;
    }


    public ResponseEntity<BookSeatResponse> bookSeats(BookSeatsRequest bookSeatsRequest){
        BookSeatResponse bookSeatResponse = new BookSeatResponse();

        User user = userService.getUserById(bookSeatsRequest.getMailId()).getFirst();
        if(user == null){
            return helperUtil.errorResponse("This request couldn't be processed. Please enter a valid Email ID");
        }

        List<Seat> allSeats = theatreService.getAllSeats(bookSeatsRequest);
        if(allSeats == null || allSeats.isEmpty()){
            return helperUtil.errorResponse("This request couldn't be processed. Hall is full");
        }


        ResponseEntity<TheatreResponse> theatreRes = theatreService.getTheatresByCityNameDate(bookSeatsRequest.getCityName(),
                                            bookSeatsRequest.getTheatreName(),
                                            bookSeatsRequest.getDate());
        Theatre theatre;
        if(theatreRes.hasBody() && theatreRes.getBody() != null && !theatreRes.getBody().getTheatreList().isEmpty()){
            theatre = theatreRes.getBody().getTheatreList().getFirst();
        }else{
            return helperUtil.errorResponse("There is no such theatre with the entered combination");
        }

        if(theatre != null){
            List<Hall> hallsInTheatre = theatre.getHalls();
            Hall requiredHall = theatreService.getHallFromTheatre(theatre, bookSeatsRequest.getHallId());

            if(requiredHall != null){
                int idxOfHall = hallsInTheatre.indexOf(requiredHall);

                SeatLayout hallSeatLayout = requiredHall.getSeatLayoutPerShow().get(bookSeatsRequest.getShowTime());

                if(hallSeatLayout != null){
                    List<Seat> seats = hallSeatLayout.getSeats();

                    List<Seat> unavailableSeats = new ArrayList<>(bookSeatsRequest.getSeatsToBook().stream().map(seatToBook -> {
                        int idx = (seatToBook.getRow() - 1) * requiredHall.getNumberOfCols() + seatToBook.getCol() - 1;
                        Seat seat = allSeats.get(idx);
                        if (seat.isBooked()) {
                            return seat;
                        }
                        return null;
                    }).toList());

                    while (unavailableSeats.remove(null));

                    if(!unavailableSeats.isEmpty()){
                        List<String> messages = unavailableSeats.stream().map(seat -> "Seat row : " + seat.getRow() + " col : " + seat.getCol() + " is already booked").toList();
                        bookSeatResponse.setMessages(messages);
                        return ResponseEntity.status(HttpStatus.OK).body(bookSeatResponse);
                    }

                    bookSeatsRequest.getSeatsToBook().forEach(seatToBook -> {
                        int idx = (seatToBook.getRow() - 1)* requiredHall.getNumberOfCols()+ seatToBook.getCol() - 1;
                        Seat seat = seats.get(idx);
                        seat.setBooked(true);

                        Booking booking = helperUtil.createBooking(bookSeatsRequest, user);

                        List<Booking> userBookings = helperUtil.getUserBookings(user);
                        userBookings.add(booking);
                        user.setUserBookings(userBookings);
                        userService.adduserIntoDB(user);

                        seat.setBooking(booking);

                        seats.set(idx, seat);
                    });
                    hallSeatLayout.setSeats(seats);
                    requiredHall.getSeatLayoutPerShow().put(bookSeatsRequest.getShowTime(), hallSeatLayout);
                }

                hallsInTheatre.set(idxOfHall, requiredHall);
                theatre.setHalls(hallsInTheatre);
            }

            theatreRepository.save(theatre);

            bookSeatResponse.setMailId(user.getMailId());
            List<String> messages = new ArrayList<>();
            bookSeatsRequest.getSeatsToBook().forEach(
                    seat -> messages.add("Seat row : " + seat.getRow() + " col : " + seat.getCol() + " is booked")
            );
            bookSeatResponse.setMessages(messages);
            bookSeatResponse.setStatus("Booking is successful");
            return ResponseEntity.ok(bookSeatResponse);
        }

        bookSeatResponse.setStatus("Sorry Booking couldn't be performed because of an Internal Error");
        return ResponseEntity.ok(bookSeatResponse);
    }
}
