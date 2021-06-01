package cz.tul;


import cz.tul.controller.AppController;
import cz.tul.data.Country;
import cz.tul.data.Measurement;
import cz.tul.data.Town;
import cz.tul.parser.CSVParser;
import cz.tul.parser.JsonParser;
import cz.tul.service.CountryService;
import cz.tul.service.MeasurementService;
import cz.tul.service.TownService;
import cz.tul.thread.UpdateThread;
import cz.tul.uploaddownload.UploadDownload;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import org.slf4j.Logger;

@SpringBootApplication(scanBasePackages = ("cz.tul"))
@EnableJpaRepositories("cz.tul.repositories")
@EnableMongoRepositories("cz.tul.repositories")
public class Main {

    @Bean
    public CountryService countryService(){
        return new CountryService();
    }

    @Bean
    public MeasurementService measurementService(){return new MeasurementService();}

    @Bean
    public UploadDownload uploadDownload(){return new UploadDownload();}

    @Bean
    public CSVParser csvParser(){return new CSVParser();}

    @Bean
    public JsonParser jsonParser(){return new JsonParser();}

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args){
        SpringApplication app = new SpringApplication(Main.class);
        ApplicationContext context = app.run(args);
        MeasurementService measurementService = context.getBean(MeasurementService.class);
        measurementService.createExpirationIndexIfNotExists();
        boolean readonly = Boolean.parseBoolean(context.getEnvironment().getProperty("readonly"));
        if(!readonly){
            logger.info("Starting update thread.");
            UpdateThread updateThread = new UpdateThread(context.getBean(TownService.class),context.getBean(MeasurementService.class));
            updateThread.start();
        }
    }
}
