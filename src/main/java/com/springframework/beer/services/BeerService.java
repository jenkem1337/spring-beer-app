package com.springframework.beer.services;

import com.springframework.beer.model.Beer;

import java.util.UUID;

public interface BeerService {
    Beer getBeerById(UUID id);
}
