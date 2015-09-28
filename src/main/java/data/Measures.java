package data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by oscar on 27/09/15.
 */
public class Measures {
    private List<Measure> measures;

    Measures(List<Measure> measures) {
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

    public Readings getVisibleMeasures() {
        return new Readings(measures.stream()
                .map(m -> m.getVisibleReadings().getReadings())
                .flatMap(r -> r.stream())
                .filter(r -> r.getIntensity() != Reading.NO_VISIBLE)
                .collect(Collectors.toList()));
    }

    public long getNumberVisibleReadings() {
        return measures.stream()
                .mapToInt(m -> m.getVisibleReadings().getReadings().size())
                .sum();
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
