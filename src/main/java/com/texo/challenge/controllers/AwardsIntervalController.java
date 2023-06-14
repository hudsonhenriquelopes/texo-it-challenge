package com.texo.challenge.controllers;

import com.texo.challenge.entities.Producer;
import com.texo.challenge.models.AwardIntervalDto;
import com.texo.challenge.models.MovieDto;
import com.texo.challenge.services.MovieService;
import com.texo.challenge.services.AwardIntervalsService;
import com.texo.challenge.services.ProducerService;
import com.texo.challenge.utils.ReadFileService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class AwardsIntervalController {

    private final MovieService movieService;
    private final ProducerService producerService;
    private final AwardIntervalsService awardIntervalsService;

    public AwardsIntervalController(MovieService movieService, ProducerService producerService, AwardIntervalsService awardIntervalsService) {
        this.movieService = movieService;
        this.producerService = producerService;
        this.awardIntervalsService = awardIntervalsService;
    }

    @PostMapping("/store")
    public String readMoviesFromFile(@RequestBody String filePath) {
        Set<MovieDto> items = ReadFileService.readMovies(filePath);

        List<Producer> producers = producerService.storeProducersFromMovies(items);
        movieService.storeMoviesWithProducers(producers, items);

        return "File content is stored.";
    }

    @GetMapping("/list-awards-interval")
    public Map<String, List<AwardIntervalDto>> getMinAndMaxAwardIntervals() {
        Map<String, List<AwardIntervalDto>> result = new HashMap<>();
        List<AwardIntervalDto> dtos = awardIntervalsService.getIntervalsBetweenAwards();

        OptionalInt optMin = dtos.stream().mapToInt(AwardIntervalDto::intervalYears).min();
        if (optMin.isPresent()) {
            result.put("min", dtos.stream().filter(awardIntervalDto -> awardIntervalDto.intervalYears() == optMin.getAsInt()).toList());
        }

        OptionalInt optMax = dtos.stream().mapToInt(AwardIntervalDto::intervalYears).max();
        if (optMax.isPresent()) {
            result.put("max", dtos.stream().filter(awardIntervalDto -> awardIntervalDto.intervalYears() == optMax.getAsInt()).toList());
        }

        return result;
    }
}

