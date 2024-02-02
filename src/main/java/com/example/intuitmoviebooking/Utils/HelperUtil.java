package com.example.intuitmoviebooking.Utils;

import com.example.intuitmoviebooking.model.Booking;
import com.example.intuitmoviebooking.model.Requests.BookSeatsRequest;
import com.example.intuitmoviebooking.model.Responses.BookSeatResponse;
import com.example.intuitmoviebooking.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Component
public class HelperUtil {

    public Booking createBooking(BookSeatsRequest bookSeatsRequest, User user){
        Booking booking = new Booking();
        UUID bId = UUID.randomUUID();
        booking.setBookingId(bId);
        booking.setTheatreName(bookSeatsRequest.getTheatreName());
        booking.setHallId(bookSeatsRequest.getHallId());
        booking.setUserMailId(user.getMailId());
        return booking;
    }

    public List<Booking> getUserBookings(User user){
        List<Booking> userBookings = user.getUserBookings();
        if(userBookings == null){
            userBookings = new ArrayList<>();
        }
        return userBookings;
    }

    public ResponseEntity<BookSeatResponse> errorResponse(String message){

        BookSeatResponse bookSeatResponse = new BookSeatResponse();
        List<String> res = new ArrayList<>();
        res.add(message);
        bookSeatResponse.setMessages(res);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bookSeatResponse);
    }
}
