package com.example.intuitmoviebooking.services;

import com.example.intuitmoviebooking.model.Theatre;
import com.example.intuitmoviebooking.repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheatreService {

    private final TheatreRepository theatreRepository;

    @Autowired
    TheatreService(TheatreRepository theatreRepository) {
        this.theatreRepository = theatreRepository;
    }

    public List<Theatre> getListOfAllTheatres(String city){
        return theatreRepository.findAll(city);
    }

    public boolean addNewTheatre(Theatre new_theatre){

        List<Theatre> theatreList = getListOfAllTheatres(new_theatre.getCityName());

        boolean anyMatch = theatreList.stream()
                .anyMatch(theatre -> theatre.getTheatreName().equals(new_theatre.getTheatreName()));

        if(!anyMatch){
            theatreRepository.save(new_theatre);
        }

        return anyMatch;
    }



}
