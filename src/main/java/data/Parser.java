package data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by oscar on 26/09/15.
 */
public class Parser {
//    private MetaData metaData;
    private int numberOfMacs;

    public Parser(MetaData metaData) {
        super();
        numberOfMacs = metaData.getNumberOfMacs();
    }

    public List<Measure> parse(String data) {
        List<Measure> measures = new ArrayList<>();

        String[] stringMeasures = data.split("\n");
        for(String measure: stringMeasures) {
            measures.add(parseLine(measure));
        }
        return measures;
    }

    private Measure parseLine(String measure) {
        String data[] = measure.split(" ");
        int[] readings = new int[numberOfMacs];
        for(int i = 0; i < numberOfMacs; i++) {
            readings[i] = Integer.parseInt(data[i]);
        }
        Room room = Room.values()[Integer.parseInt(data[numberOfMacs])];
        RelativePosition relativePosition = RelativePosition.values()[Integer.parseInt(data[numberOfMacs+1])];
        Date timeStap = new Date(Integer.parseInt(data[numberOfMacs+2]));
        return new Measure(readings, room, relativePosition, timeStap);
    }
}
