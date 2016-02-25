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
    private static String metaDataFile = "/src/main/resources/meta_data.json";
    private static String[] rooms = {"BALCONY", "KITCHEN", "WC1", "WC2", "BEDROOM1", "BEDROOM2", "BEDROOM3", "DININGROOM", "CORRIDOR"};

    @BeforeClass
    public static void init() {
        metaData = MetaDataReader.fromFile(metaDataFile);
    }

    @Test
    public void testGetNumberMeasures() throws Exception {
        Measures measures = MeasuresReader.fromFile(metaData, rooms);
        assertThat(measures.getNumberMeasures(), is(9414));
    }

    @Test
    public void testGetMeasuresByRoom() throws Exception {
        Measures measures = MeasuresReader.fromFile(metaData, rooms);
        assertThat(measures.getMeasuresByRoom("BALCONY").getNumberMeasures(), is(530));
    }
}
