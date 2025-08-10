package com.springframework.beer.controller;

import com.springframework.beer.model.Beer;
import com.springframework.beer.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {
    private final BeerService beerService;


    @PostMapping
    public ResponseEntity<Beer> saveNewBeer(@RequestBody Beer beer) {
        var savedBeer = beerService.saveNewBeer(beer);
        return new ResponseEntity<>(savedBeer, HttpStatus.CREATED);
    }
    @RequestMapping(method = RequestMethod.GET)
    public List<Beer> listBeers(){
        log.debug("BeerController::listBeers");
        return beerService.listBeers();
    }
    @RequestMapping(value = "{beerId}", method = RequestMethod.GET)
    public Beer getBeerById(
            @PathVariable("beerId")
            UUID beerId) {
        log.debug("BeerController::getBeerById");
        return beerService.getBeerById(beerId);
    }
}
