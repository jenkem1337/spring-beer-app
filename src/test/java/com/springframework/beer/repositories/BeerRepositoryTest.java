package com.springframework.beer.repositories;

import com.springframework.beer.bootstrap.BootstrapData;
import com.springframework.beer.entites.Beer;
import com.springframework.beer.model.BeerDTO;
import com.springframework.beer.model.BeerStyle;
import com.springframework.beer.services.BeerCSVServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@Import({BootstrapData.class, BeerCSVServiceImpl.class})
class BeerRepositoryTest {
    @Autowired
    BeerRepository beerRepository;

    @Test
    void listBeersByName(){
        List<Beer> beers = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%");
        assertThat(beers.size()).isEqualTo(336);
    }

    @Test
    void saveNewBeerNameTooLong(){
        assertThrows(ConstraintViolationException.class, () -> {
            var savedBeer = beerRepository.save(
                    Beer.builder().beerName("Efes Pilsen 1111111111111111111111111111111111111111111111111111111111111111111111")
                            .beerStyle(BeerStyle.PILSNER)
                            .upc("12312312313211")
                            .price(new BigDecimal("80.00"))
                            .build()
            );
            beerRepository.flush();

        });

    }
    @Test
    void saveNewBeer() {
        var savedBeer = beerRepository.save(
                Beer.builder().beerName("Efes Pilsen")
                        .beerStyle(BeerStyle.PILSNER)
                        .upc("12312312313211")
                        .price(new BigDecimal("80.00"))
                        .build()
        );
        beerRepository.flush();
        assertThat(savedBeer.getId()).isNotNull();
        assertThat(savedBeer.getBeerName()).isEqualTo("Efes Pilsen");
    }
}