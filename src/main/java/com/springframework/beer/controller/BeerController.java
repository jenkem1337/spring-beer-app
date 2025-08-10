package com.springframework.beer.controller;

import com.springframework.beer.model.Beer;
import com.springframework.beer.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Controller
public class BeerController {
    private final BeerService beerService;

    public Beer getBeerById(UUID uuid) {
        log.debug("BeerController::getBeerById");
        return beerService.getBeerById(uuid);
    }
}
