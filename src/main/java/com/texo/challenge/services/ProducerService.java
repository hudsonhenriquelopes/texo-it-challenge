package com.texo.challenge.services;

import com.texo.challenge.entities.Producer;
import com.texo.challenge.models.MovieDto;
import com.texo.challenge.repositories.ProducerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProducerService {
    private final ProducerRepository producerRepository;

    public ProducerService(ProducerRepository producerRepository) {
        this.producerRepository = producerRepository;
    }

    public List<Producer> storeProducersFromMovies(Set<MovieDto> list) {
        producerRepository.deleteAll();
        Set<Producer> producerNames = list.stream()
                .map(MovieDto::producers)
                .flatMap(Set::stream)
                .map(Producer::new)
                .collect(Collectors.toSet());
        return producerRepository.saveAll(producerNames);
    }
}
