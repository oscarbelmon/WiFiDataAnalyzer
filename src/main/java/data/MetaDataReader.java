package data;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * MetaDataReader
 *
 * Carga los datos de un fichero y devuelve un objeto MetaData.
 */

public class MetaDataReader {
    public static MetaData fromFile(String fileName) {
        MetaData metaData;
        Gson gson = new Gson();

        try {
            String content = new String(Files.readAllBytes(Paths.get(fileName)));
            metaData = gson.fromJson(new FileReader(fileName), MetaData.class);
        } catch (IOException e) {
            return null;
        }
        return metaData;
    }
}