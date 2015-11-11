package main;

import data.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mvc.Controller;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.InvalidPropertiesFormatException;

/**
 * Created by oscar on 26/09/15.
 */
public class Main extends Application {
    //Lista de WAPs de la aplicación y metadatos
    private static MetaData metaData;

    //Valores de las WAP y métodos para tratarlas
    private static Measures measures;

    public static void main(String[] args) {
        House house = loadHouse();
        loadData(args[0], house);
        launch(args); // De Application
    }

    //Carga los datos del fichero JSON
    private static void loadData(final String metaDataFileName, House house) {
        metaData = MetaDataReader.fromFile(metaDataFileName);
        measures = MeasuresReader.fromFile(metaData);
    }

    private static House loadHouse() {
        //TODO Provisional
        int[] kitchenXPoints = {35, 139, 139, 63, 63, 34};
        int[] kitchenYPoints = {40, 40, 138, 138, 109, 109};

        int[] diningRoomXPoints = {142, 270, 270, 142};
        int[] diningRoomYPoints = {13, 13, 170, 170};

        int[] corridorXPoints = {71, 71, 160, 161, 188, 188, 189, 189, 187, 187, 139, 139, 142, 141, 139, 138};
        int[] corridorYPoints = {141, 192, 192, 284, 284, 212, 212, 201, 200, 175, 135, 166, 166, 145, 145, 142};

        int[] balconyXPoints = {33, 34, 97, 97, 115, 115, 135, 135, 105, 105};
        int[] balconyYPoints = {10, 33, 34, 38, 38, 35, 35, 13, 11, 10};

        int[] bedroom1XPoints = {70, 71, 156, 157};
        int[] bedroom1YPoints = {208, 284, 284, 209};

        int[] bedroom2XPoints = {161, 161, 117, 117, 113, 113, 118, 118, 188, 188};
        int[] bedroom2YPoints = {285, 301, 300, 303, 303, 374, 374, 378, 378, 287};

        int[] bedroom3XPoints = {189, 189, 192, 192, 266, 266, 271, 270, 250, 250};
        int[] bedroom3YPoints = {269, 282, 282, 278, 378, 375, 376, 302, 302, 267};

        int[] drawingRoomXPoints = {};
        int[] drawingRoomYPoints = {};

        int[] wc1XPoints = {191, 269, 270, 191};
        int[] wc1YPoints = {175, 176, 217, 216};

        int[] wc2XPoints = {191, 270, 270, 191};
        int[] wc2YPoints = {263, 264, 219, 219};

        Polygon kitchen = new Polygon(kitchenXPoints, kitchenYPoints, kitchenXPoints.length);
        Polygon diningRoom = new Polygon(diningRoomXPoints, diningRoomYPoints, diningRoomXPoints.length);
        Polygon corridor = new Polygon(corridorXPoints, corridorYPoints, corridorXPoints.length);
        Polygon balcony = new Polygon(balconyXPoints, balconyYPoints, balconyXPoints.length);
        Polygon bedroom1 = new Polygon(bedroom1XPoints, bedroom1YPoints, bedroom1XPoints.length);
        Polygon bedroom2 = new Polygon(bedroom2XPoints, bedroom2YPoints, bedroom2XPoints.length);
        Polygon bedroom3 = new Polygon(bedroom3XPoints, bedroom3YPoints, bedroom3XPoints.length);
        Polygon drawingRoom = new Polygon(drawingRoomXPoints, drawingRoomYPoints, drawingRoomXPoints.length);
        Polygon wc1 = new Polygon(wc1XPoints, wc1YPoints, wc1XPoints.length);
        Polygon wc2 = new Polygon(wc2XPoints, wc2YPoints, wc2XPoints.length);

        House house = new House();

        house.addNewRoom("KITCHEN");
        house.addNewRoom("DININGROOM");
        house.addNewRoom("CORRIDOR");
        house.addNewRoom("BALCONY");
        house.addNewRoom("BEDROOM1");
        house.addNewRoom("BEDROOM2");
        house.addNewRoom("BEDROOM3");
        house.addNewRoom("DRAWINGROOM");
        house.addNewRoom("WC1");
        house.addNewRoom("WC2");

        house.addRoomPolygon("KITCHEN", kitchen);
        house.addRoomPolygon("DININGROOM", diningRoom);
        house.addRoomPolygon("CORRIDOR", corridor);
        house.addRoomPolygon("BALCONY", balcony);
        house.addRoomPolygon("BEDROOM1", bedroom1);
        house.addRoomPolygon("BEDROOM2", bedroom2);
        house.addRoomPolygon("BEDROOM3", bedroom3);
        house.addRoomPolygon("DRAWINGROOM", drawingRoom);
        house.addRoomPolygon("WC1", wc1);
        house.addRoomPolygon("WC2", wc2);

        return house;
    }

    private static House loadHouse(final String jsonHouseFile) {
        House house = new House();
        try {
            house.loadHouseFromJsonFile(jsonHouseFile);
        }
        catch (FileNotFoundException fe) {
            //TODO carga de la casa
        }
        catch (InvalidPropertiesFormatException ie) {

        }
        return house;
    }

    // 1) Inicializa la interfaz gráfica (carga sample.fxml)
    // 2) Actualiza la interfaz con las redes y sus valores
    // Stage es una clase que extiende a Window (interfaz gráfica)
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carga la jerarquía del objeto desde un fxml
        FXMLLoader loader = new FXMLLoader();

        // Obtiene el objeto de tipo Parent mediante un Stream y con métodos de FXMLLoader
        Parent root = loader.load(getClass().getResource("../gui/sample.fxml").openStream());

        // Obtiene el objeto controlador de la interfaz gráfica (método de FXMLLoader)
        Controller controller = loader.getController();

        // Pone el título a la ventana
        primaryStage.setTitle("WiFi Fingerprints");

        // Crea la ventana propiamente dicha, pasando el Stream del fxml y dando el tamaño
        primaryStage.setScene(new Scene(root, 1000, 500));

        // Llama a métodos del controlador para pasar los datos y muestra la ventana
        controller.setReadings(measures.getVisibleReadings());
        controller.setMetaData(metaData);
        controller.setMeasures(measures);
        controller.setHouse(this.loadHouse());
        primaryStage.show();
    }
}
