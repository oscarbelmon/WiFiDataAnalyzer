package data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by oscar on 27/09/15.
 */
public class Readings {
    private List<Reading> readings;
    private Readings visibleReadings = null;
    private Map<WAP, Readings> wapReadings = new HashMap<>();
    private Map<String, Readings> roomReadings = new HashMap<>();

    public Readings(List<Reading> readings) {
        this.readings = readings;
    }

    public List<Reading> getReadings() {
        return readings;
    }

    public boolean getReadingsGreaterOrEqualTo (int intensity) {
        return readings.stream()
                .filter(r -> r.getIntensity() >= intensity && r.getIntensity() != Reading.NO_VISIBLE)
                .count() > 0;
    }

    public Readings getVisibleReadings() {
        if(visibleReadings == null)
            visibleReadings = new Readings(readings.stream()
                .filter(r -> r.getIntensity() != Reading.NO_VISIBLE)
                .collect(Collectors.toList()));
        return visibleReadings;
    }

    public long getNumberOfReadings() {
        return readings.size();
    }

    public Readings getVisibleReadingByRoom(String room) {
        return new Readings(getVisibleReadings().getReadings().stream()
                .filter(r -> r.getRoom().equals(room))
                .collect(Collectors.toList()));
    }

    private void createEntry(String room) {
        Readings readings = new Readings(getVisibleReadings().getReadings().stream()
                .filter(r -> r.getRoom().equals(room))
                .collect(Collectors.toList()));
        roomReadings.put(room, readings);
    }

    public Readings getVisibleReadingsByWAP(WAP wap) {
        if(wapReadings.containsKey(wap) == false)
            createEntry(wap);
        return wapReadings.get(wap);
    }

    private void createEntry(WAP wap) {
        Readings readings = new Readings(getVisibleReadings().getReadings().stream()
                .filter(r -> r.getWap() == wap)
                .collect(Collectors.toList()));
        wapReadings.put(wap, readings);
    }

    public Readings getReadingsGreeterOrEqualTo(int intensity) {
        return new Readings(getVisibleReadings().getReadings().stream()
                .filter(m -> m.getIntensity() > intensity)
                .collect(Collectors.toList()));
    }

}
