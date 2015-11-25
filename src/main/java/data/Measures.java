package data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mesasures
 *
 * Contenedor de Measure que opera con todas las mediciones a la vez.
 */
public class Measures {
    private List<Measure> measures;

    Measures(List<Measure> measures) {
        this.measures = measures;
    }

    public int getNumberMeasures() {
        return measures.size();
    }

    public Measures getMeasuresByRoom(String room) {
        return new Measures(measures.stream()
            .filter(m -> m.getRoom().equals(room))
            .collect(Collectors.toList()));
    }

    public Measures getMeasuresByRoomAndRelativePosition(String room, RelativePosition relativePosition) {
        return new Measures(measures.stream()
            .filter(m -> m.getRoom().equals(room))
            .filter(m -> m.getRelativePosition() == relativePosition)
            .collect(Collectors.toList()));
    }

    public Readings getVisibleReadings() {
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
