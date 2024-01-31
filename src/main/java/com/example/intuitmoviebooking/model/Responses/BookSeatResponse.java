package com.example.intuitmoviebooking.model.Responses;

import com.example.intuitmoviebooking.model.Seat;
import lombok.Data;

import java.util.List;

@Data
public class BookSeatResponse {

    private String mailId;

    private int bookingId;

    private double price;

    private String status;

    private List<Seat> bookedSeats;

    private List<String> messages;
}
