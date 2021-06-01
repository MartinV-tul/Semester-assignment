package cz.tul.service;


import cz.tul.data.Town;
import cz.tul.repositories.TownRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TownService {

    @Autowired
    private TownRepository townRepository;

    private static final Logger logger = LoggerFactory.getLogger(TownService.class);

    public List<Town> getTowns(){
        logger.info("Loading all towns.");
        return StreamSupport.stream(townRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public void saveOrUpdate(Town town){
        logger.info("Saving town: name="+town.getName()+", townId="+town.getId()+".");
        townRepository.save(town);
    }


    public List<Town> getTownsByCountryCode(String code){
        if(code == null)return null;
        logger.info("Loading all towns of country: code="+code+".");
        List<Town> towns = townRepository.findByCountryCode(code);
        if(towns.size() == 0) return null;

        return towns;
    }

    public void deleteByCountryCode(String code){
        if(code == null)return;
        logger.warn("Deleting all towns of country: code="+code+".");
        townRepository.deleteByCountryCode(code);
    }

    public boolean exists(Integer id){
        return townRepository.existsById(id);
    }

    public void deleteTownById(Integer id){
        logger.warn("Deleting town: townId="+id+".");
        townRepository.deleteTownById(id);
    }

    public void deleteAllTowns(){
        logger.warn("Deleting all towns.");
        townRepository.deleteAllTowns();
    }
}
