package data;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by oscar on 28/9/15.
 */
public class WapReadings {
    private Measures measures;
    private Readings visibleReadings;
    private Map<WAP, Readings> wapMeasures = new HashMap<>();

    public WapReadings(Measures measures) {
        this.measures = measures;
        visibleReadings = measures.getVisibleReadings();
    }

    public Readings getVisibleReadingByRoom(String room) {
        return new Readings(visibleReadings.getReadings().stream()
                .filter(r -> r.getRoom().equals(room))
                .collect(Collectors.toList()));
    }

    public Readings getVisibleReadingsByWAP(WAP wap) {
        if(wapMeasures.containsKey(wap) == false)
            createEntry(wap);
        return wapMeasures.get(wap);
    }

    private void createEntry(WAP wap) {
        Readings readings = new Readings(visibleReadings.getReadings().stream()
                .filter(r -> r.getWap() == wap)
                .collect(Collectors.toList()));
        wapMeasures.put(wap, readings);
    }
}
