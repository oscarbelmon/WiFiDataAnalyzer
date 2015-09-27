package main;

import data.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by oscar on 26/09/15.
 */
public class Main {
    public static void main(String[] args) {
        new Main().ejecuta();
    }

    private void ejecuta() {
        MetaData metaData = MetaData.fromFile("src/main/resources/meta_data.json");
        Measures measures = Measures.fromFile("src/main/resources/sensorstrainingData_belmonte.txt", metaData);
        System.out.println(measures.getMeasuresByRoom(Room.BALCONY).size());
    }
}
