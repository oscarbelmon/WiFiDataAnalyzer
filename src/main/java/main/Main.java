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
//        Measures filtered = measures.getMeasuresReadingsGreeterOrEqualTo(-20);
//        System.out.println(filtered.getNumberMeasures());
//        System.out.println(measures.getMeasureByIndex(1000).getVisibleReadings().size());
        System.out.println(measures.getMeasuresByRoom(Room.BEDROOM1).getMeanNumberVisibleWAPs());
        System.out.println(measures.getMeasuresByRoomAndRelativePosition(Room.BEDROOM1, RelativePosition.CENTER).getMeanNumberVisibleWAPs());
        System.out.println(measures.getMeasuresByRoomAndRelativePosition(Room.BEDROOM1, RelativePosition.CORNER).getMeanNumberVisibleWAPs());
        System.out.println(measures.getMeasuresByRoomAndRelativePosition(Room.BEDROOM1, RelativePosition.DOOR).getMeanNumberVisibleWAPs());
        System.out.println(measures.getMeasuresByRoomAndRelativePosition(Room.BEDROOM2, RelativePosition.CENTER).getMeanNumberVisibleWAPs());
        System.out.println(measures.getMeasuresByRoomAndRelativePosition(Room.BEDROOM2, RelativePosition.CORNER).getMeanNumberVisibleWAPs());
        System.out.println(measures.getMeasuresByRoomAndRelativePosition(Room.BEDROOM2, RelativePosition.DOOR).getMeanNumberVisibleWAPs());
        System.out.println(measures.getMeasuresByRoomAndRelativePosition(Room.BEDROOM3, RelativePosition.CENTER).getMeanNumberVisibleWAPs());
        System.out.println(measures.getMeasuresByRoomAndRelativePosition(Room.BEDROOM3, RelativePosition.CORNER).getMeanNumberVisibleWAPs());
        System.out.println(measures.getMeasuresByRoomAndRelativePosition(Room.BEDROOM3, RelativePosition.DOOR).getMeanNumberVisibleWAPs());
    }
}
