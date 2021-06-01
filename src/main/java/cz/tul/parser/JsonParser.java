package cz.tul.parser;

import cz.tul.data.Measurement;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class JsonParser {

    private static final Logger logger = LoggerFactory.getLogger(JsonParser.class);

    public ArrayList<Measurement> getMeasurementsFromJson(StringBuilder string){
        try {
            ArrayList<Measurement> measurements = new ArrayList<>();
            JSONObject json = new JSONObject(string.toString());
            JSONArray array = json.getJSONArray("list");
            for (int i = 0; i < array.length(); i++) {
                JSONObject result = (JSONObject) array.get(i);
                JSONObject sys = result.getJSONObject("sys");
                String countryCode = sys.getString("country");
                String townName = result.getString("name");
                Integer townId = result.getInt("id");
                Integer ts = result.getInt("dt");
                JSONArray weatherArray = result.getJSONArray("weather");
                JSONObject weather = (JSONObject) weatherArray.get(0);
                String description = weather.getString("description");
                String main = weather.getString("main");
                JSONObject values = result.getJSONObject("main");
                Double temperature = values.getDouble("temp");
                Double minTemperature = values.getDouble("temp_min");
                Double maxTemperature = values.getDouble("temp_max");
                Double feelsLike = values.getDouble("feels_like");
                Integer humidity = values.getInt("humidity");
                Integer pressure = values.getInt("pressure");
                JSONObject wind = result.getJSONObject("wind");
                Integer windDegree = wind.getInt("deg");
                Integer windSpeed = wind.getInt("speed");
                Measurement measurement = new Measurement(townId,ts.longValue(),main,description,temperature,feelsLike,maxTemperature,minTemperature,pressure,humidity,windSpeed,windDegree,townName,countryCode);
                measurements.add(measurement);
            }
            logger.info(measurements.size() + " measurements parsed from input string.");
            return measurements;
        }catch (Exception e){
            logger.error("Unexpected format of input.");
        }
      return null;
    }
    //TODO testy
}
