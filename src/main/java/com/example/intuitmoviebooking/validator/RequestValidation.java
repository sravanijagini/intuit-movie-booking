package com.example.intuitmoviebooking.validator;

import com.example.intuitmoviebooking.model.City;
import com.example.intuitmoviebooking.model.Hall;
import com.example.intuitmoviebooking.model.Movie;
import com.example.intuitmoviebooking.model.Theatre;
import com.example.intuitmoviebooking.repository.CityRepository;
import com.example.intuitmoviebooking.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RequestValidation {

    private final MovieRepository movieRepository;
    private final CityRepository cityRepository;

    @Autowired
    RequestValidation(MovieRepository movieRepository, CityRepository cityRepository){
        this.movieRepository = movieRepository;
        this.cityRepository = cityRepository;
    }

    public List<String> validateTheatre(Theatre theatre) {
        List<String> invalid_timings = new ArrayList<>();
        List<String> invalid_movies = new ArrayList<>();

        if (theatre != null) {
            theatre.getHalls().forEach(hall ->
                invalid_timings.addAll(validateHallTimings(hall))
            );


            theatre.getHalls().forEach(hall ->
                invalid_movies.addAll(validateHallMovies(hall)));

            invalid_timings.addAll(invalid_movies);

            String cityValidation = validateCity(theatre.getCityName());
            if(cityValidation != null){
                invalid_timings.add(cityValidation);
            }

        }

        return invalid_timings;
    }

    public List<String> validateHallTimings(Hall hall){

        String valid_time_regex = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$";
        List<String> invalid_time = new ArrayList<>();

        Set<String> hs =  hall.getShowTimings().keySet();
        hs.forEach(time -> {
            if(!time.matches(valid_time_regex)){
                invalid_time.add("Invalid time format : " + time + ". Please use hh:mm format");
            }
        });

        return invalid_time;
    }

    public List<String> validateHallMovies(Hall hall){

        List<String> invalid_movies = new ArrayList<>();

        Collection<String> list_of_movies = hall.getShowTimings().values();
        List<Movie> movies = movieRepository.findAll();
        List<String> movie_names_db = movies.stream().map(Movie::getMovieTitle).toList();

        list_of_movies.forEach(movie_from_user ->
            {
                if(!movie_names_db.contains(movie_from_user.toLowerCase())){
                    invalid_movies.add("Invalid movie : " + movie_from_user + ". Please use a valid movie");
                }
            }
        );

        return invalid_movies;
    }

    public String validateCity(String cityName){
        List<City> cityList = cityRepository.findAll();
        List<String> cityNames = cityList.stream().map(City::getCityName).toList();

        return cityNames.contains(cityName) ? null : "Please enter a valid city";

    }

}
