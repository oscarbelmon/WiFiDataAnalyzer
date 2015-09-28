package data;

import java.util.Date;

/**
 * Created by oscar on 26/09/15.
 */
public class Measure {
    private Readings readings;
    private Room room;
    private RelativePosition relativePosition;
    private Date timeStamp;

    public Measure(Readings readings, Room romm, RelativePosition relativePosition, Date timeStamp) {
        this.readings = readings;
        this.room = romm;
        this.relativePosition = relativePosition;
        this.timeStamp = timeStamp;
    }

    public Room getRoom() {
        return room;
    }

    public RelativePosition getRelativePosition() {
        return relativePosition;
    }

    public boolean getAnyReadingGreaterOrEqualTo(int intensity) {
        return readings.getReadingsGreaterOrEqualTo(intensity);
    }

    public Readings getVisibleReadings() {
        return readings.getVisibleReadings();
    }

    public long getNumberVisibleReadings() {
        return readings.getVisibleReadings().getNumberOfReadings();
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
