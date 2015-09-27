package data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by oscar on 27/09/15.
 */
public class Readings {
    private List<Reading> readings;

    public Readings(List<Reading> readings) {
        this.readings = readings;
    }

    public boolean getReadingsGreaterOrEqualTo (int intensity) {
        return readings.stream()
                .filter(r -> r.getIntensity() >= intensity && r.getIntensity() != Reading.NO_VISIBLE)
                .count() > 0;
    }

    public Readings getVisibleReadings() {
        return new Readings(readings.stream()
                .filter(r -> r.getIntensity() != Reading.NO_VISIBLE)
                .collect(Collectors.toList()));
    }

    public long getNumberOfReadings() {
        return readings.size();
    }
}
