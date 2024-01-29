package com.example.intuitmoviebooking.repository;

import com.example.intuitmoviebooking.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MovieRepository extends MongoRepository<Movie, String> {

    @Query(value = "{movieTitle:'?0'}")
    List<Movie> findAll(String movieTitle);

}
