package cz.tul.uploaddownload;

import cz.tul.controller.AppController;
import cz.tul.data.Measurement;
import cz.tul.parser.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

public class UploadDownload {

    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    public ResponseEntity<Object> downloadMeasurements(String fileName, StringBuilder csv){
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName);
            writer.write(csv.toString());
            writer.flush();

            File file = new File(fileName);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition",String.format("attachment;filename=\"%s\"",file.getName()));
            headers.add("Cache-Control","no-cache, no-store, must-revalidate");
            headers.add("Pragma","no-cache");
            headers.add("Expires","0");
            ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("text/csv")).body(resource);
            return responseEntity;
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<>("ERROR OCCURRED", HttpStatus.OK);
        }
        finally {
            try {
                if(writer != null) writer.close();
            }
            catch (Exception e){
                logger.error(e.getMessage());
            }
        }
    }

    public List<Measurement> upload(MultipartFile file){
        try {
            InputStream inputStream = file.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            CSVParser csvParser = new CSVParser();
            List<Measurement> measurements = csvParser.createList(reader);
            return measurements;
        }catch (Exception e)
        {
            logger.error(e.getMessage());
            return null;
        }
    }
}
