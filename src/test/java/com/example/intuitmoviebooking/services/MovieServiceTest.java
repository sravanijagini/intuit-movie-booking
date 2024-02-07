//package com.example.intuitmoviebooking.services;
//
//import com.example.intuitmoviebooking.model.Movie;
//import com.example.intuitmoviebooking.repository.MovieRepository;
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
//public class MovieServiceTest {
//
//    @Mock
//    private MovieRepository movieRepository;
//
//    @InjectMocks
//    private MovieService movieService;
//
//    @Test
//    public void testGetMovieByTitle() {
//        String movieTitle = "Inception";
//        Movie movie1 = new Movie();
//        movie1.setMovieTitle("Inception");
//
//        Movie movie2 = new Movie();
//        movie2.setMovieTitle("Inception");
//        when(movieRepository.findAll(movieTitle.toLowerCase())).thenReturn(Arrays.asList(movie1, movie2));
//
//        List<Movie> result = movieService.getMovieByTitle(movieTitle);
//
//        assertEquals(2, result.size());
//        assertTrue(result.contains(movie1));
//        assertTrue(result.contains(movie2));
//    }
//
//    @Test
//    public void testAddNewMovie() {
//        Movie newMovie = new Movie();
//        newMovie.setMovieTitle("Interstellar");
//        movieService.addNewMovie(newMovie);
//
//        verify(movieRepository, times(1)).save(newMovie);
//        assertEquals("interstellar", newMovie.getMovieTitle()); // Movie title should be converted to lowercase
//    }
//}
