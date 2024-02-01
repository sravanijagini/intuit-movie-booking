package com.example.intuitmoviebooking.controller;

import com.example.intuitmoviebooking.model.*;
import com.example.intuitmoviebooking.model.Requests.BookSeatsRequest;
import com.example.intuitmoviebooking.model.Responses.BookSeatResponse;
import com.example.intuitmoviebooking.model.Responses.TheatreResponse;
import com.example.intuitmoviebooking.services.CityService;
import com.example.intuitmoviebooking.services.MovieService;
import com.example.intuitmoviebooking.services.TheatreService;
import com.example.intuitmoviebooking.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MovieBookingAPIsTest {

    @Mock
    private CityService cityService;

    @Mock
    private UserService userService;

    @Mock
    private MovieService movieService;

    @Mock
    private TheatreService theatreService;

    @InjectMocks
    private MovieBookingAPIs movieBookingAPIs;

    @Test
    public void testAddNewCity_Success() {
        
        City city = new City();
        city.setCityName("Bangalore");
        doNothing().when(cityService).addNewCity(city);

        
        ResponseEntity<String> result = movieBookingAPIs.addNewCity(city);


        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    public void testAddNewCity_Exception() {
        
        City city = new City();
        city.setCityName("Bangalore");
        doThrow(new RuntimeException()).when(cityService).addNewCity(city);

        
        ResponseEntity<String> result = movieBookingAPIs.addNewCity(city);

        
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testGetAllCities_Success() {
        
        City city1 = new City();
        city1.setCityName("Bangalore");
        City city2 = new City();
        city2.setCityName("Bangalore");
        List<City> cities = Arrays.asList(city1, city2);
        when(cityService.getAllCities()).thenReturn(cities);

        
        ResponseEntity<List<City>> result = movieBookingAPIs.getAllCities();

        
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(cities, result.getBody());
    }

    @Test
    public void testGetAllCities_Exception() {
        
        when(cityService.getAllCities()).thenThrow(new RuntimeException());

        
        ResponseEntity<List<City>> result = movieBookingAPIs.getAllCities();

        
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testAddNewUser_Success() {
        
        User user = new User();
        user.setUserName("sravani");
        user.setMailId("test@mail.com");
        when(userService.addNewUser(user)).thenReturn(false);

        
        ResponseEntity<String> result = movieBookingAPIs.addNewUser(user);

        
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    public void testAddNewUser_UserAlreadyExists() {
        
        User user = new User();
        user.setUserName("Existing User");
        user.setMailId("existing@mail.com");
        when(userService.addNewUser(user)).thenReturn(true);

        
        ResponseEntity<String> result = movieBookingAPIs.addNewUser(user);

        
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("User Already Exists!", result.getBody());
    }

    @Test
    public void testAddNewUser_Exception() {
        User user = new User();
        user.setUserName("Existing User");
        user.setMailId("existing@mail.com");
        when(userService.addNewUser(user)).thenThrow(new RuntimeException());

        
        ResponseEntity<String> result = movieBookingAPIs.addNewUser(user);

        
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testGetUserById_Success() {
        User user = new User();
        user.setUserName("Existing User");
        user.setMailId("existing@mail.com");
        List<User> userList = Collections.singletonList(user);
        when(userService.getUserById("test@mail.com")).thenReturn(userList);

        
        ResponseEntity<User> result = movieBookingAPIs.getUserById("test@mail.com");

        
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(user, result.getBody());
    }

    @Test
    public void testGetUserById_UserNotFound() {
        
        when(userService.getUserById("nonexistent@mail.com")).thenReturn(Collections.emptyList());

        
        ResponseEntity<User> result = movieBookingAPIs.getUserById("nonexistent@mail.com");

        
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testGetUserById_Exception() {
        
        when(userService.getUserById("test@mail.com")).thenThrow(new RuntimeException());

        
        ResponseEntity<User> result = movieBookingAPIs.getUserById("test@mail.com");

        
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testAddNewMovie_Success() {
        
        Movie movie = new Movie();
        movie.setMovieTitle("Race");
        doNothing().when(movieService).addNewMovie(movie);

        
        ResponseEntity<String> result = movieBookingAPIs.addNewMovie(movie);

        
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    public void testAddNewMovie_Exception() {
        
        Movie movie = new Movie();
        movie.setMovieTitle("Race");
        doThrow(new RuntimeException()).when(movieService).addNewMovie(movie);

        
        ResponseEntity<String> result = movieBookingAPIs.addNewMovie(movie);

        
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testGetMoviesByTitle_Success() {
        
        Movie movie = new Movie();
        movie.setMovieTitle("Race");
        List<Movie> movies = Collections.singletonList(movie);
        when(movieService.getMovieByTitle("Race")).thenReturn(movies);

        
        ResponseEntity<List<Movie>> result = movieBookingAPIs.getMoviesByTitle("Race");

        
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(movies, result.getBody());
    }

    @Test
    public void testGetMoviesByTitle_MovieNotFound() {
        
        when(movieService.getMovieByTitle("NonexistentMovie")).thenReturn(Collections.emptyList());

        
        ResponseEntity<List<Movie>> result = movieBookingAPIs.getMoviesByTitle("NonexistentMovie");

        
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testGetMoviesByTitle_Exception() {
        
        when(movieService.getMovieByTitle("Race")).thenThrow(new RuntimeException());

        
        ResponseEntity<List<Movie>> result = movieBookingAPIs.getMoviesByTitle("Race");

        
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testAddNewTheatre_Success() {
        
        Theatre theatre = new Theatre();
        when(theatreService.addNewTheatre(theatre)).thenReturn(ResponseEntity.ok(new TheatreResponse()));

        
        ResponseEntity<TheatreResponse> result = movieBookingAPIs.addNewTheatre(theatre);

        
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testAddNewTheatre_Exception() {
        
        Theatre theatre = new Theatre();
        when(theatreService.addNewTheatre(theatre)).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

        
        ResponseEntity<TheatreResponse> result = movieBookingAPIs.addNewTheatre(theatre);

        
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testGetTheatresByCity_Success() {
        
        String cityName = "Bangalore";
        when(theatreService.getTheatresByCity(cityName)).thenReturn(ResponseEntity.ok(new TheatreResponse()));

        
        ResponseEntity<TheatreResponse> result = movieBookingAPIs.getAllTheatresByCity(cityName);

        
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testGetTheatresByCity_Exception() {
        
        String cityName = "Bangalore";
        when(theatreService.getTheatresByCity(cityName)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        
        ResponseEntity<TheatreResponse> result = movieBookingAPIs.getAllTheatresByCity(cityName);

        
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testGetTheatresByCityAndName_Success() {
        
        String city = "Bangalore";
        String theatreName = "PVR";
        when(theatreService.getTheatresByCityAndName(city, theatreName)).thenReturn(ResponseEntity.ok(new TheatreResponse()));

        
        ResponseEntity<TheatreResponse> result = movieBookingAPIs.getTheatresByCityAndName(city, theatreName);

        
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testGetTheatresByCityAndName_Exception() {
        
        String city = "Bangalore";
        String theatreName = "PVR";
        when(theatreService.getTheatresByCityAndName(city, theatreName)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        
        ResponseEntity<TheatreResponse> result = movieBookingAPIs.getTheatresByCityAndName(city, theatreName);

        
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testGetTheatresByCityAndMovie_Success() {
        
        String cityVal = "Bangalore";
        String movieVal = "Race";
        when(theatreService.getTheatresByCityAndMovie(cityVal, movieVal)).thenReturn(ResponseEntity.ok(new TheatreResponse()));

        
        ResponseEntity<TheatreResponse> result = movieBookingAPIs.getTheatresByCityAndMovie(cityVal, movieVal);

        
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testGetTheatresByCityAndMovie_Exception() {
        
        String cityVal = "Bangalore";
        String movieVal = "Race";
        when(theatreService.getTheatresByCityAndMovie(cityVal, movieVal)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        
        ResponseEntity<TheatreResponse> result = movieBookingAPIs.getTheatresByCityAndMovie(cityVal, movieVal);

        
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testGetListOfMoviesByTheatreId_Success() {
        
        String theatreId = "1";
        when(theatreService.getListOfMoviesByTheatreId(Integer.parseInt(theatreId))).thenReturn(ResponseEntity.ok(Collections.emptyList()));

        
        ResponseEntity<List<Movie>> result = movieBookingAPIs.getListOfMoviesByTheatreId(theatreId);

        
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(Collections.emptyList(), result.getBody());
    }

    @Test
    public void testGetListOfMoviesByTheatreId_Exception() {
        
        String theatreId = "1";
        when(theatreService.getListOfMoviesByTheatreId(Integer.parseInt(theatreId))).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        
        ResponseEntity<List<Movie>> result = movieBookingAPIs.getListOfMoviesByTheatreId(theatreId);

        
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testGetAvailableSeats_Success() {
        
        BookSeatsRequest bookSeatsRequest = new BookSeatsRequest();
        when(theatreService.getAvailableSeats(bookSeatsRequest)).thenReturn(ResponseEntity.ok(Collections.emptyList()));

        
        ResponseEntity<List<Seat>> result = movieBookingAPIs.getAvailableSeats(bookSeatsRequest);

        
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(Collections.emptyList(), result.getBody());
    }

    @Test
    public void testGetAvailableSeats_Exception() {
        
        BookSeatsRequest bookSeatsRequest = new BookSeatsRequest();
        when(theatreService.getAvailableSeats(bookSeatsRequest)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        
        ResponseEntity<List<Seat>> result = movieBookingAPIs.getAvailableSeats(bookSeatsRequest);

        
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testBookSeats_Success() {
        
        BookSeatsRequest bookSeatsRequest = new BookSeatsRequest();
        when(theatreService.bookSeats(bookSeatsRequest)).thenReturn(ResponseEntity.ok(new BookSeatResponse()));

        
        ResponseEntity<BookSeatResponse> result = movieBookingAPIs.bookSeats(bookSeatsRequest);

        
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    public void testBookSeats_Exception() {
        
        BookSeatsRequest bookSeatsRequest = new BookSeatsRequest();
        when(theatreService.bookSeats(bookSeatsRequest)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        
        ResponseEntity<BookSeatResponse> result = movieBookingAPIs.bookSeats(bookSeatsRequest);

        
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
}
