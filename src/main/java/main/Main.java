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


public class Main extends Application {
    //Lista de WAPs de la aplicación y metadatos
    private static MetaData metaData;
    //Valores de las WAP y métodos para tratarlas
    private static Measures measures;

    private static House house;

    public static void main(String[] args) {
        house = new House();
        if (loadHouse()) {
            loadData(args[0], house.getRoomsNames());
            launch(args); // De Application
        }
        else
            System.out.print("Fallo al cargar la casa");

    }

    //Carga los datos del fichero JSON
    private static void loadData(final String metaDataFileName, String[] rooms) {
        metaData = MetaDataReader.fromFile(metaDataFileName);
        measures = MeasuresReader.fromFile(metaData, rooms);
    }

    private static boolean loadHouse() {
        //TODO habitación de más provisional
        if (house.loadHouseFromJsonFile("src/main/resources/home_data.json")) {
            house.addNewRoom("DRAWINGROOM");
            house.addRoomPolygon("DRAWINGROOM", new Polygon());
            return true;
        }

        return false;
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
        controller.setHouse(house);

        primaryStage.show();
    }
}
