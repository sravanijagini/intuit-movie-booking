package com.example.intuitmoviebooking.model;

import com.example.intuitmoviebooking.model.enums.TypeOfHall;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@Data
public class Hall {

    private int hallId;

    private TypeOfHall typeOfHall;

    private HashMap<DateTimeFormatter, Movie> hallInformation;

    private List<Integer> numberOfSeatsInEachRow;

    private List<Seat> typesOfSeats;

}
