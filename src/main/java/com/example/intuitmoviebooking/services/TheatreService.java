package com.example.intuitmoviebooking.services;

import com.example.intuitmoviebooking.Utils.HelperUtil;
import com.example.intuitmoviebooking.model.*;
import com.example.intuitmoviebooking.model.Requests.BookSeatsRequest;
import com.example.intuitmoviebooking.model.Responses.BookSeatResponse;
import com.example.intuitmoviebooking.model.Responses.TheatreResponse;
import com.example.intuitmoviebooking.repository.TheatreRepository;
import com.example.intuitmoviebooking.validator.RequestValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TheatreService {

    private final TheatreRepository theatreRepository;
    private final RequestValidation requestValidation;
    private final NextSequenceService nextSequenceService;
    private final MovieService movieService;
    private final UserService userService;
    private final MongoTemplate mongoTemplate;
    private final HelperUtil helperUtil;

    @Autowired
    TheatreService(TheatreRepository theatreRepository,
                   RequestValidation requestValidation,
                   NextSequenceService nextSequenceService,
                   MovieService movieService,
                   UserService userService,
                   HelperUtil helperUtil,
                   MongoTemplate mongoTemplate) {
        this.theatreRepository = theatreRepository;
        this.requestValidation = requestValidation;
        this.nextSequenceService = nextSequenceService;
        this.movieService = movieService;
        this.userService = userService;
        this.helperUtil = helperUtil;
        this.mongoTemplate = mongoTemplate;
    }

    public ResponseEntity<TheatreResponse> addNewTheatre(Theatre new_theatre){
        TheatreResponse theatreResponse = new TheatreResponse();
        List<String> validationList = requestValidation.validateTheatre(new_theatre);

        if(!validationList.isEmpty()){
            theatreResponse.setValidationList(validationList);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(theatreResponse);
        }

        List<Theatre> theatreList = theatreRepository.findAll(new_theatre.getCityName(), new_theatre.getTheatreName());
        if(!theatreList.isEmpty()){
            List<String> res = new ArrayList<>();
            res.add("A Theatre Already Exists with the name : " + new_theatre.getTheatreName() + " in the city : " + new_theatre.getCityName());
            theatreResponse.setValidationList(res);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(theatreResponse);
        }

        int theatreId;
        theatreId = nextSequenceService.getNextSequence("theatres");
        new_theatre.setTheatreId(theatreId);
        List<Hall> hallList = new_theatre.getHalls();

        hallList.forEach(hall -> {
            Map<String, SeatLayout> hm = hall.getSeatLayoutPerShow();
            hm.keySet().forEach(
                    key -> {
                        SeatLayout seatLayout = hm.get(key);
                        int rows = 1;
                        int cols = 1;

                        if(seatLayout.getSeats() == null || seatLayout.getSeats().isEmpty()){
                            List<Seat> new_seats = new ArrayList<>();

                            while(rows <= seatLayout.getRows()){
                                Seat new_seat = new Seat(rows, cols, false);
                                new_seats.add(new_seat);
                                cols++;

                                if(cols > seatLayout.getCols()){
                                    cols = 1;
                                    rows++;
                                }
                            }
                            seatLayout.setSeats(new_seats);
                        }
                        hm.put(key, seatLayout);
                    });
            hall.setSeatLayoutPerShow(hm);
        });

//        hallList.forEach(hall -> {
//            SeatLayout seatLayout = hall.getSeatLayout();
//            int rows = 1;
//            int cols = 1;
//
//            if(seatLayout.getSeats() == null || seatLayout.getSeats().isEmpty()){
//                List<Seat> new_seats = new ArrayList<>();
//
//                while(rows <= seatLayout.getRows()){
//                    Seat new_seat = new Seat(rows, cols, false);
//                    new_seats.add(new_seat);
//                    cols++;
//
//                    if(cols > seatLayout.getCols()){
//                        cols = 1;
//                        rows++;
//                    }
//                }
//                seatLayout.setSeats(new_seats);
//            }
//
//
//
//            hall.setSeatLayout(seatLayout);
//        });

        theatreRepository.save(new_theatre);

        theatreList.add(new_theatre);
        theatreResponse.setTheatreList(theatreList);
        return ResponseEntity.ok(theatreResponse);
    }

    public ResponseEntity<TheatreResponse> getTheatresByCity(String cityName){
        TheatreResponse theatreResponse = new TheatreResponse();

        List<String> validationList = new ArrayList<>();
        String validationResponse = requestValidation.validateCity(cityName);
        if(validationResponse != null)
            validationList.add(validationResponse);

        if(!validationList.isEmpty()){
            theatreResponse.setValidationList(validationList);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(theatreResponse);
        }

        List<Theatre> theatreList = theatreRepository.findAll(cityName);
        theatreResponse.setTheatreList(theatreList);
        return ResponseEntity.ok(theatreResponse);
    }

    public ResponseEntity<TheatreResponse> getTheatresByCityAndName(String city, String theatreName){
        TheatreResponse theatreResponse = new TheatreResponse();

        List<Theatre> theatreList = theatreRepository.findAll(city, theatreName);
        theatreResponse.setTheatreList(theatreList);

        return ResponseEntity.ok(theatreResponse);
    }

    public ResponseEntity<TheatreResponse> getTheatresByCityAndMovie(String city, String movie){
        TheatreResponse theatreResponse = new TheatreResponse();

        List<Theatre> theatreList = theatreRepository.findAll(city);
        List<Theatre> theatreListWithMovie = theatreList.stream()
                .filter(theatre -> {
                    if(theatre.getMovies() != null && !theatre.getMovies().isEmpty()) {
                        return theatre.getMovies().contains(movie);
                    }
                    return false;
                })
                .toList();

        theatreResponse.setTheatreList(theatreListWithMovie);
        return ResponseEntity.ok(theatreResponse);
    }

    public ResponseEntity<List<Movie>> getListOfMoviesByTheatreId(int theatreId){
        Query query = new Query(Criteria.where("theatreId").is(theatreId));
        List<Theatre> theatreList =  mongoTemplate.find(query, Theatre.class);

        if(!theatreList.isEmpty()){
            List<String> movieList = new ArrayList<>(theatreList.getFirst().getMovies());

            List<List<Movie>> listOfList = movieList.stream().map(movieService::getMovieByTitle).toList();

            List<Movie> flattenedList = listOfList.stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(flattenedList);
        }

        return ResponseEntity.ok(null);
    }

    private Theatre getTheatresByCityAndId(String city, int  theatreId){
        Query query = new Query(Criteria.where("theatreId").is(theatreId));
        List<Theatre> theatreList =  mongoTemplate.find(query, Theatre.class);

        return theatreList.stream().map(theatre -> {
            if(theatre.getCityName() != null && theatre.getCityName().equalsIgnoreCase(city)){
                return theatre;
            }
            return null;
        }).toList().getFirst();
    }

    private List<Seat> getAllSeats(BookSeatsRequest  bookSeatsRequest){
        Theatre theatre = getTheatresByCityAndId(bookSeatsRequest.getCityName() , bookSeatsRequest.getTheatreId());

        List<Hall> hallsInTheatre = theatre.getHalls();
        Hall requiredHall = hallsInTheatre.stream()
                .filter(hall -> hall.getHallId() == bookSeatsRequest.getHallId())
                .toList().getFirst();

        if(requiredHall != null && requiredHall.getSeatLayoutPerShow()!= null && requiredHall.getSeatLayoutPerShow().get(bookSeatsRequest.getShowTime()) != null){
            return requiredHall.getSeatLayoutPerShow().get(bookSeatsRequest.getShowTime()).getSeats();
        }
        return null;
    }

    public ResponseEntity<List<Seat>> getAvailableSeats(BookSeatsRequest bookSeatsRequest){
        List<Seat> allSeats = getAllSeats(bookSeatsRequest);
        if(allSeats != null && !allSeats.isEmpty()){
            List<Seat> bookedSeats =  allSeats.stream().filter(seat -> !seat.isBooked()).toList();
            return ResponseEntity.ok(bookedSeats);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private Hall getHallFromTheatre(Theatre theatre, int hallId){
        return theatre.getHalls().stream().map(hall -> {
            if(hall.getHallId() == hallId){
                return hall;
            }
            return null;
        }).toList().getFirst();

//        Hall requiredHall = hallsInTheatre.stream()
//                .filter(hall -> hall.getHallId() == bookSeatsRequest.getHallId())
//                .toList().getFirst();
    }


    public ResponseEntity<BookSeatResponse> bookSeats(BookSeatsRequest bookSeatsRequest){
        BookSeatResponse bookSeatResponse = new BookSeatResponse();

        User user = userService.getUserById(bookSeatsRequest.getMailId()).getFirst();
        if(user == null){
            List<String> res = new ArrayList<>();
            res.add("This request couldn't be processed. Please enter a valid Email ID");
            bookSeatResponse.setMessages(res);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bookSeatResponse);
        }

        List<Seat> allSeats = getAllSeats(bookSeatsRequest);
        if(allSeats == null || allSeats.isEmpty()){
            List<String> res = new ArrayList<>();
            res.add("This request couldn't be processed. Hall hasn't been defined properly");
            bookSeatResponse.setMessages(res);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bookSeatResponse);
        }

        // update theatre repo
        Theatre theatre = getTheatresByCityAndId(bookSeatsRequest.getCityName(), bookSeatsRequest.getTheatreId());
        if(theatre != null){
            List<Hall> hallsInTheatre = theatre.getHalls();
            Hall requiredHall = getHallFromTheatre(theatre, bookSeatsRequest.getHallId());

            if(requiredHall != null){
                int idxOfHall = hallsInTheatre.indexOf(requiredHall);

                SeatLayout hallSeatLayout = requiredHall.getSeatLayoutPerShow().get(bookSeatsRequest.getShowTime());

                if(hallSeatLayout != null){
                    List<Seat> seats = hallSeatLayout.getSeats();

                    List<Seat> unavailableSeats = new ArrayList<>(bookSeatsRequest.getSeatsToBook().stream().map(seatToBook -> {
                        int idx = (seatToBook.getRow() - 1) * hallSeatLayout.getCols() + seatToBook.getCol() - 1;
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
                        int idx = (seatToBook.getRow() - 1)*hallSeatLayout.getCols() + seatToBook.getCol() - 1;
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
