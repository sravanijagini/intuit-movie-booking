package com.example.intuitmoviebooking.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SeatLayout {

    @JsonProperty("seats")
    private List<Seat> seats;

}
