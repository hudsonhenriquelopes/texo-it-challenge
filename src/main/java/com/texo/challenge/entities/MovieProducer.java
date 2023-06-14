package com.texo.challenge.entities;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "movie_producer")
@NoArgsConstructor
public class MovieProducer implements Serializable {

    @EmbeddedId
    private MovieProducerId id;

    @Embeddable
    class MovieProducerId implements Serializable {
        @Column(name = "movie_id")
        public UUID movie;
        @Column(name = "producer_id")
        public UUID producer;
    }
}
