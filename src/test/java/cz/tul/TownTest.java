package cz.tul;


import cz.tul.data.Country;
import cz.tul.data.Town;
import cz.tul.service.CountryService;
import cz.tul.service.TownService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Main.class})
public class TownTest {

    @Autowired
    private CountryService countryService;

    @Autowired
    private TownService townService;

    private static Country country1 = new Country("CZ","Czech Republic");
    private static Country country2 = new Country("SK","Slovakia");

    private Town town1 = new Town(1,"Prague",country1);
    private Town town2 = new Town(2,"Brno",country1);
    private Town town3 = new Town(3,"Liberec",country1);

    private Town town4 = new Town(4,"Bratislava",country2);
    private Town town5 = new Town(5,"Kosice",country2);
    private Town town6 = new Town(6,"Trencín",country2);

    public void init(){
        countryService.deleteAllCountries();
    }

    @Test
    public void testSaveGetAndDelete(){
        countryService.saveOrUpdate(country1);
        countryService.saveOrUpdate(country2);
        townService.saveOrUpdate(town1);
        townService.saveOrUpdate(town2);
        townService.saveOrUpdate(town3);
        townService.saveOrUpdate(town4);
        townService.saveOrUpdate(town5);
        townService.saveOrUpdate(town6);

        List<Town> towns = townService.getTowns();
        assertEquals("Six towns should have been saved a retrieved.",6,towns.size());
        List<Town> towns1 = townService.getTownsByCountryCode(country1.getCode());
        assertEquals("Three towns should have been retrieved.",3,towns1.size());
        townService.deleteByCountryCode(country2.getCode());
        List<Town> towns2 = townService.getTowns();
        assertEquals("Three towns should have been retrieved.",3,towns2.size());
        townService.deleteTownById(town1.getId());
        assertEquals("Country should not exist.",false,townService.exists(town1.getId()));
        townService.deleteAllTowns();
        List<Town> towns3 = townService.getTowns();
        assertEquals("All towns should be deleted",0,towns3.size());

    }

    @Test
    public void testUpdate(){
        countryService.saveOrUpdate(country1);
        townService.saveOrUpdate(town1);
        town1.setName("Praha");
        townService.saveOrUpdate(town1);
        List<Town> towns = townService.getTowns();
        assertEquals("Name of country should have been changed.",town1.getName(),towns.get(0).getName());
        town1.setName("Prague");
    }

    @Test
    public void testExists(){
        countryService.saveOrUpdate(country1);
        townService.saveOrUpdate(town1);
        assertEquals("Country should exist.",true,townService.exists(town1.getId()));
    }

}
