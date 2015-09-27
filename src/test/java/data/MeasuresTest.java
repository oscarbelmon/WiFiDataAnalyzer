package data;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;
import static org.mockito.Mockito.*;

/**
 * Created by oscar on 27/09/15.
 */
public class MeasuresTest {

    @Test
    public void testGetNumberMeasures() throws Exception {
        String fileName = "src/test/resources/sensorstrainingData_belmonte.txt";
        MetaData mockMetaData = mock(MetaData.class);
        when(mockMetaData.getNumberOfMacs()).thenReturn(127);
        Measures measures = Measures.fromFile(fileName, mockMetaData);
        assertThat(measures.getNumberMeasures(), is(9414));
    }

    @Test
    public void testGetMeasuresByRoom() throws Exception {
        String fileName = "src/test/resources/sensorstrainingData_belmonte.txt";
        MetaData mockMetaData = mock(MetaData.class);
        when(mockMetaData.getNumberOfMacs()).thenReturn(127);
        Measures measures = Measures.fromFile(fileName, mockMetaData);
        assertThat(measures.getMeasuresByRoom(Room.BALCONY).getNumberMeasures(), is(530));
    }
}