package cz.tul.thread;

import cz.tul.data.Measurement;
import cz.tul.data.Town;
import cz.tul.parser.JsonParser;
import cz.tul.service.MeasurementService;
import cz.tul.service.TownService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class UpdateThread extends Thread{

    public static Integer updateTime = 5000;
    boolean updateEnabled = true;

    public UpdateThread(TownService townService,MeasurementService measurementService){
        this.measurementService = measurementService;
        this.townService = townService;
    }


    private final TownService townService;

    private final MeasurementService measurementService;

    public void run(){
        Long currentTime = System.currentTimeMillis();
        while (updateEnabled){
            if(System.currentTimeMillis()-currentTime>updateTime){
                currentTime = System.currentTimeMillis();
                List<Town> towns = townService.getTowns();
                if(towns==null || towns.size() == 0)continue;
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < towns.size() - 1; i++) {
                    stringBuilder.append(towns.get(i).getId());
                    stringBuilder.append(',');
                }
                stringBuilder.append(towns.get(towns.size()-1).getId());
                String urlString = "https://api.openweathermap.org/data/2.5/group?id=" + stringBuilder.toString()+ "&appid=ab9635a72d36ec4ce25bd37c0f200ba9&units=metric";
                try{
                    StringBuilder result = new StringBuilder();
                    URL url = new URL(urlString);
                    URLConnection conn = url.openConnection();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while((line = rd.readLine()) != null){
                        result.append(line);
                    }
                    rd.close();
                    JsonParser parser = new JsonParser();
                    ArrayList<Measurement> measurements = parser.getMeasurementsFromJson(result);
                    measurementService.saveAllMeasurements(measurements);
                }catch (Exception e){
                    updateEnabled = false;
                }
            }
        }
    }
}
