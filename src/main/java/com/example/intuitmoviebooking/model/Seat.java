package com.example.intuitmoviebooking.model;

import com.example.intuitmoviebooking.model.enums.TypeOfSeat;
import lombok.Data;

import java.util.List;

@Data
public class Seat {

    private TypeOfSeat typeOfSeat;

    private int numberOfSeats;

    private List<Boolean> availabilityOfSeats;

}
