package com.example.intuitmoviebooking.repository;

import com.example.intuitmoviebooking.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    @Query(value="{mailId:'?0'}")
    List<User> findAll(String mailId);



}
