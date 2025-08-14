package com.springframework.beer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springframework.beer.model.Beer;
import com.springframework.beer.services.BeerService;
import com.springframework.beer.services.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.UUID;


import static org.mockito.ArgumentMatchers.any;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(BeerController.class)
class BeerControllerTest {
    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<Beer> beerArgumentCaptor;

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BeerService beerService;

    @Autowired
    ObjectMapper mapper;

    BeerServiceImpl beerServiceImpl;

    @BeforeEach
    void beforeEach() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void saveNewBeer() throws Exception {
        var beer = beerServiceImpl.listBeers().get(0);
        beer.setId(null);
        beer.setVersion(null);

        var obj = mapper.writeValueAsString(beer);

        given(beerService.saveNewBeer(any(Beer.class))).willReturn(beerServiceImpl.listBeers().get(1));

        mockMvc.perform(post("/api/v1/beer")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }
    @Test
    void listBeers() throws Exception {
        given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());

        mockMvc.perform(get("/api/v1/beer").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }
    @Test
    void getBeerById() throws Exception {
        var beer = beerServiceImpl.listBeers().get(0);
        given(beerService.getBeerById(beer.getId())).willReturn(beer);
        mockMvc.perform(get("/api/v1/beer/"+beer.getId().toString())
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(jsonPath("$.id", is(beer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(beer.getBeerName())))
                .andExpect(status().isOk());
    }

    @Test
    void updateBeerById() throws Exception {
        var beer = beerServiceImpl.listBeers().get(0);

        mockMvc.perform(put("/api/v1/beer/"+beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(beer)))
                .andExpect(status().isNoContent());

        verify(beerService, times(1)).updateBeerById(any(UUID.class), any(Beer.class));
    }

    @Test
    void deleteBeerById() throws Exception {
        var beer =beerServiceImpl.listBeers().getFirst();

        mockMvc.perform(delete("/api/v1/beer/"+beer.getId().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());

    }
    @Test
    void patchUpdateBeerById() throws Exception {
        var beer = beerServiceImpl.listBeers().getFirst();

        var beerMap = new HashMap<String, String>();
        beerMap.put("beerName", "Tuborg Gold");

        mockMvc.perform(patch("/api/v1/beer/"+beer.getId().toString())
                .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());

        verify(beerService).patchUpdateBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());

    }


}