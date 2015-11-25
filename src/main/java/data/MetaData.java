package data;

import java.util.List;

/**
 * MetaData
 *
 * Almacena los datos del fichero de casos de uso (entrenamiento).
 */
public class MetaData {
    private String trainingDataFile;
    private String validationDataFile;
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

    public String getTrainingDataFile() {
        return trainingDataFile;
    }

    public String getValidationDataFile() {
        return validationDataFile;
    }

    public WAP getWAPByIndex(int index) {
        return WAPs.get(index);
    }

    @Override
    public String toString() {
        return "MetaData{" +
                "trainingDataFile='" + trainingDataFile + '\'' +
                ", validationDataFile='" + validationDataFile + '\'' +
                ", trainingSamples=" + trainingSamples +
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
