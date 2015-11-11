package data;

import java.util.Date;

/**
 * Created by oscar on 27/09/15.
 */
public class Reading {
    public static int NO_VISIBLE = 100;
    private int intensity;
    private WAP wap;
    private String room;
    private RelativePosition relativePosition;
    private Date date;

    public Reading(int intensity, WAP wap, String room, RelativePosition relativePosition, Date date) {
        this.intensity = intensity;
        this.wap = wap;
        this.room = room;
        this.relativePosition = relativePosition;
        this.date = date;
    }

    public int getIntensity() {
        return intensity;
    }

    public WAP getWap() {
        return wap;
    }

    public String getRoom() {
        return room;
    }

    public RelativePosition getRelativePosition() {
        return relativePosition;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Reading{" +
                "intensity=" + intensity +
                ", wap=" + wap +
                ", room=" + room +
                ", relativePosition=" + relativePosition +
                ", date=" + date +
                '}';
    }
}
