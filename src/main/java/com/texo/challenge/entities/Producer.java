package com.texo.challenge.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producer implements Serializable {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;

    public Producer(String name) {
        this.name = name;
    }


}
