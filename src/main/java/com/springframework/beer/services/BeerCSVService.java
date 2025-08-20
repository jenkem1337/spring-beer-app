package com.springframework.beer.services;

import com.springframework.beer.model.BeerCSVRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public interface BeerCSVService {
    List<BeerCSVRecord> convertCSV(File csvFile) throws FileNotFoundException;
}
