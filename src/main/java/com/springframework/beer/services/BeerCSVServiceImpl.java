package com.springframework.beer.services;

import com.opencsv.bean.CsvToBeanBuilder;
import com.springframework.beer.model.BeerCSVRecord;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
@Service
public class BeerCSVServiceImpl implements BeerCSVService {
    @Override
    public List<BeerCSVRecord> convertCSV(File csvFile) throws FileNotFoundException {
        return new CsvToBeanBuilder<BeerCSVRecord>(new FileReader(csvFile))
                .withType(BeerCSVRecord.class)
                .build().parse();
    }
}
