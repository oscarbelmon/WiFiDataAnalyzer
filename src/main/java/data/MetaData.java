package data;

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

    public int getNumberOfMacs() {
        return numberOfMacs;
    }

    public WAP getWAPByIndex(int index) {
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
