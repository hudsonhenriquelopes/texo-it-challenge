package com.texo.challenge;

import com.texo.challenge.controllers.AwardsIntervalController;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class ChallengeInit {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChallengeInit.class);

    @Autowired
    AwardsIntervalController controller;

    @PostConstruct
    public void loadDefault() {
        controller.readMoviesFromFile("./src/main/resources/movielist.csv");
        LOGGER.warn("Default file `movielist.csv` is already loaded.");
    }
}
