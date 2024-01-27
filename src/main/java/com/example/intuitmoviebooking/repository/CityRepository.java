package com.example.intuitmoviebooking.repository;

import com.example.intuitmoviebooking.model.City;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CityRepository extends MongoRepository<City, String> {

    @Query(value="{cityName:'?0'}")
    List<City> findAll(String cityName);

}
