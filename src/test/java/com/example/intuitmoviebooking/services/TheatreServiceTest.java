package com.example.intuitmoviebooking.services;

import com.example.intuitmoviebooking.Utils.HelperUtil;
import com.example.intuitmoviebooking.model.*;
import com.example.intuitmoviebooking.model.Requests.BookSeatsRequest;
import com.example.intuitmoviebooking.model.Responses.BookSeatResponse;
import com.example.intuitmoviebooking.model.Responses.TheatreResponse;
import com.example.intuitmoviebooking.model.enums.TypeOfHall;
import com.example.intuitmoviebooking.repository.TheatreRepository;
import com.example.intuitmoviebooking.validator.RequestValidation;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TheatreServiceTest {

    @Mock
    private TheatreRepository theatreRepository;

    @Mock
    private RequestValidation requestValidation;

    @Mock
    private NextSequenceService nextSequenceService;

    @Mock
    private MovieService movieService;

    @Mock
    private UserService userService;

    @Mock
    private HelperUtil helperUtil;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private TheatreService theatreService;

    private Theatre theatreObj(int theatreId, String cityName, String theatreName, List<String> movies, List<Hall> halls) {
            Theatre theatre = new Theatre();
        theatre.setTheatreId(theatreId);
        theatre.setCityName(cityName);
        theatre.setTheatreName(theatreName);
        theatre.setMovies(movies);
        theatre.setHalls(halls);
        return theatre;
    }

    @Test
    public void testAddNewTheatre_Success() {
        
        Theatre newTheatre = theatreObj(1, "Bangalore", "PVR", Collections.emptyList(), Collections.emptyList());

        when(requestValidation.validateTheatre(newTheatre)).thenReturn(Collections.emptyList());
        when(theatreRepository.findAll(newTheatre.getCityName(), newTheatre.getTheatreName())).thenReturn(Collections.emptyList());
        when(nextSequenceService.getNextSequence("theatres")).thenReturn(1);

        
        ResponseEntity<TheatreResponse> result = theatreService.addNewTheatre(newTheatre);

        
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertNotNull(result.getBody().getTheatreList());
        assertEquals(1, result.getBody().getTheatreList().size());
        assertEquals(newTheatre, result.getBody().getTheatreList().get(0));
    }

    @Test
    public void testAddNewTheatre_ValidationFailure() {
        
        Theatre newTheatre = theatreObj(1, "Bangalore", "", Collections.emptyList(), Collections.emptyList());
        List<String> validationErrors = Arrays.asList("Theatre name is required", "City name is required");
        when(requestValidation.validateTheatre(newTheatre)).thenReturn(validationErrors);

        
        ResponseEntity<TheatreResponse> result = theatreService.addNewTheatre(newTheatre);

        
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());
        assertNotNull(result.getBody().getValidationList());
        assertEquals(validationErrors, result.getBody().getValidationList());
    }

    @Test
    public void testGetTheatresByCity_Success() {
        
        String cityName = "Bangalore";
        List<Theatre> theatres = Arrays.asList(theatreObj(1, cityName, "PVR", Collections.emptyList(), Collections.emptyList()));
        when(requestValidation.validateCity(cityName)).thenReturn(null);
        when(theatreRepository.findAll(cityName)).thenReturn(theatres);

        
        ResponseEntity<TheatreResponse> result = theatreService.getTheatresByCity(cityName);

        
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertNotNull(result.getBody().getTheatreList());
        assertEquals(theatres, result.getBody().getTheatreList());
    }

    @Test
    public void testGetTheatresByCity_ValidationFailure() {
        
        String cityName = "";
        List<String> validationErrors = Collections.singletonList("City name is required");
        when(requestValidation.validateCity(cityName)).thenReturn(String.valueOf(validationErrors));

        
        ResponseEntity<TheatreResponse> result = theatreService.getTheatresByCity(cityName);

        
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());
        assertNotNull(result.getBody().getValidationList());
    }

    @Test
    public void testGetTheatresByCityAndName_Success() {
        
        String city = "Bangalore";
        String theatreName = "PVR";
        List<Theatre> theatres = Collections.singletonList(theatreObj(1, city, theatreName, Collections.emptyList(), Collections.emptyList()));
        when(theatreRepository.findAll(city, theatreName)).thenReturn(theatres);

        
        ResponseEntity<TheatreResponse> result = theatreService.getTheatresByCityAndName(city, theatreName);

        
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertNotNull(result.getBody().getTheatreList());
        assertEquals(theatres, result.getBody().getTheatreList());
    }

    @Test
    public void testGetTheatresByCityAndMovie_Success() {
        
        String city = "Bangalore";
        String movie = "PVR";
        List<Theatre> theatres = Collections.singletonList(theatreObj(1, city, "PVR", Collections.singletonList(movie), Collections.emptyList()));
        when(theatreRepository.findAll(city)).thenReturn(theatres);

        
        ResponseEntity<TheatreResponse> result = theatreService.getTheatresByCityAndMovie(city, movie);

        
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertNotNull(result.getBody().getTheatreList());
        assertEquals(theatres, result.getBody().getTheatreList());
    }

    @Test
    public void testGetListOfMoviesByTheatreId_Success() {
        
        int theatreId = 1;
        String cityName = "Bangalore";
        Theatre theatre = theatreObj(theatreId, cityName, "PVR", Collections.singletonList("Movie1"), Collections.emptyList());
        List<Theatre> theatreList = Collections.singletonList(theatre);
        when(mongoTemplate.find(any(), eq(Theatre.class))).thenReturn(theatreList);
        Movie newMovie = new Movie();
        newMovie.setMovieTitle("Race");
        when(movieService.getMovieByTitle("Race")).thenReturn(Arrays.asList(newMovie));

        
        ResponseEntity<List<Movie>> result = theatreService.getListOfMoviesByTheatreId(theatreId);

        
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals("Race", result.getBody().get(0).getMovieTitle());
    }

    @Test
    public void testGetAvailableSeats_Success() {
        
        BookSeatsRequest bookSeatsRequest = new BookSeatsRequest("Bangalore", 1, 1, "9:00", Collections.singletonList(new Seat(1, 1, false)));
        List<Seat> allSeats = Collections.singletonList(new Seat(1, 1, false));
        when(theatreService.getAllSeats(bookSeatsRequest)).thenReturn(allSeats);

        
        ResponseEntity<List<Seat>> result = theatreService.getAvailableSeats(bookSeatsRequest);
        
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(allSeats, result.getBody());
    }

    @Test
    public void testGetAvailableSeats_NoContent() {
        
        BookSeatsRequest bookSeatsRequest = new BookSeatsRequest("Bangalore", 1, 1, "9:00", Collections.emptyList());
        when(theatreService.getAllSeats(bookSeatsRequest)).thenReturn(null);

        ResponseEntity<List<Seat>> result = theatreService.getAvailableSeats(bookSeatsRequest);
        
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    public void testBookSeats_Success() {
        
        BookSeatsRequest bookSeatsRequest = new BookSeatsRequest("Bangalore", 1, 1, "9:00", Collections.singletonList(new Seat(1, 1, false)));
        Hall hall = new Hall();
        hall.setHallId(1);
        hall.setTypeOfHall(TypeOfHall.valueOf("TWO_DIMENSION"));
        hall.setSeatLayoutPerShow(new HashMap<>());
        Theatre theatre = theatreObj(1,
                "Bangalore",
                "PVR",
                Collections.emptyList(),
                Collections.singletonList(hall));
        User user = new User();
        user.setUserName("");
        user.setMailId("");
        when(userService.getUserById(bookSeatsRequest.getMailId())).thenReturn(Collections.singletonList(user));
        when(theatreService.getTheatresByCityAndId(bookSeatsRequest.getCityName(), bookSeatsRequest.getTheatreId())).thenReturn(theatre);
        when(theatreService.getAllSeats(bookSeatsRequest)).thenReturn(Collections.singletonList(new Seat(1, 1, false)));
        when(helperUtil.createBooking(any(), any())).thenReturn(new Booking());

        
        ResponseEntity<BookSeatResponse> result = theatreService.bookSeats(bookSeatsRequest);

        
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertNotNull(result.getBody().getMailId());
        assertNotNull(result.getBody().getMessages());
        assertEquals("Booking is successful", result.getBody().getStatus());
    }

}
