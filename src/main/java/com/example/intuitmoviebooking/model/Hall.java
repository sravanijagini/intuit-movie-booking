package com.example.intuitmoviebooking.model;

import com.example.intuitmoviebooking.model.enums.TypeOfHall;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Hall {

    @JsonProperty("hallId")
    private int hallId;

    @JsonProperty("typeOfHall")
    private TypeOfHall typeOfHall;

    @JsonProperty("time-movie")
    private Map<String, String> showTimings; // 9:00 -> race , 10:00 -> race2

    @JsonProperty("time-seatLayout")
    private Map<String, SeatLayout> seatLayoutPerShow;

//    public Hall(int hallId, String typeOfHall, HashMap<String, SeatLayout> seatLayoutPerShow) {
//        this.hallId = hallId;
//        this.typeOfHall = TypeOfHall.valueOf(typeOfHall);
//        this.setSeatLayoutPerShow(seatLayoutPerShow);
//    }
}
