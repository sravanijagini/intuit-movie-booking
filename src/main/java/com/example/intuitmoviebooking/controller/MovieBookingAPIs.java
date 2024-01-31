package com.example.intuitmoviebooking.controller;

import com.example.intuitmoviebooking.model.*;
import com.example.intuitmoviebooking.model.Requests.BookSeatsRequest;
import com.example.intuitmoviebooking.model.Responses.BookSeatResponse;
import com.example.intuitmoviebooking.model.Responses.TheatreResponse;
import com.example.intuitmoviebooking.services.CityService;
import com.example.intuitmoviebooking.services.MovieService;
import com.example.intuitmoviebooking.services.TheatreService;
import com.example.intuitmoviebooking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "mba") // Movie Booking Application
public class MovieBookingAPIs {

    private final CityService cityService;
    private final UserService userService;
    private final MovieService movieService;
    private final TheatreService theatreService;

    @Autowired
    MovieBookingAPIs(CityService cityService,
                     UserService userService,
                     MovieService movieService,
                     TheatreService theatreService){
        this.cityService = cityService;
        this.userService = userService;
        this.movieService = movieService;
        this.theatreService = theatreService;
    }

    // add a new city
    @PostMapping(value = "/add/city", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addNewCity(@RequestBody City city){
        try{
            cityService.addNewCity(city);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // get list of All Cities
    @GetMapping("/allCities")
    public ResponseEntity<List<City>> getAllCities(){
        List<City> cityList;

        try{
            cityList = cityService.getAllCities();
            return ResponseEntity.ok(cityList);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Create a new User
    @PostMapping(value = "/add/user", consumes = MediaType.APPLICATION_JSON_VALUE) 
    public ResponseEntity<String> addNewUser(@RequestBody User user){
        try{
            if(userService.addNewUser(user)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Already Exists!");
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/user/{mailId}")
    public ResponseEntity<User> getUserById(@PathVariable("mailId")  String mailId){
        List<User> userList;

        try{
            userList = userService.getUserById(mailId);
            return ResponseEntity.ok(userList.getFirst());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Create a new Movie
    @PostMapping(value = "/add/movie", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addNewMovie(@RequestBody Movie movie){
        try{
            movieService.addNewMovie(movie);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // get a movie by its title
    @GetMapping("/movie/{movieTitle}")
    public ResponseEntity<List<Movie>> getMoviesByTitle(@PathVariable("movieTitle")  String title){
        List<Movie> movieList;

        try{
            movieList = movieService.getMovieByTitle(title);
            return ResponseEntity.ok(movieList);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // create a new theatre
    @PostMapping(value = "/add/theatre", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TheatreResponse> addNewTheatre(@RequestBody Theatre theatre){
        try{
            return theatreService.addNewTheatre(theatre);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/theatre/city/{city}")
    public ResponseEntity<TheatreResponse> getAllTheatresByCity(@PathVariable("city")  String cityName){
        try{
            return theatreService.getTheatresByCity(cityName);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/theatre/theatreName")
    public ResponseEntity<TheatreResponse> getTheatresByCityAndName(@RequestParam("city") String city,
                                                                    @RequestParam("theatreName") String theatreName){
        try{
            return theatreService.getTheatresByCityAndName(city, theatreName);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/theatre/movie")
    public ResponseEntity<TheatreResponse> getTheatresByCityAndMovie(@RequestParam("city") String cityVal,
                                                                     @RequestParam("movie") String movieVal){
        try{
            return theatreService.getTheatresByCityAndMovie(cityVal, movieVal);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/theatres/{theatreId}/movies")
    public ResponseEntity<List<Movie>> getListOfMoviesByTheatreId(@PathVariable("theatreId") String theatreId){
        try{
            return theatreService.getListOfMoviesByTheatreId(Integer.parseInt(theatreId));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

//    @Get Available Seats for a Movie Showtime

    @GetMapping("/hall/availableSeats")
    public ResponseEntity<List<Seat>> getAvailableSeats(@RequestBody BookSeatsRequest bookSeatsRequest){
        try{
            return theatreService.getAvailableSeats(bookSeatsRequest);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/bookSeats")
    public ResponseEntity<BookSeatResponse> bookSeats(@RequestBody BookSeatsRequest bookSeatsRequest){
        try{
            return theatreService.bookSeats(bookSeatsRequest);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // getAllMoviesByCityName

    // getMovieInfoById

    // getAllTheatresByCityName&MovieId

    // getAllShowsByTheatreId, Movie & City

    // get All movies by theatre?

    // book a seat

}
