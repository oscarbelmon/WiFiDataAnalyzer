package data;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by oscar on 26/09/15.
 */
public class Measure {
    private int[] readings;
    private Room room;
    private RelativePosition relativePosition;
    private Date timeStamp;

    public Measure(int[] readings, Room romm, RelativePosition relativePosition, Date timeStamp) {
        this.readings = readings;
        this.room = romm;
        this.relativePosition = relativePosition;
        this.timeStamp = timeStamp;
    }

    public Room getRoom() {
        return room;
    }

    @Override
    public String toString() {
        return "Measure{" +
                "readings=" + Arrays.toString(readings) +
                ", room=" + room +
                ", relativePosition=" + relativePosition +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
