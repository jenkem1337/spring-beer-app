package com.springframework.beer.services;

import com.springframework.beer.model.Beer;
import com.springframework.beer.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {
    private final AtomicInteger versionCounter = new AtomicInteger();
    @Override
    public Beer getBeerById(UUID id) {
        log.debug("BeerServiceImpl::getBeerByID -- id : {}", id.toString());
        return Beer.builder()
                .id(id)
                .version(versionCounter.incrementAndGet())
                .beerName("Efes Pilsen")
                .beerStyle(BeerStyle.PILSNER)
                .price(BigDecimal.valueOf(80.00))
                .upc("123456")
                .quantityOnHand(1000)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
    }
}
