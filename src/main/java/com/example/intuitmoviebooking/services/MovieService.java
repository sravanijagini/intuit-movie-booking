package com.example.intuitmoviebooking.services;

import com.example.intuitmoviebooking.model.Movie;
import com.example.intuitmoviebooking.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    @Autowired
    MovieService(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    public List<Movie> getMovieByTitle(String movieTitle){
        return movieRepository.findAll(movieTitle.toLowerCase());
    }

    public void addNewMovie(Movie new_movie){

        new_movie.setMovieTitle(new_movie.getMovieTitle().toLowerCase());
        movieRepository.save(new_movie);

    }
}
