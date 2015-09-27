package data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by oscar on 27/09/15.
 */
public class Measures {
    private List<Measure> measures;

    public static Measures fromFile(String fileName, MetaData metaData) {
        List<Measure> measures = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(fileName)));
            Parser parser = new Parser(metaData);
            measures = parser.parse(content);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Measures(measures);
    }

    private Measures(List<Measure> measures) {
        this.measures = measures;
    }

    public int getNumberMeasures() {
        return measures.size();
    }

    public Measures getMeasuresByRoom(Room room) {
        return new Measures(measures.stream()
            .filter(m -> m.getRoom() == room)
            .collect(Collectors.toList()));
    }

    public Measures getMeasuresByRoomAndRelativePosition(Room room, RelativePosition relativePosition) {
        return new Measures(measures.stream()
            .filter(m -> m.getRoom() == room)
            .filter(m -> m.getRelativePosition() == relativePosition)
            .collect(Collectors.toList()));
    }

    public Measures getMeasuresReadingsGreeterOrEqualTo(int reading) {
        return new Measures(measures.stream()
                .filter(m -> m.getAnyReadingGreaterOrEqualTo(reading))
                .collect(Collectors.toList()));
    }

    public Measure getMeasureByIndex(int index) {
        // TODO Check index validity.
        return measures.get(index);
    }

    public double getMeanNumberVisibleWAPs() {
        return measures.stream()
                .mapToLong(Measure::getNumberVisibleReadings)
                .average()
                .getAsDouble();
    }

    @Override
    public String toString() {
        return "{" +
                "measures=" + measures +
                '}';
    }
}
