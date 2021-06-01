package cz.tul.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@Document(collection = "measurement")
public class Measurement {

    public Integer getTownId() {
        return townId;
    }

    public void setTownId(Integer townId) {
        this.townId = townId;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(Double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public Double getMaximalTemperature() {
        return maximalTemperature;
    }

    public void setMaximalTemperature(Double maximalTemperature) {
        this.maximalTemperature = maximalTemperature;
    }

    public Double getMinimalTemperature() {
        return minimalTemperature;
    }

    public void setMinimalTemperature(Double minimalTemperature) {
        this.minimalTemperature = minimalTemperature;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Integer getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Integer windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Integer getWindDegree() {
        return windDegree;
    }

    public void setWindDegree(Integer windDegree) {
        this.windDegree = windDegree;
    }

    public Measurement(){

    }
    public Measurement(Integer townId, Long ts, String main, String description, Double temperature, Double feelsLike, Double maximalTemperature, Double minimalTemperature, Integer pressure, Integer humidity, Integer windSpeed, Integer windDegree,String townName, String countryCode) {
        this.townId = townId;
        this.ts = ts;
        this.main = main;
        this.description = description;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.maximalTemperature = maximalTemperature;
        this.minimalTemperature = minimalTemperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windDegree = windDegree;
        this.countryCode = countryCode;
        this.townName = townName;
        this.id = createMeasurementId(townId,ts);
        this.createdAt = new Date(ts*1000);
    }

    @Id
    Long id;

    Date createdAt;

    Integer townId;
    Long ts;
    String main;
    String description;
    String townName;
    String countryCode;
    Double temperature;
    Double feelsLike;
    Double maximalTemperature;
    Double minimalTemperature;
    Integer pressure;
    Integer humidity;
    Integer windSpeed;
    Integer windDegree;

    public static Long createMeasurementId(int townID,long timeStamp){
        return timeStamp*100000000 + townID;
    }

    @Override
    public String toString() {
        return "cz.tul.data.Measurement,"+id+","+countryCode+","+createdAt+","+description+","+feelsLike+","+humidity+","+main+","+maximalTemperature+","+minimalTemperature+","+pressure+","+temperature+","+townId+","+townName+","+ts+","+windDegree+","+windSpeed;
    }


}
