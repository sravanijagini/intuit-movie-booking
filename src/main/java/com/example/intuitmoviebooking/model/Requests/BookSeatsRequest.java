package com.example.intuitmoviebooking.model.Requests;

import com.example.intuitmoviebooking.model.Seat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BookSeatsRequest {

    @JsonProperty("mailId")
    private String mailId;

    @JsonProperty("date")
    private String date;

    @JsonProperty("cityName")
    private String cityName;

    @JsonProperty("theatreName")
    private String theatreName;

    @JsonProperty("hallId")
    private int hallId;

    @JsonProperty("showTime")
    private String showTime;

    @NotNull
    @JsonProperty("seatsToBook")
    private List<Seat> seatsToBook;

    public BookSeatsRequest(String cityName, String theatreName, int hallId, String showTime, List<Seat> seatsToBook) {
        this.cityName = cityName;
        this.theatreName = theatreName;
        this.hallId = hallId;
        this.showTime = showTime;
        this.setSeatsToBook(seatsToBook);
    }

    public BookSeatsRequest(){

    }
}
