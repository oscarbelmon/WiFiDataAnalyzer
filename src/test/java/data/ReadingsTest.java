package data;

/**
 * Created by oscar on 28/9/15.
 */

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;
/**
public class ReadingsTest {
    private static String metaDataFile = "src/main/resources/meta_data.json";
    private static MetaData metaData;
    private static Measures measures;
    private static long totalNumberReadings = 132035L;
    private static Readings readings;

    @BeforeClass
    public static void init() {
        metaData = MetaDataReader.fromFile(metaDataFile);
        measures = MeasuresReader.fromFile(metaData);
        readings = measures.getVisibleReadings();
    }


    @Test
    public void getVisibleReadingTest ()  {
        assertThat(readings.getNumberOfReadings(), is(totalNumberReadings));
    }

    @Test
    public void getVisibleReadingsByRoom() throws Exception {
        long accumulatedNumberReadings = 0;
        //TODO: Apaño para que ejecute
        House house = new House();
        for(String room: house.getRoomsNames()) {
            accumulatedNumberReadings += readings.getVisibleReadingByRoom(room).getNumberOfReadings();
        }
        assertThat(accumulatedNumberReadings, is(totalNumberReadings));
    }

    @Test
    public void getVisibleReadingsByWAP() throws Exception {
        long accumulatedNumberReadings = 0;
        for(int i = 0; i < metaData.getNumberOfMacs(); i++) {
            accumulatedNumberReadings += readings.getVisibleReadingsByWAP(metaData.getWAPByIndex(i)).getNumberOfReadings();
        }
        assertThat(accumulatedNumberReadings, is(totalNumberReadings));
    }
}
**/