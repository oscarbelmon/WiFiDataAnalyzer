package data;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by oscar on 27/09/15.
 */
public class MeasuresTest {
    private static MetaData metaData;
    private static String metaDataFile = "src/test/resources/meta_data.json";

    @BeforeClass
    public static void init() {
        metaData = MetaData.fromFile(metaDataFile);
    }

    @Test
    public void testGetNumberMeasures() throws Exception {
        String dataFile = "src/test/resources/sensorstrainingData_belmonte.txt";
        Measures measures = Measures.fromFile(dataFile, metaData);
        assertThat(measures.getNumberMeasures(), is(9414));
    }

    @Test
    public void testGetMeasuresByRoom() throws Exception {
        String dataFile = "src/test/resources/sensorstrainingData_belmonte.txt";
        Measures measures = Measures.fromFile(dataFile, metaData);
        assertThat(measures.getMeasuresByRoom(Room.BALCONY).getNumberMeasures(), is(530));
    }
}