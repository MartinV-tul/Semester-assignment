package cz.tul.service;

import cz.tul.data.Country;
import cz.tul.parser.CSVParser;
import cz.tul.repositories.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);

    public void saveOrUpdate(Country country){
        countryRepository.save(country);
    }

    public boolean exists(String id){
        return countryRepository.existsById(id);
    }

    public List<Country> getAllCountries(){
        logger.info("Loading all countries from database.");
        return StreamSupport.stream(countryRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    public void deleteCountryByCode(String code){
        logger.warn("Deleting country: code="+code+".");
        countryRepository.deleteById(code);
    }

    public void deleteAllCountries(){
        logger.warn("Deleting all countries.");
        countryRepository.deleteAll();
    }
}
