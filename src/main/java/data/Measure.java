package data;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by oscar on 26/09/15.
 */
public class Measure {
    public static int NO_MEASURE = 100;
    private List<Reading> readings;
    private Room room;
    private RelativePosition relativePosition;
    private Date timeStamp;

    public Measure(List<Reading> readings, Room romm, RelativePosition relativePosition, Date timeStamp) {
        this.readings = readings;
        this.room = romm;
        this.relativePosition = relativePosition;
        this.timeStamp = timeStamp;
    }

    public Room getRoom() {
        return room;
    }

    public boolean getAnyReadingGreaterOrEqualTo(int reading) {
        return readings
                .stream()
                .filter(r -> r.getIntensity() >= reading && r.getIntensity() < NO_MEASURE)
                .count() > 0;
    }

    public List<Reading> getVisibleReadings() {
        return readings.stream()
                .filter(r -> r.getIntensity() != NO_MEASURE)
                .collect(Collectors.toList());
    }

    public long getNumberVisibleReadings() {
        return getVisibleReadings().size();
    }

    @Override
    public String toString() {
        return "{" +
                "readings=" + readings +
                ", room=" + room +
                ", relativePosition=" + relativePosition +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
