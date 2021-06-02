package cz.tul;

import com.fasterxml.jackson.databind.JsonSerializer;
import cz.tul.data.Measurement;
import cz.tul.parser.JsonParser;
import cz.tul.service.MeasurementService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MeasurementTest {
    @Autowired
    private MeasurementService measurementService;

    private Measurement measurement1 = new Measurement(3067696,1622050369L,"Clouds","overcast clouds",11.97,11.34,12.28,10.66,1014,81,5,221,"prague","CZ");
    private Measurement measurement2 = new Measurement(3067696,1622050370L,"Clouds","overcast clouds",5.97,11.34,12.28,10.66,1014,81,5,221,"prague","CZ");
    private Measurement measurement3 = new Measurement(3067696,1622050367L,"Clouds","overcast clouds",3.97,11.34,12.28,10.66,1014,81,5,221,"prague","CZ");
    private Measurement measurement4 = new Measurement(3067696,1621550366L,"Clouds","overcast clouds",8.97,11.34,12.28,10.66,1014,81,5,221,"prague","CZ");
    private Measurement measurement5 = new Measurement(3067696,1621050365L,"Clouds","overcast clouds",20.97,11.34,12.28,10.66,1014,81,5,221,"prague","CZ");
    private Measurement measurement6 = new Measurement(3078610,1622050364L,"Clouds","overcast clouds",7.97,11.34,12.28,10.66,1014,81,5,221,"bratislava","SK");
    private Measurement measurement7 = new Measurement(3078610,1622050363L,"Clouds","overcast clouds",9.97,11.34,12.28,10.66,1014,81,5,221,"bratislava","SK");

    @Before
    public void init(){
        measurementService.deleteAllMeasurements();
    }

    @After
    public void clearDatabase(){
        measurementService.deleteAllMeasurements();
    }

    @Test
    public void testSaveGetDelete(){
        List<Measurement> measurements = new ArrayList<>();
        measurements.add(measurement1);
        measurements.add(measurement2);
        measurements.add(measurement3);
        measurements.add(measurement4);
        measurements.add(measurement5);
        measurements.add(measurement6);
        measurements.add(measurement7);

        measurementService.saveAllMeasurements(measurements);

        List<Measurement> measurements1 = measurementService.getAllMeasurements();
        assertEquals("Seven measurements should have been saved and retrieved",7,measurements1.size());
        List<Measurement> measurements2 = measurementService.getAllMeasurementsOfCountry("CZ");
        assertEquals("Five measurements should have been retrieved",5,measurements2.size());
        List<Measurement> measurements3 = measurementService.getAllMeasurementsOfTown(measurement6.getTownId());
        assertEquals("Two measurements should have been retrieved",2,measurements3.size());

        measurementService.deleteAllMeasurementsOfTown(measurement1.getTownId());
        List<Measurement> measurements4 = measurementService.getAllMeasurements();
        assertEquals("Two measurements should have been retrieved",2,measurements4.size());

        measurementService.saveMeasurement(measurement1);

        measurementService.deleteAllMeasurementsOfCountry("SK");
        List<Measurement> measurements5 = measurementService.getAllMeasurements();
        assertEquals("One measurement should have been retrieved",1,measurements5.size());

    }

    @Test
    public void testCalculateAverage(){
        List<Measurement> measurements = new ArrayList<>();
        measurements.add(measurement1);
        measurements.add(measurement2);
        measurements.add(measurement3);
        measurements.add(measurement4);
        measurements.add(measurement5);
        measurements.add(measurement6);
        measurements.add(measurement7);
        measurementService.saveAllMeasurements(measurements);
        double oneDayAverage = measurementService.oneDayAverage(measurement1.getTs(),measurement1.getTownId());
        double oneWeekAverage = measurementService.oneWeekAverage(measurement1.getTs(),measurement1.getTownId());
        double twoWeeksAverage = measurementService.twoWeeksAverage(measurement1.getTs(),measurement1.getTownId());
        assertEquals(7.3,oneDayAverage,0.01);
        assertEquals(7.72,oneWeekAverage,0.01);
        assertEquals(10.37,twoWeeksAverage,0.01);
    }

    @Test
    public void testChangeTownNameOfMeasurement(){
        measurementService.saveMeasurement(measurement1);
        measurementService.updateTownNameOfMeasurement("Praha",measurement1.getTownId());
        List<Measurement> measurements = measurementService.getAllMeasurementsOfTown(measurement1.getTownId());
        assertEquals("Name of measurements should be changed","Praha",measurements.get(0).getTownName());
    }

    @Test
    public void testExpirationTime(){
        Long time = new Date().getTime()/1000;
        Measurement measurement = new Measurement(3067696,time,"Clouds","overcast clouds",11.97,11.34,12.28,10.66,1014,81,5,221,"Prague","CZ");
        measurementService.createExpirationIndexIfNotExists(60);
        measurementService.saveMeasurement(measurement);
        List<Measurement> measurements = measurementService.getAllMeasurements();
        assertEquals("One measurement should have been saved and retrieved",1,measurements.size());
        try {
            Thread.sleep(120000);
        }catch (Exception e){

        }
        List<Measurement> measurements1 = measurementService.getAllMeasurements();
        assertEquals("One measurement should have expired",0,measurements1.size());
        measurementService.changeExpirationTime(1209600);
    }
}
