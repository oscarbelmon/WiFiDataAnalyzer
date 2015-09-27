package data;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by oscar on 27/09/15.
 */
public class MetaDataReader {
    public static MetaData fromFile(String fileName) {
        MetaData metaData = new MetaData();
        Gson gson = new Gson();

        try {
            String content = new String(Files.readAllBytes(Paths.get(fileName)));
            metaData = gson.fromJson(new FileReader(fileName), MetaData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return metaData;
    }
}
