package data;

import java.util.Date;

/**
 * Measure
 *
 * Contiene los datos relativos a una medición, que incluyen:
 *
 * - Lecturas de esa medición (datos medidos) -> Objeto Readings
 * - Habitación en la que se ha realizado.
 * - Posición relativa dentro de esa habitación.
 * - Fecha en la que se realizó.
 *
 * Incluye getters para obtener la información.
 */
public class Measure {
    private Readings readings;
    private String room;
    private RelativePosition relativePosition;
    private Date timeStamp;

    public Measure(Readings readings, String romm, RelativePosition relativePosition, Date timeStamp) {
        this.readings = readings;
        this.room = romm;
        this.relativePosition = relativePosition;
        this.timeStamp = timeStamp;
    }

    public String getRoom() {
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