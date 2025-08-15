package com.springframework.beer.mappers;

import com.springframework.beer.entites.Beer;
import com.springframework.beer.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {
    Beer beerDtoToBeer(BeerDTO dto);
    BeerDTO beerToBeerDto(Beer beer);
}
