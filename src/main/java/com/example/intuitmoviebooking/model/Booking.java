package com.example.intuitmoviebooking.model;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class Booking {

    private UUID bookingId;
    private Date date;
    private int theatreId;
    private int hallId;
    private String userMailId;

}
