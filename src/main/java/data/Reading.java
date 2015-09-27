package data;

/**
 * Created by oscar on 27/09/15.
 */
public class Reading {
    private int intensity;
    private WAP wap;

    public Reading(int intensity, WAP wap) {
        this.intensity = intensity;
        this.wap = wap;
    }

    public int getIntensity() {
        return intensity;
    }

    public WAP getWap() {
        return wap;
    }

    @Override
    public String toString() {
        return "{" +
                "intensity=" + intensity +
                ", wap='" + wap + '\'' +
                '}';
    }
}
