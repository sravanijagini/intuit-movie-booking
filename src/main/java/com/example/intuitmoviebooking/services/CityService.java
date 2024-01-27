package com.example.intuitmoviebooking.services;

import com.example.intuitmoviebooking.model.City;
import com.example.intuitmoviebooking.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    private final CityRepository cityRepository;

    @Autowired
    CityService(CityRepository cityRepository){
        this.cityRepository = cityRepository;
    }


    public List<City> getAllCities(){
        return cityRepository.findAll();
    }

    public void addNewCity(City new_city){
        // Get all Cities and add if it is not a duplicate
        List<City> cityList = getAllCities();
        boolean anyMatch = cityList.stream()
                .anyMatch(city -> city.getCityName().equals(new_city.getCityName()));

        if(!anyMatch){
            cityRepository.save(new_city);
        }
    }


}
