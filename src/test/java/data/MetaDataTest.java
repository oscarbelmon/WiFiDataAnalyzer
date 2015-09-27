package data;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

/**
 * Created by oscar on 27/09/15.
 */
public class MetaDataTest {
    @Test
    public void testGetNumberOfMacs() throws Exception {
        String fileName = "src/main/resources/meta_data.json";
        MetaData metaData = MetaDataReader.fromFile(fileName);
        assertThat(metaData.getNumberOfMacs(), is(127));
    }
}