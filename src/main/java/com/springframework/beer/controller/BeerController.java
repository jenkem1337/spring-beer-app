package com.springframework.beer.controller;

import com.springframework.beer.model.BeerDTO;
import com.springframework.beer.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {
    private final BeerService beerService;

    @PatchMapping("{beerId}")
    public ResponseEntity<Void> patchUpdateBeerById(@PathVariable("beerId") UUID beerId, @RequestBody BeerDTO beerDTO){
        if(beerService.patchUpdateBeerById(beerId, beerDTO).isEmpty()) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{beerId}")
    public ResponseEntity<Void> deleteBeerById(@PathVariable("beerId") UUID beerId) {
        if(!beerService.deleteBeerById(beerId)) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
    @PutMapping("{beerId}")
    public ResponseEntity<Void> updateBeerById(@PathVariable("beerId") UUID beerId, @RequestBody BeerDTO beerDTO){
        log.debug(beerId.toString());
        if(beerService.updateBeerById(beerId, beerDTO).isEmpty()){
            throw new NotFoundException();
        }
        return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
    }
    @PostMapping
    public ResponseEntity<BeerDTO> saveNewBeer(@RequestBody BeerDTO beerDTO) {
        var savedBeer = beerService.saveNewBeer(beerDTO);
        var httpHeader = new HttpHeaders();
        httpHeader.add("Location", "/api/v1/beerDTO/"+savedBeer.getId().toString());
        return new ResponseEntity<>(savedBeer, httpHeader,HttpStatus.CREATED);
    }
    @RequestMapping(method = RequestMethod.GET)
    public List<BeerDTO> listBeers(){
        log.debug("BeerController::listBeers");
        return beerService.listBeers();
    }
    @RequestMapping(value = "{beerId}", method = RequestMethod.GET)
    public BeerDTO getBeerById(
            @PathVariable("beerId")
            UUID beerId) {
        log.debug("BeerController::getBeerById");
        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }
}
