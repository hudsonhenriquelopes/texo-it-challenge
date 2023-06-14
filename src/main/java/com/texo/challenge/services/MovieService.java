package com.texo.challenge.services;

import com.texo.challenge.entities.Movie;
import com.texo.challenge.entities.Producer;
import com.texo.challenge.models.MovieDto;
import com.texo.challenge.repositories.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void storeMoviesWithProducers(List<Producer> producers, Set<MovieDto> list) {
        movieRepository.deleteAll();
        Set<Movie> movies = Movie.MovieBuilder.init(producers, list).build();
        movieRepository.saveAllAndFlush(movies);
    }
}
