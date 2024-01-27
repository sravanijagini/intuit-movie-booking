package com.example.intuitmoviebooking.model;

import lombok.Data;

import java.util.List;

@Data
public class Theater {

    private int theaterId;

    private String theaterName;

    private City city;

    private String locationDetails;

    private List<Hall> Halls;

}
