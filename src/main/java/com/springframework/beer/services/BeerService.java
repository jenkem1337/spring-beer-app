package com.springframework.beer.services;

import com.springframework.beer.model.BeerDTO;
import com.springframework.beer.model.BeerStyle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    Optional<BeerDTO> getBeerById(UUID id);
    List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory);
    BeerDTO saveNewBeer(BeerDTO beerDTO);

    Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beerDTO);

    boolean deleteBeerById(UUID beerId);

    Optional<BeerDTO> patchUpdateBeerById(UUID beerId, BeerDTO beerDTO);
}
