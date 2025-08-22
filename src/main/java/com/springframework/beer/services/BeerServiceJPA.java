package com.springframework.beer.services;

import com.springframework.beer.entites.Beer;
import com.springframework.beer.mappers.BeerMapper;
import com.springframework.beer.model.BeerDTO;
import com.springframework.beer.model.BeerStyle;
import com.springframework.beer.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper mapper;

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(mapper.beerToBeerDto(beerRepository.findById(id)
                .orElse(null)));
    }

    @Override
    public List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory) {
        List<Beer> beerList;
        if(StringUtils.hasText(beerName) && beerStyle == null) {
            beerList = listBeersByName(beerName);
        }
        else if(!StringUtils.hasText(beerName) && beerStyle != null) {
            beerList = listBeersByStyle(beerStyle);
        }
        else if (StringUtils.hasText(beerName) && beerStyle != null) {
            beerList = listBeersByNameAndStyle(beerName, beerStyle);
        }
        else {
            beerList = beerRepository.findAll();
        }
        if(showInventory != null && !showInventory) {
            beerList.forEach(beer -> beer.setQuantityOnHand(null));
        }
        return beerList
                .stream()
                .map(mapper::beerToBeerDto)
                .collect(Collectors.toList());
    }

    private List<Beer> listBeersByNameAndStyle(String beerName, BeerStyle beerStyle) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%"+beerName+"%", beerStyle);
    }

    private List<Beer> listBeersByName(String beerName){
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%"+beerName+"%");
    }

    private List<Beer> listBeersByStyle(BeerStyle beerStyle) {
        return beerRepository.findAllByBeerStyle(beerStyle  );
    }
    @Override
    public BeerDTO saveNewBeer(BeerDTO beerDTO) {
        return mapper.beerToBeerDto(beerRepository.save(mapper.beerDtoToBeer(beerDTO)));
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beerDTO) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();
        beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
            foundBeer.setBeerName(beerDTO.getBeerName());
            foundBeer.setBeerStyle(beerDTO.getBeerStyle());
            foundBeer.setUpc(beerDTO.getUpc());
            foundBeer.setPrice(beerDTO.getPrice());
            final var savedBeer = beerRepository.save(foundBeer);
            atomicReference.set(Optional.of(mapper.beerToBeerDto(savedBeer)));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

    @Override
    public boolean deleteBeerById(UUID beerId) {
        if(beerRepository.existsById(beerId)){
            beerRepository.deleteById(beerId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<BeerDTO> patchUpdateBeerById(UUID beerId, BeerDTO beerDTO) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();
        beerRepository.findById(beerId).ifPresentOrElse(beer -> {

            if (StringUtils.hasText(beerDTO.getBeerName())){
                beer.setBeerName(beerDTO.getBeerName());
            }

            if (beerDTO.getBeerStyle() != null) {
                beer.setBeerStyle(beerDTO.getBeerStyle());
            }

            if (beerDTO.getPrice() != null) {
                beer.setPrice(beerDTO.getPrice());
            }

            if (beerDTO.getQuantityOnHand() != null){
                beer.setQuantityOnHand(beerDTO.getQuantityOnHand());
            }

            if (StringUtils.hasText(beerDTO.getUpc())) {
                beer.setUpc(beerDTO.getUpc());
            }
            final var savedBeer = beerRepository.save(beer);
            atomicReference.set(Optional.of(mapper.beerToBeerDto(savedBeer)));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();

    }
}
