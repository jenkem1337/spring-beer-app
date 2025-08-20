package com.springframework.beer.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BeerCSVServiceImplTest {
    BeerCSVService beerCSVService;
    @BeforeEach
    void setUp(){
        beerCSVService = new BeerCSVServiceImpl();
    }

    @Test
    void convertCSV() throws FileNotFoundException {
        var beerCsvRecords =  beerCSVService.convertCSV(ResourceUtils.getFile("classpath:csvdata/beers.csv"));
        assertThat(beerCsvRecords.size()).isGreaterThan(0);
    }
}