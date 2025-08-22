package com.springframework.beer.services;

import com.springframework.beer.model.BeerDTO;
import com.springframework.beer.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {
    private final Map<UUID, BeerDTO> beerMap;
    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        BeerDTO beerDTO1 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        BeerDTO beerDTO2 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        BeerDTO beerDTO3 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        beerMap.put(beerDTO1.getId(), beerDTO1);
        beerMap.put(beerDTO2.getId(), beerDTO2);
        beerMap.put(beerDTO3.getId(), beerDTO3);

    }
    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        log.debug("BeerServiceImpl::getBeerByID -- id : {}", id.toString());
        return Optional.of(beerMap.get(id));
    }

    @Override
    public List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory) {
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beerDTO) {
        var newBeer = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName(beerDTO.getBeerName())
                .beerStyle(beerDTO.getBeerStyle())
                .upc(beerDTO.getUpc())
                .price(beerDTO.getPrice())
                .quantityOnHand(beerDTO.getQuantityOnHand())
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        beerMap.put(newBeer.getId(), newBeer);
        return newBeer;
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beerDTO) {
        var beerFromStorage = beerMap.get(beerId);
        beerFromStorage.setBeerName(beerDTO.getBeerName());
        beerFromStorage.setBeerStyle(beerDTO.getBeerStyle());
        beerFromStorage.setPrice(beerDTO.getPrice());
        beerFromStorage.setUpc(beerDTO.getUpc());
        beerFromStorage.setQuantityOnHand(beerDTO.getQuantityOnHand());
        beerFromStorage.setVersion(beerFromStorage.getVersion() + 1);
        beerFromStorage.setUpdatedDate(LocalDateTime.now());
        return Optional.of(beerFromStorage);
    }

    @Override
    public boolean deleteBeerById(UUID beerId) {
        beerMap.remove(beerId);
        return true;
    }

    @Override
    public Optional<BeerDTO> patchUpdateBeerById(UUID beerId, BeerDTO beerDTO) {
        var existing = beerMap.get(beerId);

        if (StringUtils.hasText(beerDTO.getBeerName())){
            existing.setBeerName(beerDTO.getBeerName());
        }

        if (beerDTO.getBeerStyle() != null) {
            existing.setBeerStyle(beerDTO.getBeerStyle());
        }

        if (beerDTO.getPrice() != null) {
            existing.setPrice(beerDTO.getPrice());
        }

        if (beerDTO.getQuantityOnHand() != null){
            existing.setQuantityOnHand(beerDTO.getQuantityOnHand());
        }

        if (StringUtils.hasText(beerDTO.getUpc())) {
            existing.setUpc(beerDTO.getUpc());
        }
        existing.setVersion(existing.getVersion() + 1);
        existing.setUpdatedDate(LocalDateTime.now());
        return Optional.of(existing);
    }
}
