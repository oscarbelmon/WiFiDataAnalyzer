package data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by oscar on 27/09/15.
 */
public class MeasuresReader {
    private String[] rooms;

    public static Measures fromFile(MetaData metaData, String[] rooms) {
        final String fileName = metaData.getTrainingDataFile();
        List<Measure> measures = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(fileName)));
            measures = parse(content, metaData, rooms);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Measures(measures);
    }

    private static List<Measure> parse(String data, MetaData metaData, String[] rooms) {
        List<Measure> measures = new ArrayList<>();

        String[] stringMeasures = data.split("\n");
        for(String measure: stringMeasures) {
            measures.add(parseLine(measure, metaData, rooms));
        }
        return measures;
    }

    private static Measure parseLine(String measure, MetaData metaData, String[] rooms) {
        int numberOfMacs = metaData.getNumberOfMacs();
        String data[] = measure.split(" ");
        List<Reading> readings = new ArrayList<>();
        String room = rooms[Integer.parseInt(data[numberOfMacs])];
        RelativePosition relativePosition = RelativePosition.values()[Integer.parseInt(data[numberOfMacs+1])];
        Date timeStap = new Date(Integer.parseInt(data[numberOfMacs+2]));
        for(int i = 0; i < numberOfMacs; i++) {
            readings.add(new Reading(Integer.parseInt(data[i]), metaData.getWAPByIndex(i), room, relativePosition, timeStap));
        }
        return new Measure(new Readings(readings), room, relativePosition, timeStap);
    }
}
