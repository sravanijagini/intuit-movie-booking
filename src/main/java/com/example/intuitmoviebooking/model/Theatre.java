package com.example.intuitmoviebooking.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("theatres")
public class Theatre {

    @JsonProperty("halls")
    private List<Hall> halls;

    @Id
    @JsonProperty("theatreName")
    private String theatreName;

    @JsonProperty("cityName")
    private String cityName;

    @JsonProperty("locationDetails")
    private String locationDetails;

}
