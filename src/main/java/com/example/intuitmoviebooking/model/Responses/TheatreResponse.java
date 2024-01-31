package com.example.intuitmoviebooking.model.Responses;

import com.example.intuitmoviebooking.model.Theatre;
import lombok.Data;

import java.util.List;

@Data
public class TheatreResponse {
    List<Theatre> theatreList;
    List<String> validationList;
}
