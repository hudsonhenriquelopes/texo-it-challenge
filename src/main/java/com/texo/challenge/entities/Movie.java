package com.texo.challenge.entities;

import com.texo.challenge.models.MovieDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Movie implements Serializable {
    @Id
    @GeneratedValue
    private UUID id;
    private int awardYear;
    private String title;
    private String studios;
    private boolean winner;
    @ElementCollection
    @CollectionTable(name = "movie_producer", joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id", table = "producer"))
    @Column(name = "producer_id")
    private List<UUID> producerIds;

    public Movie(int awardYear, String title, String studios, List<UUID> producerIds, boolean winner) {
        this.awardYear = awardYear;
        this.title = title;
        this.studios = studios;
        this.producerIds = producerIds;
        this.winner = winner;
    }

    public static class MovieBuilder {
        Set<MovieDto> movieDtos;
        Map<String, UUID> producers;

        public static MovieBuilder init(List<Producer> producers, Set<MovieDto> movieDtos) {
            MovieBuilder builder = new MovieBuilder();
            builder.producers = producers.stream().collect(Collectors.toMap(Producer::getName, Producer::getId));
            builder.movieDtos = movieDtos;
            return builder;
        }

        private Movie createMovie(MovieDto dto) {
            List<UUID> producerIds = new ArrayList<>();
            dto.producers().forEach(name -> producerIds.add(producers.get(name)));
            return new Movie(dto.awardYear(), dto.title(), dto.studios(), producerIds, dto.winner());
        }

        public Set<Movie> build() {
            Set<Movie> movies = new HashSet<>();
            movieDtos.forEach((movieDto -> movies.add(createMovie(movieDto))));
            return movies;
        }

    }
}
