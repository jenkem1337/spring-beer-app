package com.springframework.beer.controller;

import com.springframework.beer.model.Beer;
import com.springframework.beer.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
public class BeerController {
    private final BeerService beerService;

    @RequestMapping("/api/v1/beer")
    public List<Beer> listBeers(){
        return beerService.listBeers();
    }
    public Beer getBeerById(UUID uuid) {
        log.debug("BeerController::getBeerById");
        return beerService.getBeerById(uuid);
    }
}
