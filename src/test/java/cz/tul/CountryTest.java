package cz.tul;

import cz.tul.data.Country;
import cz.tul.data.Town;
import cz.tul.service.CountryService;
import cz.tul.service.TownService;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Main.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CountryTest {

    @Autowired
    private CountryService countryService;

    private Country country1 = new Country("CZ","Czech Republic");
    private Country country2 = new Country("SK","Slovakia");


    @Before
    public void init() {
        countryService.deleteAllCountries();
    }


    @Test
    public void testSaveGetAndDelete(){
        countryService.saveOrUpdate(country1);
        countryService.saveOrUpdate(country2);
        List<Country> countries1 = countryService.getAllCountries();
        assertEquals("Two countries should have been saved and retrieved",2,countries1.size());
        countryService.deleteCountryByCode(country1.getCode());
        List<Country> countries2 = countryService.getAllCountries();
        assertEquals("One country should have been deleted and retrieved",1,countries2.size());
        countryService.deleteAllCountries();
        List<Country> countries3 = countryService.getAllCountries();
        assertEquals("All countries should have been deleted.",0,countries3.size());
    }


    @Test
    public void testUpdate(){
        countryService.saveOrUpdate(country1);
        country1.setCountryName("Czechia");
        countryService.saveOrUpdate(country1);
        List<Country> countries = countryService.getAllCountries();
        assertEquals("Name of country should have been changed.",country1.getCountryName(),countries.get(0).getCountryName());
        country1.setCountryName("Czech Republic");
    }

    @Test
    public void testExists(){
        countryService.saveOrUpdate(country1);
        assertEquals("Country should exist.",true,countryService.exists(country1.getCode()));
    }

}
