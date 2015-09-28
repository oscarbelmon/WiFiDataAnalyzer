package main;

import data.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by oscar on 26/09/15.
 */
public class Main extends Application {
    private static String dataFileName = "src/main/resources/sensorstrainingData_belmonte.txt";
    private static String metaDataFileName = "src/main/resources/meta_data.json";
    private static MetaData metaData;
    private static Measures measures;

    public static void main(String[] args) {
//        new Main().ejecuta();
        loadData();
        launch(args);
    }

    private static void loadData() {
        metaData = MetaDataReader.fromFile(metaDataFileName);
        measures = MeasuresReader.fromFile(dataFileName, metaData);
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

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("../gui/sample.fxml").openStream());
        Controller controller = loader.getController();
        controller.setReadings(measures.getVisibleReadings().getVisibleReadingByRoom(Room.BEDROOM1));
        primaryStage.setTitle("WiFi Fingerprints");
        primaryStage.setScene(new Scene(root, 800, 275));
        primaryStage.show();
    }
}
