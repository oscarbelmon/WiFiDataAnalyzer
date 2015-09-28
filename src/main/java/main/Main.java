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
        MetaData metaData = MetaDataReader.fromFile("src/main/resources/meta_data.json");
        Measures measures = MeasuresReader.fromFile("src/main/resources/sensorstrainingData_belmonte.txt", metaData);
//        Measures filtered = measures.getMeasuresReadingsGreeterOrEqualTo(-20);
//        System.out.println(filtered.getNumberMeasures());
//        System.out.println(measures.getMeasureByIndex(1000).getVisibleReadings().size());
//        System.out.println(measures.getMeasuresByRoom(Room.BEDROOM1).getMeanNumberVisibleWAPs());
//        System.out.println(measures.getMeasuresByRoomAndRelativePosition(Room.BEDROOM1, RelativePosition.CENTER).getMeanNumberVisibleWAPs());
//        System.out.println(measures.getMeasuresByRoomAndRelativePosition(Room.BEDROOM1, RelativePosition.CORNER).getMeanNumberVisibleWAPs());
//        System.out.println(measures.getMeasuresByRoomAndRelativePosition(Room.BEDROOM1, RelativePosition.DOOR).getMeanNumberVisibleWAPs());
//        System.out.println(measures.getMeasuresByRoomAndRelativePosition(Room.BEDROOM2, RelativePosition.CENTER).getMeanNumberVisibleWAPs());
//        System.out.println(measures.getMeasuresByRoomAndRelativePosition(Room.BEDROOM2, RelativePosition.CORNER).getMeanNumberVisibleWAPs());
//        System.out.println(measures.getMeasuresByRoomAndRelativePosition(Room.BEDROOM2, RelativePosition.DOOR).getMeanNumberVisibleWAPs());
//        System.out.println(measures.getMeasuresByRoomAndRelativePosition(Room.BEDROOM3, RelativePosition.CENTER).getMeanNumberVisibleWAPs());
//        System.out.println(measures.getMeasuresByRoomAndRelativePosition(Room.BEDROOM3, RelativePosition.CORNER).getMeanNumberVisibleWAPs());
//        System.out.println(measures.getMeasuresByRoomAndRelativePosition(Room.BEDROOM3, RelativePosition.DOOR).getMeanNumberVisibleWAPs());
//        System.out.println(measures.getNumberMeasures());
//        System.out.println(measures.getNumberVisibleReadings());
//        System.out.println(measures.getVisibleReadings().getNumberOfReadings());
//        WapReadings wapReadings = new WapReadings(measures);
//        System.out.println(wapReadings.getVisibleReadingByRoom(Room.BEDROOM1).getNumberOfReadings());
//        System.out.println(wapReadings.getVisibleReadingsByWAP(metaData.getWAPByIndex(2)).getNumberOfReadings());

        Readings allVisible = measures.getVisibleReadings();
        Readings bedroom1Visible = allVisible.getVisibleReadingByRoom(Room.BEDROOM1);
        System.out.println(bedroom1Visible.getNumberOfReadings());
        Readings greaterThan = bedroom1Visible.getReadingsGreeterOrEqualTo(-100);
        System.out.println(greaterThan.getNumberOfReadings());
        greaterThan = bedroom1Visible.getReadingsGreeterOrEqualTo(-90);
        System.out.println(greaterThan.getNumberOfReadings());
        greaterThan = bedroom1Visible.getReadingsGreeterOrEqualTo(-50);
        System.out.println(greaterThan.getNumberOfReadings());
        greaterThan = bedroom1Visible.getReadingsGreeterOrEqualTo(-30);
        System.out.println(greaterThan.getNumberOfReadings());
        greaterThan = bedroom1Visible.getReadingsGreeterOrEqualTo(-10);
        System.out.println(greaterThan.getNumberOfReadings());
    }
}
