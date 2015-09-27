package main;

import data.*;

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
        Measures filtered = measures.getMeasuresReadingsGreeterOrEqualTo(-20);
        System.out.println(filtered.getNumberMeasures());
        System.out.println(filtered);
    }
}
