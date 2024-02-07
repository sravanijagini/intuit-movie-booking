//package com.example.intuitmoviebooking.services;
//
//import com.example.intuitmoviebooking.model.City;
//import com.example.intuitmoviebooking.repository.CityRepository;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//public class CityServiceTest {
//
//    @Mock
//    private CityRepository cityRepository;
//
//    @InjectMocks
//    private CityService cityService;
//
//    @Test
//    public void testGetAllCities() {
//        City city1 = new City();
//        city1.setCityName("Bangalore");
//        City city2 = new City();
//        city1.setCityName("Hyderabad");
//
//        when(cityRepository.findAll()).thenReturn(Arrays.asList(city1, city2));
//
//        List<City> result = cityService.getAllCities();
//
//        assertEquals(2, result.size());
//        assertTrue(result.contains(city1));
//        assertTrue(result.contains(city2));
//    }
//
//    @Test
//    public void testAddNewCity_Success() {
//        City newCity = new City();
//        newCity.setCityName("NewCity");
//        when(cityRepository.findAll()).thenReturn(List.of()); // No existing cities
//
//        cityService.addNewCity(newCity);
//
//        verify(cityRepository, times(1)).save(newCity);
//        assertEquals("newcity", newCity.getCityName()); // City name should be converted to lowercase
//    }
//
//    @Test
//    public void testAddNewCity_Duplicate() {
//        City existingCity = new City();
//        existingCity.setCityName("ExistingCity");
//        when(cityRepository.findAll()).thenReturn(List.of(existingCity)); // Existing city with the same name
//
//        cityService.addNewCity(existingCity);
//
//        verify(cityRepository, never()).save(any()); // Should not be called since it's a duplicate
//    }
//}
