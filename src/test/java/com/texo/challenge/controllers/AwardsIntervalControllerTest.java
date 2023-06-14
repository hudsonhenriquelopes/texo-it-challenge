package com.texo.challenge.controllers;

import com.texo.challenge.entities.Producer;
import com.texo.challenge.models.MovieDto;
import com.texo.challenge.repositories.MovieRepository;
import com.texo.challenge.services.MovieService;
import com.texo.challenge.services.ProducerService;
import com.texo.challenge.utils.ReadFileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AwardsIntervalControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProducerService producerService;

    @Autowired
    MovieService movieService;

    @Autowired
    MovieRepository movieRepository;

    private static final String RESOURCE_PATH = "./src/test/resources/";

    @Test
    void testReadMoviesFromFileSuccess() throws Exception {
        String filePath = new File(RESOURCE_PATH + "movielist.csv").getAbsolutePath();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/store").accept(MediaType.TEXT_PLAIN_VALUE).content(filePath))
                .andExpect(status().isOk())
                .andExpect(content().string("File content is stored."));
    }

    @Test
    void testReadMoviesFromFileFailsWhenNotFound() throws Exception {
        String filePath = new File(RESOURCE_PATH + "invalid.csv").getAbsolutePath();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/store").accept(MediaType.TEXT_PLAIN_VALUE).content(filePath))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(String.format("File does not exist. (Path: \"%s\")", filePath)));
    }

    @Test
    void testGetMinAndMaxAwardIntervalsSuccessWhenEmpty() throws Exception {
        movieRepository.deleteAll();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/list-awards-interval"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.min").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.max").doesNotExist());
    }

    @Test
    void testGetMinAndMaxAwardIntervalsSuccess() throws Exception {
        String filePath = new File(RESOURCE_PATH + "movielist.csv").getAbsolutePath();
        Set<MovieDto> items = ReadFileService.readMovies(filePath);

        List<Producer> producers = producerService.storeProducersFromMovies(items);
        movieService.storeMoviesWithProducers(producers, items);

        MvcResult result =
                mockMvc.perform(MockMvcRequestBuilders.get("/api/list-awards-interval"))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andReturn();

        String expected = "{\"min\":[{\"producer\":\"Joel Silver\",\"interval\":1,\"previousWin\":1990,\"followingWin\":1991}],\"max\":[{\"producer\":\"Matthew Vaughn\",\"interval\":13,\"previousWin\":2002,\"followingWin\":2015}]}";
        assertEquals(expected, result.getResponse().getContentAsString());
    }
}