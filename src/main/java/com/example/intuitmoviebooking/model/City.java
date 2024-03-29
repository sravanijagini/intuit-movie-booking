package com.example.intuitmoviebooking.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Document("cities")
public class City {

    @Id
    @NotNull
    @JsonProperty("cityName")
    private String cityName;

    @JsonProperty("movies")
    private List<Movie> movies;

}
