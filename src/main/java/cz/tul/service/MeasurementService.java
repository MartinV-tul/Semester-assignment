package cz.tul.service;

import cz.tul.data.Measurement;
import cz.tul.repositories.MeasurementRepository;
import lombok.Synchronized;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexInfo;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import java.util.ArrayList;
import java.util.List;

public class MeasurementService {

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final Logger logger = LoggerFactory.getLogger(MeasurementService.class);

    public List<Measurement> getAllMeasurementsOfTown(int townId){
        logger.info("Loading all measurements of town: townId="+townId+".");
        return measurementRepository.findAllByTownIdOrderByTsDesc(townId);
    }

    public List<Measurement> getAllMeasurementsOfCountry(String code){
        logger.info("Loading all measurements of country: code="+code+".");
        return measurementRepository.findAllByCountryCodeOrderByTsDesc(code);
    }

    public Measurement getLastMeasurementOfTown(int townId){
        logger.info("Loading last measurement of town: townId="+townId+".");
        return  measurementRepository.findFirstByTownIdOrderByTsDesc(townId);
    }

    public void createExpirationIndexIfNotExists(int expirationTime){
        if(expirationTime<60) {
            logger.warn("Expiration time lower then 1 minute is not allowed. Expiration time set to 60s.");
            expirationTime=60;
        }
        List<IndexInfo> s = mongoTemplate.indexOps(Measurement.class).getIndexInfo();
        for (IndexInfo info:s) {
            if(info.getName().equals("createdAt_1")) changeExpirationTime(expirationTime);
        }
        createExpirationIndex(expirationTime);
    }

    public void changeExpirationTime(int newTimeOfExpiration){
        mongoTemplate.indexOps(Measurement.class).dropIndex("createdAt_1");
        createExpirationIndex(newTimeOfExpiration);
    }

    private void createExpirationIndex(int expirationTime){
        logger.warn("New expiration index will be created.");
        mongoTemplate
                .indexOps(Measurement.class)
                .ensureIndex(new Index().on("createdAt", Sort.Direction.ASC).expire(expirationTime));
    }

    public void saveMeasurement(Measurement measurement){
        logger.info("Saving one measurement.");
        measurementRepository.save(measurement);
    }

    public void saveAllMeasurements(List<Measurement> measurements){
        logger.info("Saving measurements.");
        measurementRepository.saveAll(measurements);
    }

    public void deleteAllMeasurements(){
        logger.warn("Deleting all measurements.");
        measurementRepository.deleteAll();
    }

    public void deleteAllMeasurementsOfTown(int townId){
        logger.warn("Deleting measurements of town: townId="+townId+".");
        measurementRepository.deleteAllByTownId(townId);
    }

    public void deleteAllMeasurementsOfCountry(String code){
        logger.warn("Deleting measurements of country: code="+code+".");
        measurementRepository.deleteAllByCountryCode(code);
    }

    public void deleteMeasurement(Measurement measurement){
        logger.warn("Deleting one measurement.");
        measurementRepository.delete(measurement);
    }

    public void updateTownNameOfMeasurement(String townName,int townId){
        logger.warn("Changing name of town: townId="+townId+" to "+townName+".");
        mongoTemplate.updateMulti(new Query(Criteria.where("townId").is(townId)),Update.update("townName",townName),"measurement");
    }

    public List<Measurement> getAllMeasurements(){
        logger.info("Loading all measurements.");
        return measurementRepository.findAll();
    }

    public double oneDayAverage(Long currentTime,Integer townId ){
        return averageOverTime(currentTime - 86400,townId);
    }

    public double oneWeekAverage(Long currentTime,Integer townId ){
        return averageOverTime(currentTime - 604800,townId);
    }

    public double twoWeeksAverage(Long currentTime,Integer townId ){
        return averageOverTime(currentTime - 1209600,townId);
    }

    private double averageOverTime(Long endTime, Integer townId){
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(Criteria.where("townId").is(townId).and("ts").gte(endTime)),Aggregation.group().avg("temperature").as("average"));
        Document document = mongoTemplate.aggregate(aggregation,"measurement", String.class).getRawResults();
        Object[] values = document.values().toArray();
        ArrayList list = (ArrayList) values[0];
        if(list.size() == 0) return 0;
        Document document1 = (Document)list.get(0);
        return document1.getDouble("average");
    }
}
