package com.example.intuitmoviebooking.repository;

import com.example.intuitmoviebooking.model.Theatre;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TheatreRepository extends MongoRepository<Theatre, String> {

    @Query(value = "{cityName:'?1'}")
    List<Theatre> findAll(String cityName);

}
