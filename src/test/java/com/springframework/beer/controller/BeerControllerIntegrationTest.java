package com.springframework.beer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springframework.beer.entites.Beer;
import com.springframework.beer.mappers.BeerMapper;
import com.springframework.beer.model.BeerDTO;
import com.springframework.beer.repositories.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.beans.Transient;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class BeerControllerIntegrationTest {
    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper mapper;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    @Test
    void getBeerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.getBeerById(UUID.randomUUID());
        });
    }

    @Test
    void getBeerById() {
        var beer = beerRepository.findAll().getFirst();
        var beerDto = beerController.getBeerById(beer.getId());
        assertThat(beerDto).isNotNull();
    }

    @Test
    void listBeers() {
        var beers = beerController.listBeers();
        assertThat(beers.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void emptyList() {
        beerRepository.deleteAll();
        var beers = beerController.listBeers();
        assertThat(beers.size()).isEqualTo(0);

    }
    @Rollback
    @Transactional
    @Test
    void updateBeerByIdAndFindBeer() {
        var beer = beerRepository.findAll().getFirst();
        var  beerDto = mapper.beerToBeerDto(beer);
        beerDto.setId(null);
        beerDto.setVersion(null);
        final String newBeerName = "Tuborg Gold";
        beerDto.setBeerName(newBeerName);
        var response = beerController.updateBeerById(beer.getId(), beerDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        var updatedBeer = beerController.getBeerById(beer.getId());
        assertThat(updatedBeer).isNotNull();
        assertThat(updatedBeer.getBeerName()).isEqualTo(newBeerName);

    }

    @Rollback
    @Transactional
    @Test
    void saveNewBeerAndFindBeer() {
        var newBeer = BeerDTO.builder()
                .beerName("Efes Pilsen")
                .build();

        var response = beerController.saveNewBeer(newBeer);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(response.getHeaders().getLocation()).isNotNull();

        String[] splitLocationHeader = response.getHeaders().getLocation().getPath().split("/");
        UUID beerUUID = UUID.fromString( splitLocationHeader[4]);
        var beer = beerController.getBeerById(beerUUID);
        assertThat(beer).isNotNull();
    }

    @Test
    void updateBeerByIdNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            beerController.updateBeerById(UUID.randomUUID(), BeerDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void deleteBeerById() {
        var beer = beerRepository.findAll().getFirst();

        var response = beerController.deleteBeerById(beer.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(beerRepository.findById(beer.getId()).isEmpty()).isTrue();
    }

    @Test
    void deleteBeerByIdNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            beerController.deleteBeerById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void patchUpdateBeerById() {
        var beer = beerRepository.findAll().getFirst();
        var  beerDto = mapper.beerToBeerDto(beer);
        beerDto.setId(null);
        beerDto.setVersion(null);
        final String newBeerName = "Tuborg Gold";
        beerDto.setBeerName(newBeerName);
        var response = beerController.patchUpdateBeerById(beer.getId(), beerDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        var updatedBeer = beerController.getBeerById(beer.getId());
        assertThat(updatedBeer).isNotNull();
        assertThat(updatedBeer.getBeerName()).isEqualTo(newBeerName);
    }

    @Test
    void patchUpdateByIdNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            beerController.patchUpdateBeerById(UUID.randomUUID(), BeerDTO.builder().build());
        });
    }

}