package com.example.intuitmoviebooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
//@EnableMongoRepositories
public class IntuitMovieBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntuitMovieBookingApplication.class, args);
	}

}
