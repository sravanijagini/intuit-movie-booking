package com.example.intuitmoviebooking.model.Requests;

import com.example.intuitmoviebooking.model.Seat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BookSeatsRequest {

    @JsonProperty("mailId")
    private String mailId;

    @JsonProperty("cityName")
    private String cityName;

    @JsonProperty("theatreId")
    private int theatreId;

    @JsonProperty("hallId")
    private int hallId;

    @JsonProperty("showTime")
    private String showTime;

    @JsonProperty("seatsToBook")
    private List<Seat> seatsToBook;

}
