package com.example.intuitmoviebooking.model;

import com.example.intuitmoviebooking.model.enums.TypeOfHall;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Hall {

    @JsonProperty("hallId")
    private int hallId;

    @JsonProperty("typeOfHall")
    private TypeOfHall typeOfHall;

    @JsonProperty("hallInformation")
    private Map<String, String> showTimings; // 9:00 -> race , 10:00 -> race2

    @JsonProperty("numberOfSeatsInEachRow")
    private List<Integer> numberOfSeatsInEachRow;

    @JsonProperty("seats")
    private List<Seat> seats;

}
