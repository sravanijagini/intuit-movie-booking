package com.example.intuitmoviebooking.services;

import com.example.intuitmoviebooking.Utils.HelperUtil;
import com.example.intuitmoviebooking.model.*;
import com.example.intuitmoviebooking.model.Requests.BookSeatsRequest;
import com.example.intuitmoviebooking.model.Responses.TheatreResponse;
import com.example.intuitmoviebooking.repository.TheatreRepository;
import com.example.intuitmoviebooking.validator.RequestValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TheatreService {

    private final TheatreRepository theatreRepository;
    private final RequestValidation requestValidation;
    private final NextSequenceService nextSequenceService;
    private final MovieService movieService;
    private final MongoTemplate mongoTemplate;

    @Autowired
    TheatreService(TheatreRepository theatreRepository,
                   RequestValidation requestValidation,
                   NextSequenceService nextSequenceService,
                   MovieService movieService,
                   MongoTemplate mongoTemplate) {
        this.theatreRepository = theatreRepository;
        this.requestValidation = requestValidation;
        this.nextSequenceService = nextSequenceService;
        this.movieService = movieService;
        this.mongoTemplate = mongoTemplate;
    }

    public ResponseEntity<TheatreResponse> addNewTheatre(Theatre new_theatre){
        TheatreResponse theatreResponse = new TheatreResponse();
        List<String> validationList = requestValidation.validateTheatre(new_theatre);

        if(!validationList.isEmpty()){
            theatreResponse.setValidationList(validationList);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(theatreResponse);
        }

        List<Theatre> theatreList = theatreRepository.findAll(new_theatre.getCityName(), new_theatre.getTheatreName(), new_theatre.getDate());
        if(!theatreList.isEmpty()){
            List<String> res = new ArrayList<>();
            res.add("A Theatre Already Exists with the name : " + new_theatre.getTheatreName() + " in the city : " + new_theatre.getCityName()
                    + " at this date : " + new_theatre.getDate());
            theatreResponse.setValidationList(res);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(theatreResponse);
        }

        int theatreId;
        theatreId = nextSequenceService.getNextSequence("theatres");
        new_theatre.setTheatreId(theatreId);
        List<Hall> hallList = new_theatre.getHalls();

        hallList.forEach(hall -> {
            Map<String, SeatLayout> hm = null;
            hm = new HashMap<>();
            Map<String, SeatLayout> finalHm = hm;
            hall.getShowTimings().keySet().forEach(time -> finalHm.put(time, new SeatLayout()));

            finalHm.keySet().forEach(
                    key -> {
                        SeatLayout seatLayout = finalHm.get(key);
                        int rows = 1;
                        int cols = 1;

                        if(seatLayout.getSeats() == null || seatLayout.getSeats().isEmpty()){
                            List<Seat> new_seats = new ArrayList<>();

                            while(rows <= hall.getNumberOfRows()){
                                Seat new_seat = new Seat(rows, cols, false);
                                new_seats.add(new_seat);
                                cols++;

                                if(cols > hall.getNumberOfCols()){
                                    cols = 1;
                                    rows++;
                                }
                            }
                            seatLayout.setSeats(new_seats);
                        }
                        finalHm.put(key, seatLayout);
                    });
            hall.setSeatLayoutPerShow(finalHm);
        });

        theatreRepository.save(new_theatre);

        theatreList.add(new_theatre);
        theatreResponse.setTheatreList(theatreList);
        return ResponseEntity.ok(theatreResponse);
    }

    public ResponseEntity<TheatreResponse> getTheatresByCity(String cityName){
        TheatreResponse theatreResponse = new TheatreResponse();

        List<String> validationList = new ArrayList<>();
        String validationResponse = requestValidation.validateCity(cityName);
        if(validationResponse != null)
            validationList.add(validationResponse);

        if(!validationList.isEmpty()){
            theatreResponse.setValidationList(validationList);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(theatreResponse);
        }

        List<Theatre> theatreList = theatreRepository.findAll(cityName);
        theatreResponse.setTheatreList(theatreList);
        return ResponseEntity.ok(theatreResponse);
    }

    public ResponseEntity<TheatreResponse> getTheatresByCityAndName(String city, String theatreName){
        TheatreResponse theatreResponse = new TheatreResponse();

        List<Theatre> theatreList = theatreRepository.findAll(city, theatreName);
        theatreResponse.setTheatreList(theatreList);

        return ResponseEntity.ok(theatreResponse);
    }

    public ResponseEntity<TheatreResponse> getTheatresByCityAndMovie(String city, String movie){
        TheatreResponse theatreResponse = new TheatreResponse();

        List<Theatre> theatreList = theatreRepository.findAll(city);
        List<Theatre> theatreListWithMovie = theatreList.stream()
                .filter(theatre -> {
                    if(theatre.getMovies() != null && !theatre.getMovies().isEmpty()) {
                        return theatre.getMovies().contains(movie);
                    }
                    return false;
                })
                .toList();

        theatreResponse.setTheatreList(theatreListWithMovie);
        return ResponseEntity.ok(theatreResponse);
    }

    public ResponseEntity<List<Movie>> getListOfMoviesByTheatreId(int theatreId){
        Query query = new Query(Criteria.where("theatreId").is(theatreId));
        List<Theatre> theatreList =  mongoTemplate.find(query, Theatre.class);

        if(!theatreList.isEmpty()){
            List<String> movieList = new ArrayList<>(theatreList.getFirst().getMovies());

            List<List<Movie>> listOfList = movieList.stream().map(movieService::getMovieByTitle).toList();

            List<Movie> flattenedList = listOfList.stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(flattenedList);
        }

        return ResponseEntity.ok(null);
    }

    Theatre getTheatresByCityAndId(String city, int theatreId){
        Query query = new Query(Criteria.where("theatreId").is(theatreId));
        List<Theatre> theatreList =  mongoTemplate.find(query, Theatre.class);

        if(!theatreList.isEmpty()){

            List<Theatre> theatresFromDB = theatreList.stream().map(theatre -> {
                if(theatre.getCityName() != null && theatre.getCityName().equalsIgnoreCase(city)){
                    return theatre;
                }
                return null;
            }).toList();

            if(theatresFromDB.isEmpty()){
                return null;
            }

            return theatresFromDB.getFirst();
        }
        return null;
    }

    List<Seat> getAllSeats(BookSeatsRequest bookSeatsRequest){
        List<Theatre> theatreList = theatreRepository.findAll(bookSeatsRequest.getCityName() , bookSeatsRequest.getTheatreName());
        Theatre theatre = null;
        if(theatreList != null && !theatreList.isEmpty()){
            theatre = theatreList.getFirst();
        }

        if(theatre != null){
            List<Hall> hallsInTheatre = theatre.getHalls();
            Hall requiredHall = hallsInTheatre.stream()
                    .filter(hall -> hall.getHallId() == bookSeatsRequest.getHallId())
                    .toList().getFirst();

            if(requiredHall != null && requiredHall.getSeatLayoutPerShow()!= null && requiredHall.getSeatLayoutPerShow().get(bookSeatsRequest.getShowTime()) != null){
                return requiredHall.getSeatLayoutPerShow().get(bookSeatsRequest.getShowTime()).getSeats();
            }

        }
        return null;
    }

    public ResponseEntity<List<Seat>> getAvailableSeats(BookSeatsRequest bookSeatsRequest){
        List<Seat> allSeats = getAllSeats(bookSeatsRequest);
        if(allSeats != null && !allSeats.isEmpty()){
            List<Seat> bookedSeats =  allSeats.stream().filter(seat -> !seat.isBooked()).toList();
            return ResponseEntity.ok(bookedSeats);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    Hall getHallFromTheatre(Theatre theatre, int hallId){
        return theatre.getHalls().stream().map(hall -> {
            if(hall.getHallId() == hallId){
                return hall;
            }
            return null;
        }).toList().getFirst();

//        Hall requiredHall = hallsInTheatre.stream()
//                .filter(hall -> hall.getHallId() == bookSeatsRequest.getHallId())
//                .toList().getFirst();
    }


    public ResponseEntity<TheatreResponse> getTheatresByCityNameDate(String city, String theatreName, String date){
        TheatreResponse theatreResponse = new TheatreResponse();

        List<Theatre> theatreList = theatreRepository.findAll(city, theatreName, date);
        theatreResponse.setTheatreList(theatreList);

        return ResponseEntity.ok(theatreResponse);
    }
}
