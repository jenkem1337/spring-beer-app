package com.springframework.beer.repositories;

import com.springframework.beer.entites.Beer;
import com.springframework.beer.model.BeerStyle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {
    List<Beer> findAllByBeerNameIsLikeIgnoreCase(String beerName);
    List<Beer> findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(String beerName, BeerStyle beerStyle);
    List<Beer> findAllByBeerStyle(BeerStyle beerStyle);
}
