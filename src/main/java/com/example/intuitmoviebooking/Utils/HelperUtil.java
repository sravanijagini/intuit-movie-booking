package com.example.intuitmoviebooking.Utils;

import com.example.intuitmoviebooking.model.Booking;
import com.example.intuitmoviebooking.model.Requests.BookSeatsRequest;
import com.example.intuitmoviebooking.model.User;
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
        booking.setTheatreId(bookSeatsRequest.getTheatreId());
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
}
