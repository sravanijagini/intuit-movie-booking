package com.example.intuitmoviebooking.controller;

import com.example.intuitmoviebooking.model.City;
import com.example.intuitmoviebooking.model.Movie;
import com.example.intuitmoviebooking.model.User;
import com.example.intuitmoviebooking.services.CityService;
import com.example.intuitmoviebooking.services.MovieService;
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

    @Autowired
    MovieBookingAPIs(CityService cityService, UserService userService, MovieService movieService){
        this.cityService = cityService;
        this.userService = userService;
        this.movieService = movieService;
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

    // Create a new Booking

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



    // create a new theater

    // create a new movie



    // getAllMoviesByCityName

    // getMovieInfoById

    // getAllTheatersByCityName&MovieId

    // getAllShowsByTheaterId, Movie & City

    // get All movies by theater?

    // book a seat

}
