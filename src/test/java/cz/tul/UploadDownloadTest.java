package cz.tul;


import cz.tul.data.Measurement;
import cz.tul.parser.CSVParser;
import cz.tul.uploaddownload.UploadDownload;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Main.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UploadDownloadTest {

    @Autowired
    private UploadDownload uploadDownload;

    @Autowired
    private CSVParser csvParser;

    private Measurement measurement1 = new Measurement(3067696,1622050369L,"Clouds","overcast clouds",11.97,11.34,12.28,10.66,1014,81,5,221,"prague","CZ");
    private Measurement measurement2 = new Measurement(3067696,1622050370L,"Clouds","overcast clouds",5.97,11.34,12.28,10.66,1014,81,5,221,"prague","CZ");


    @Test
    public void downloadTest(){
        List<Measurement> measurements = new ArrayList<>();
        measurements.add(measurement1);
        measurements.add(measurement2);
        ResponseEntity<Object> responseEntity = uploadDownload.downloadMeasurements("C:\\csvDownload/test.csv",csvParser.createCSV(measurements));
        assertEquals("Resource should be loaded","InputStream resource [resource loaded through InputStream]",responseEntity.getBody().toString());
    }

    @Test
    public void uploadTest(){
        List<Measurement> measurements = new ArrayList<>();
        measurements.add(measurement1);
        measurements.add(measurement2);
        File file = new File("C:\\csvDownload/test.csv");
        try{
            FileWriter writer = new FileWriter(file);
            writer.write(csvParser.createCSV(measurements).toString());
            writer.flush();
            writer.close();
            InputStream inputStream = new FileInputStream("C:\\csvDownload/test.csv");
            MultipartFile multipartFile = new MockMultipartFile("test.csv",inputStream);
            List<Measurement> measurements1 = uploadDownload.upload(multipartFile);
            assertEquals("Two measurements should have been retrieved",2,measurements1.size());
        }catch (Exception e){
        }
    }
}
