package com.example.intuitmoviebooking.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Seat {
    @JsonProperty("row")
    private int row;

    @JsonProperty("col")
    private int col;

    @JsonProperty("isBooked")
    private boolean isBooked;

    private Booking booking;

    public Seat(int row, int col, boolean isBooked) {
        this.row = row;
        this.col = col;
        this.isBooked = isBooked;
    }
}