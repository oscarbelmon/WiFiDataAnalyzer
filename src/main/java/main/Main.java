package main;

import data.House;
import data.Measures;
import data.MetaData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import mvc.Controller;

import java.io.File;
import java.net.URL;


public class Main extends Application {
    //Lista de WAPs de la aplicación y metadatos
    private static MetaData metaData;
    //Valores de las WAP y métodos para tratarlas
    private static Measures measures;

    private static House house;

    private static String[] arguments;

    public static void main(String[] args) {
        house = new House();
        metaData = new MetaData();
        measures = new Measures();
        arguments = args;
        launch(args);
    }

    // 1) Inicializa la interfaz gráfica (carga sample.fxml)
    // 2) Actualiza la interfaz con las redes y sus valores
    // Stage es una clase que extiende a Window (interfaz gráfica)
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carga la jerarquía del objeto desde un fxml
        FXMLLoader loader = new FXMLLoader();
        String applicationDir = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

        //TODO: Utilizar variables de entorno para el testing
        //String pathToFXML = "src/main/resources/gui/sample.fxml";
        String pathToFXML = "appData/gui/sample.fxml";
        URL fxmlURL = new File(pathToFXML).toURI().toURL();
        loader.setLocation(fxmlURL);

        Parent root = loader.load();

        // Obtiene el objeto controlador de la interfaz gráfica (método de FXMLLoader)
        Controller controller = loader.getController();

        // Pone el título a la ventana
        primaryStage.setTitle("WiFi Fingerprints");

        // Crea la ventana propiamente dicha, pasando el Stream del fxml y dando el tamaño
        primaryStage.setScene(new Scene(root, 1000, 500));
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(800);
        primaryStage.setResizable(true);

        // Llama a métodos del controlador para pasar los datos y muestra la ventana
        controller.setReadings(measures.getVisibleReadings());
        controller.setMetaData(metaData);
        controller.setMeasures(measures);
        controller.setHouse(house);

        if (arguments != null && arguments.length == 1) {
            String fileDir = arguments[0];
            //String applicationDir = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

            File houseDir = new File(fileDir);
            if (houseDir.exists() && houseDir.isFile() && houseDir.canRead())
                controller.load(houseDir);
            else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error loading the file");
                error.setContentText("There has been an error loading the file passed as an argument.");
            }
        }

        primaryStage.show();
    }
}