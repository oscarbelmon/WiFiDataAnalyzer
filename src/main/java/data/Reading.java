package data;

import java.util.Date;

/**
 * Reading
 *
 * Lectura individual. Una medición se compone de muchas lecturas.
 * Cada lectura individual obtiene una intensidad.
 * Se guardan los siguientes datos:
 *
 * - Intensidad.
 * - WAP.
 * - Habitación.
 * - Posición relativa dentro de la habitación.
 * - Fecha en la que fue tomada.
 *
 * Hay métodos para obtener estos datos.
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
