package data;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by oscar on 26/09/15.
 */
public class MetaData {
    private int trainingSamples;
    private int validationSamples;
    private int numberOfMacs;
    private int numberOfMacsTraining;
    private int numberOfMacsValidation;
    private Range rangeTraining;
    private Range rangeValidation;
    private List<WAP> WAPs;

    public static MetaData fromFile(String fileName) {
        MetaData metaData = new MetaData();
        Gson gson = new Gson();

        try {
            String content = new String(Files.readAllBytes(Paths.get(fileName)));
            metaData = gson.fromJson(new FileReader(fileName), MetaData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return metaData;
    }

    public int getNumberOfMacs() {
        return numberOfMacs;
    }

    WAP getWAPByIndex(int index) {
        return WAPs.get(index);
    }

    @Override
    public String toString() {
        return "MetaData{" +
                "trainingSamples=" + trainingSamples +
                ", validationSamples=" + validationSamples +
                ", numberOfMacs=" + numberOfMacs +
                ", numberOfMacsTraining=" + numberOfMacsTraining +
                ", numberOfMacsValidation=" + numberOfMacsValidation +
                ", rangeTraining=" + rangeTraining +
                ", rangeValidation=" + rangeValidation +
                ", WAPs=" + WAPs +
                '}';
    }
}
