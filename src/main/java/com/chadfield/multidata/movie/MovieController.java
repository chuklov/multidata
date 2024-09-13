package com.chadfield.multidata.movie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/api/movies")
public class MovieController {


    private MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping(value="", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Movie> getAllMovies(WebSession session) {
        log.info("GET all movies");
        return movieService.findAll();
    }

}
