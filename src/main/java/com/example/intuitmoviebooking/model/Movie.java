package com.example.intuitmoviebooking.model;

import com.example.intuitmoviebooking.model.enums.MovieCertification;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Document("movies")
public class Movie {

    @JsonIgnore
    private UUID movieId;

//    @NotNull
    @JsonProperty("movieTitle")
    private String movieTitle;

    @JsonProperty("movieCertification")
    private MovieCertification movieCertification;

    @JsonProperty("releaseDate")
    private Date releaseDate;

    @JsonProperty("cast")
    private List<String> cast;

    @JsonProperty("description")
    private String description;

    @JsonProperty("rating")
    private double rating;

    @JsonProperty("runTime")
    private String runTime;

    @JsonProperty("language")
    private String language;

    // ToDo : Poster ?
}
