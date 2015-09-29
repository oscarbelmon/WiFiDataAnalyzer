package main;

import data.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mvc.Controller;

/**
 * Created by oscar on 26/09/15.
 */
public class Main extends Application {
    private static MetaData metaData;
    private static Measures measures;

    public static void main(String[] args) {
        loadData(args[0]);
        launch(args);
//        ejecuta(args[0]);
    }

    private static void loadData(final String metaDataFileName) {
        metaData = MetaDataReader.fromFile(metaDataFileName);
        measures = MeasuresReader.fromFile(metaData);
    }

    private static void ejecuta(final String metaDataFileName) {
        metaData = MetaDataReader.fromFile(metaDataFileName);
        measures = MeasuresReader.fromFile(metaData);
//        MetaData metaData = MetaDataReader.fromFile("src/main/resources/meta_data.json");
//        Measures measures = MeasuresReader.fromFile("src/main/resources/sensorstrainingData_belmonte.txt", metaData);
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
        for(int i = 0; i < metaData.getNumberOfMacs(); i++) {
            Readings bedroom1WAPVisible = bedroom1Visible.getVisibleReadingsByWAP(metaData.getWAPByIndex(i));
            System.out.println(i + " --> " + bedroom1WAPVisible.getNumberOfReadings());
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        System.out.println("Tres");
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("../gui/sample.fxml").openStream());
        Controller controller = loader.getController();
        primaryStage.setTitle("WiFi Fingerprints");
        primaryStage.setScene(new Scene(root, 800, 275));
        controller.setReadings(measures.getVisibleReadings());
        controller.setMetaData(metaData);
        primaryStage.show();
    }
}
