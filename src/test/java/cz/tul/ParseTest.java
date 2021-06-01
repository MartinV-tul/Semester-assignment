package cz.tul;

import cz.tul.data.Measurement;
import cz.tul.parser.CSVParser;
import cz.tul.parser.JsonParser;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Main.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParseTest {

    @Autowired
    private CSVParser csvParser;

    private Measurement measurement1 = new Measurement(3067696,1622050369L,"Clouds","overcast clouds",11.97,11.34,12.28,10.66,1014,81,5,221,"prague","CZ");
    private Measurement measurement2 = new Measurement(3067696,1622050370L,"Clouds","overcast clouds",5.97,11.34,12.28,10.66,1014,81,5,221,"prague","CZ");


    @Test
    public void testCSVParser(){
        List<Measurement> measurements = new ArrayList<>();
        measurements.add(measurement1);
        measurements.add(measurement2);
        File file = new File("C:\\csvDownload/test.csv");
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(csvParser.createCSV(measurements).toString());
            writer.flush();
            writer.close();
            FileReader reader = new FileReader("C:\\csvDownload/test.csv");
            BufferedReader bufferedReader = new BufferedReader(reader);
            List<Measurement> measurements1 = csvParser.createList(bufferedReader);
            assertEquals("Two measurements should have been retrieved",2,measurements1.size());
            assertEquals("Measurements in lists should be the same",measurements.get(0).getId(),measurements1.get(0).getId());
            assertEquals("Measurements in lists should be the same",measurements.get(1).getId(),measurements1.get(1).getId());
        }catch (Exception e){

        }
    }

}
