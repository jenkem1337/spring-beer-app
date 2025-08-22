package com.springframework.beer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springframework.beer.model.BeerDTO;
import com.springframework.beer.services.BeerService;
import com.springframework.beer.services.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Optional;
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
    ArgumentCaptor<BeerDTO> beerArgumentCaptor;

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
    void saveNewBeerWithNullBeerName() throws Exception {
        var dto = BeerDTO.builder().build();
        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.listBeers(null).getFirst());

        mockMvc.perform(post("/api/v1/beer")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(jsonPath("$.length()", is(6)))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void saveNewBeer() throws Exception {
        var beer = beerServiceImpl.listBeers(null).get(0);
        beer.setId(null);
        beer.setVersion(null);

        var obj = mapper.writeValueAsString(beer);

        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.listBeers(null).get(1));

        mockMvc.perform(post("/api/v1/beer")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }
    @Test
    void listBeers() throws Exception {
        given(beerService.listBeers(null)).willReturn(beerServiceImpl.listBeers(null));

        mockMvc.perform(get("/api/v1/beer").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }
    @Test
    void getBeerById() throws Exception {
        var beer = beerServiceImpl.listBeers(null).get(0);
        given(beerService.getBeerById(beer.getId())).willReturn(Optional.of(beer));
        mockMvc.perform(get("/api/v1/beer/"+beer.getId().toString())
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(jsonPath("$.id", is(beer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(beer.getBeerName())))
                .andExpect(status().isOk());
    }

    @Test
    void updateBeerById() throws Exception {
        var beer = beerServiceImpl.listBeers(null).getFirst();
        given(beerService.updateBeerById(any(UUID.class), any(BeerDTO.class))).willReturn(Optional.of(beer));
        mockMvc.perform(put("/api/v1/beer/"+beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(beer)))
                .andExpect(status().isNoContent());

        verify(beerService, times(1)).updateBeerById(any(UUID.class), any(BeerDTO.class));
    }

    @Test
    void updateBeerByIdBlankName() throws Exception {
        var beer = beerServiceImpl.listBeers(null).getFirst();
        beer.setBeerName("");
        given(beerService.updateBeerById(any(UUID.class), any(BeerDTO.class))).willReturn(Optional.of(beer));
        mockMvc.perform(put("/api/v1/beer/"+beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(beer)))
                .andExpect(status().isBadRequest());

        verify(beerService, times(0)).updateBeerById(any(UUID.class), any(BeerDTO.class));
    }


    @Test
    void deleteBeerById() throws Exception {
        var beer =beerServiceImpl.listBeers(null).getFirst();

        given(beerService.deleteBeerById(any(UUID.class))).willReturn(true);

        mockMvc.perform(delete("/api/v1/beer/"+beer.getId().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());

    }
    @Test
    void patchUpdateBeerById() throws Exception {
        var beer = beerServiceImpl.listBeers(null).getFirst();

        var beerMap = new HashMap<String, String>();
        beerMap.put("beerName", "Tuborg Gold");
        given(beerService.patchUpdateBeerById(any(), any())).willReturn(Optional.of(beer));
        mockMvc.perform(patch("/api/v1/beer/"+beer.getId().toString())
                .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());

        verify(beerService).patchUpdateBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());

    }

    @Test
    void getBeerByIdNotFound() throws Exception {
        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/beer/"+ UUID.randomUUID())).andExpect(
                status().isNotFound()
        );
    }
}