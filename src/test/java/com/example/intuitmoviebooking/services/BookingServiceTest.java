package com.example.intuitmoviebooking.services;

import com.example.intuitmoviebooking.Utils.HelperUtil;
import com.example.intuitmoviebooking.controller.MovieBookingAPIs;
import com.example.intuitmoviebooking.model.*;
import com.example.intuitmoviebooking.model.Requests.BookSeatsRequest;
import com.example.intuitmoviebooking.model.Responses.BookSeatResponse;
import com.example.intuitmoviebooking.model.enums.TypeOfHall;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BookingServiceTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private MovieBookingAPIs movieBookingAPIs;


    @Mock
    private UserService userService;

    @Mock
    private HelperUtil helperUtil;
    @InjectMocks
    private TheatreService theatreService;

    private Theatre theatreObj(int theatreId, String cityName, String theatreName, List<String> movies, List<Hall> halls) {
        Theatre theatre = new Theatre();
        theatre.setTheatreId(theatreId);
        theatre.setCityName(cityName);
        theatre.setTheatreName(theatreName);
        theatre.setMovies(movies);
        theatre.setHalls(halls);
        return theatre;
    }

    @Test
    public void testBookSeats_Success() {

        BookSeatsRequest bookSeatsRequest = new BookSeatsRequest();
        when(bookingService.bookSeats(bookSeatsRequest)).thenReturn(ResponseEntity.ok(new BookSeatResponse()));

        ResponseEntity<BookSeatResponse> result = movieBookingAPIs.bookSeats(bookSeatsRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    public void testBookSeats_Exception() {

        BookSeatsRequest bookSeatsRequest = new BookSeatsRequest();
        when(bookingService.bookSeats(bookSeatsRequest)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        ResponseEntity<BookSeatResponse> result = movieBookingAPIs.bookSeats(bookSeatsRequest);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }


    @Test
    public void testBookSeats_Success_WithBody() {

        BookSeatsRequest bookSeatsRequest = new BookSeatsRequest("Bangalore", "PVR", 1, "9:00", Collections.singletonList(new Seat(1, 1, false)));
        Hall hall = new Hall();
        hall.setHallId(1);
        hall.setTypeOfHall(TypeOfHall.valueOf("TWO_DIMENSION"));
        hall.setSeatLayoutPerShow(new HashMap<>());
        Theatre theatre = theatreObj(1,
                "Bangalore",
                "PVR",
                Collections.emptyList(),
                Collections.singletonList(hall));
        User user = new User();
        user.setUserName("");
        user.setMailId("");
        when(userService.getUserById(bookSeatsRequest.getMailId())).thenReturn(Collections.singletonList(user));
//        when(theatreService.getTheatresByCityAndName(bookSeatsRequest.getCityName(), bookSeatsRequest.getTheatreName())).thenReturn(theatre);
        when(theatreService.getAllSeats(bookSeatsRequest)).thenReturn(Collections.singletonList(new Seat(1, 1, false)));
        when(helperUtil.createBooking(any(), any())).thenReturn(new Booking());


        ResponseEntity<BookSeatResponse> result = bookingService.bookSeats(bookSeatsRequest);


        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertNotNull(result.getBody().getMailId());
        assertNotNull(result.getBody().getMessages());
        assertEquals("Booking is successful", result.getBody().getStatus());
    }
}
