package com.example.intuitmoviebooking.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("theatres")
public class Theatre {

    @Id
    private String id;
    private int seq;

    @JsonProperty("theatreId")
    private int theatreId;

    @JsonProperty("theatreName")
    private String theatreName;

    @JsonProperty("movies")
    private List<String> movies;

    @JsonProperty("halls")
    private List<Hall> halls;

    @JsonProperty("cityName")
    private String cityName;

    @JsonProperty("locationDetails")
    private String locationDetails;

}
