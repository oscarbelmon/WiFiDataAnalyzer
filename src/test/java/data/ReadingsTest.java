package data;

/**
 * Created by oscar on 28/9/15.
 */

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

public class ReadingsTest {
    private static String metaDataFile = "src/test/resources/meta_data.json";
    private static String dataFile = "src/test/resources/sensorstrainingData_belmonte.txt";
    private static MetaData metaData;
    private static Measures measures;

    @BeforeClass
    public static void init() {
        metaData = MetaDataReader.fromFile(metaDataFile);
        measures = MeasuresReader.fromFile(dataFile, metaData);
    }


    @Test
    public void getVisibleReadingTest ()  {
        Readings readings = measures.getVisibleReadings();
        long totalNumberReadings = 132035L;
        assertThat(readings.getNumberOfReadings(), is(totalNumberReadings));
    }

    @Test
    public void getVisibleReadingsByRoom() throws Exception {
        Readings readings = measures.getVisibleReadings();
        long totalNumberReadings = 132035L;
        long accumulatedNumberReadings = 0;
        for(Room room: Room.values()) {
            accumulatedNumberReadings += readings.getVisibleReadingByRoom(room).getNumberOfReadings();
        }
        assertThat(accumulatedNumberReadings, is(totalNumberReadings));
    }

    @Test
    public void getVisibleReadingsByWAP() throws Exception {
        Readings readings = measures.getVisibleReadings();
        long totalNumberReadings = 132035L;
        long accumulatedNumberReadings = 0;
        for(int i = 0; i < metaData.getNumberOfMacs(); i++) {
            accumulatedNumberReadings += readings.getVisibleReadingsByWAP(metaData.getWAPByIndex(i)).getNumberOfReadings();
        }

        assertThat(accumulatedNumberReadings, is(totalNumberReadings));
    }
}
