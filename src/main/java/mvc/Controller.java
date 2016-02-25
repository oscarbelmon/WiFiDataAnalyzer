package mvc;

import data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    // Elementos gráficos
    // ===========================================================
    @FXML
    private Slider mySlider;
    @FXML
    private Label numberWAPs;
    @FXML
    private BarChart<String, Number> readingsChart;
    @FXML
    private CategoryAxis waps;
    @FXML
    private ImageView houseImage;

    FileChooser chooser = new FileChooser();

    // ==============================================================

    // Variables del controlador
    // ==============================================================
    private Map<String, XYChart.Series> seriesMap = new HashMap<>();
    private Measures measures;
    private House house;
    private Readings readings;
    private MetaData metaData;
    // ==============================================================


    /**
     * Añade el escuchador al slider.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mySlider.valueProperty().addListener((a, b, c) -> {
            updateChartWithIntensity(c.intValue());
        });
    }


    /**
     * Actualiza el gráfico con la intensidad que se le pasa.
     * Usado por el slider. Escuchador slider -> updateChartWithIntensity(Parámetro)
     *
     * @param intensity - Parámetro de intensidad.
     */
    private void updateChartWithIntensity(int intensity) {
        Readings readingsTotal;
        Readings readingsIntensity;

        for(String roomString: seriesMap.keySet()) {
            // Obtiene el subconjunto de lecturas para las habitaciones seleccionadas
            readingsTotal = measures.getVisibleReadings().getVisibleReadingByRoom(roomString);
            readingsIntensity = readingsTotal.getReadingsGreeterOrEqualTo(intensity);

            // Actualiza todas las WAP para la habitación seleccionada, modificando la ventana
            XYChart.Series series = seriesMap.get(roomString);
            float intensityTotal;
            float intensityLevel;
            int result;
            for(int i = 0; i < metaData.getNumberOfMacs(); i++) {
                intensityTotal = readingsTotal.getVisibleReadingsByWAP(metaData.getWAPByIndex(i)).getNumberOfReadings();
                intensityLevel = readingsIntensity.getVisibleReadingsByWAP(metaData.getWAPByIndex(i)).getNumberOfReadings();
                result = (int)(intensityLevel/intensityTotal*100); //Intensidad porcentual
                XYChart.Data<String, Number> data = (XYChart.Data<String, Number>)series.getData().get(i);
                data.setYValue(result);
            }
        }
    }


    /**
     * Actualiza el gráfico añadiendo la habitación que se pasa como parámetro.
     *
     * @param room - Nombre de la habitación.
     */
    private void updateChart(String room) {
        if(seriesMap.containsKey(room) == false) {
            Readings readingsRoom = readings.getVisibleReadingByRoom(room);
            if(seriesMap.isEmpty())
                setSeries(readingsRoom, room.toString());
            else
                addSeries(readingsRoom, room.toString());
        }
    }


    /**
     * Elimina la habitación del gráfico.
     *
     * @param room - Habitación a eliminar.
     */
    private void removeSeries(String room) {
        // Elimina todos los datos de la habitación del diagrama
        readingsChart.getData().removeAll(seriesMap.get(room));
        // Elimina la habitación del HashMap de series
        seriesMap.remove(room);
    }


    /**
     * Crea y devuelve el gráfico.
     * También le pone el nombre.
     *
     * @param readingsRoom - Lecturas de todas las habitaciones.
     * @param label - Nombre del gráfico.
     * @return - Gráfico.
     */
    //TODO: Considerar implementación multithread.
    private XYChart.Series<String, Number> initializeSeries(Readings readingsRoom, String label) {
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName(label);
        String categoryName;
        Number categoryData;
        Number totalData;
        int level = (int) mySlider.getValue();
        int percentage;
        // Por cada MAC
        for(int i = 0; i < metaData.getNumberOfMacs(); i++) {
            totalData = readingsRoom.getVisibleReadingsByWAP(metaData.getWAPByIndex(i)).getNumberOfReadings();
            categoryData = readingsRoom.getReadingsGreeterOrEqualTo(level).getVisibleReadingsByWAP(metaData.getWAPByIndex(i)).getNumberOfReadings();
            percentage = (int)(categoryData.floatValue()/totalData.floatValue()*100);
            categoryName = metaData.getWAPByIndex(i).getName();
            serie.getData().add(new XYChart.Data<>(categoryName, percentage));
        }
        seriesMap.put(label, serie);
        return serie;
    }


    private void addSeries(Readings readings, String label) {
        XYChart.Series series = initializeSeries(readings, label);
        readingsChart.getData().addAll(series);
        seriesMap.put(label, series);
    }


    private void setSeries(Readings readings, String label) {
        XYChart.Series series = initializeSeries(readings, label);
        readingsChart.getData().setAll(series);
        seriesMap.put(label, series);
    }


    /**
     * Coloca las WAPs en el gráfico, en la parte inferior.
     * Si se carga otra casa cambiarán, y si no se selecciona ninguna habitación no se mostrarán.
     */
    private void populateChart() {
        String[] wapsArray = new String[metaData.getNumberOfMacs()];
        for(int i = 0; i < metaData.getNumberOfMacs(); i++) {
            wapsArray[i] = metaData.getWAPByIndex(i).getName();
        }
        ObservableList<String> wapNames = FXCollections.observableArrayList(wapsArray);
        waps.setCategories(wapNames);
        waps.setTickLabelRotation(90);
        waps.setAutoRanging(true);
    }


    /**
     * ============================
     * === Métodos ESCUCHADORES ===
     * ============================
     */

    /**
     * Devuelve la posición del ratón al clicar detro de la imagen.
     *
     * @param event - Evento de clic dentro de la imagen.
     */
    @FXML
    private void handleImageClicked(MouseEvent event) {
        double mouseXPosition = event.getX();
        double mouseYPosition = event.getY();

        String clickedPosition = getClickedPosition(mouseXPosition, mouseYPosition);
        if (clickedPosition != null) {
            if (!seriesMap.containsKey(clickedPosition.toString()))
                updateChart(clickedPosition);
            else
                removeSeries(clickedPosition);
        }
    }


    /**
     * Carga una casa a partir del fichero que se va a seleccionar, que ha de seguir el formato
     * del fichero "house.json" de la ayuda.
     *
     * @param event - Selección de la opción "Load a new house" dentro de "House options".
     */
    @FXML
    private void handleLoadHouseMenuAction(javafx.event.Event event) {
        //Abre el selector de ficheros
        chooser.setTitle("Select a house .json file");
        File houseJsonFile = chooser.showOpenDialog(new Stage());

        //Para prevenir cierres de la ventana
        if (houseJsonFile != null) {

            if (!this.load(houseJsonFile))
                this.showErrorMessageLoadingTheHouse();
        }

        else
            this.showErrorMessageLoadingAFile();

    }

    /**
     * A partir de un objeto File con la dirección del fichero de la casa.
     * 1) Carga el fichero de metadatos a partir de una ruta que puede ser relativa o absoluta.
     * 2) Manda actualizar la interfaz.
     *
     * @param houseJsonFile - Fichero .json de la casa, introducido como una objeto File.
     * @return - Estado final de la carga.
     */
    public boolean load(File houseJsonFile) {
        boolean correctlyLoadedFile = false;
        String extensionOfFileToLoad = this.getFileExtension(houseJsonFile.getName());
        House newHouse;
        MetaData newMetadata;
        Measures newMeasures;

        if (extensionOfFileToLoad.equals("json") && houseJsonFile.canRead()) {
            newHouse = new House();

            if (newHouse.loadHouseFromJsonFile(houseJsonFile.getAbsolutePath())) {
                String metadataFileDirection = newHouse.getDataFileDir();

                if (!metadataFileDirection.equals("")) {
                    extensionOfFileToLoad = this.getFileExtension(metadataFileDirection);

                    if (extensionOfFileToLoad.equals("json")) {
                        Path metaDataFilePath = Paths.get(houseJsonFile.getParent());
                        metaDataFilePath = metaDataFilePath.resolve(Paths.get(metadataFileDirection).normalize()).normalize();
                        String finalMetaDataFileDirection = metaDataFilePath.toString();

                        newMetadata = MetaDataReader.fromFile(finalMetaDataFileDirection);

                        if (newMetadata != null) {
                            newMetadata.setMetaDataFile(finalMetaDataFileDirection);
                            newMeasures = MeasuresReader.fromFile(newMetadata, newHouse.getRoomsNames());

                            if (newMeasures != null) {
                                Object[] roomsInChart = seriesMap.keySet().toArray();
                                for (int i = 0; i < roomsInChart.length; i++)
                                    this.removeSeries((String) roomsInChart[i]);

                                this.setHouse(newHouse);
                                this.setMeasures(newMeasures);
                                this.setMetaData(newMetadata);
                                this.setReadings(newMeasures.getVisibleReadings());
                                this.populateChart();

                                correctlyLoadedFile = true;
                            }
                        }

                    }
                }

            }
        }

        return correctlyLoadedFile;
    }

    /**
     * Muestra un mensaje de error por la selección de un fichero con un formato distinto a .json.
     */
    private void showErrorMessageLoadingAFile() {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Error");
        error.setHeaderText("Invalid file selected");
        error.setContentText("The selected file is not a valid .json file.");
        error.show();
    }


    /**
     * Muestra un mensaje de error por el error de cargado de un fichero .json.
     */
    private void showErrorMessageLoadingTheHouse() {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Invalid data file format");
        error.setHeaderText("There has been a problem loading the house data.");
        error.setContentText("Please, revise the file format to ensure that there\n" +
                "are not mistakes in the file. ");
        error.show();
    }


    /**
     * Gestiona la ventana de ayuda de la aplicación, y las subventanas asociadas, que muestran los esquemas.
     *
     * @param event - Selección de la opción de "Steps for use this program" dentro del menú de ayuda.
     */
    @FXML
    private void handleHelpMenuAction(javafx.event.Event event) {
        String helpText = "\n" +
        "1) Load a valid house/area .json file.\n" +
                "The direction of the .json metadata file and the image must be inside the house.json file." +
                "\nAll the directions inside the 2 files (house/area and metadata) must be correct,\n" +
                "and it can be absolute or relative paths." +
                "\n\n2) The rooms are selected clicking it in the house image, and the level of intensity\n" +
                "changing the slider value at the bottom of the window.";

        String house = "{\n" +
                "\t\"id\": \"...\",\n" +
                "\t\"title\": \"...\",\n" +
                "\t\"raster\": \"imageDir\"\n" +
                "\t\"data\": \"dataJsonFile\"\n" +
                "\t\"geo\": {\n" +
                "\t\t\"type\": \"FeatureCollection\",\n" +
                "\t\t\"features\": [\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"type\": \"Feature\",\n" +
                "\t\t\t\t\"properties\": {},\n" +
                "\t\t\t\t\"id\": \"ROOM_NAME\",\n" +
                "\t\t\t\t\"geometry\": {\n" +
                "\t\t\t\t\t\"type\": \"Polygon\",\n" +
                "\t\t\t\t\t\"coordinates\": [\n" +
                "\t\t\t\t\t\t[\n" +
                "\t\t\t\t\t\t\t[x1, y1], [x2, y2], ...\n" +
                "\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t}\n" +
                "\t\t\t},\n" +
                "\t\t\t.\n" +
                "\t\t\t.\n" +
                "\t\t\t.\n" +
                "\t\t],\n" +
                "\t\t\"crs\": {\n" +
                "\t\t\t\"type\": \"name\",\n" +
                "\t\t\t\"properties\": {\n" +
                "\t\t\t\t\"name\": \"urn:ogc:def:crs:OGC:1.3:CRS84\"\n" +
                "\t\t\t}\n" +
                "\t    }\n" +
                "\t}\n" +
                "}";

        String data = "{\n" +
                "  \"trainingDataFile\": \"txtFileDir\",\n" +
                "  \"validationDataFile\": \"txtFileDir\",\n" +
                "  \"trainingSamples\":  ...,\n" +
                "  \"validationSamples\": ...,\n" +
                "  \"numberOfMacs\": ...,\n" +
                "  \"numberOfMacsTraining\" :...,\n" +
                "  \"numberOfMacsValidation\": ...,\n" +
                "  \"rangeTraining\": {\n" +
                "    \"min\": ...,\n" +
                "    \"max\": ...\n" +
                "  },\n" +
                "  \"rangeValidation\": {\n" +
                "    \"min\": ...,\n" +
                "    \"max\": ...\n" +
                "  },\n" +
                "  \"WAPs\" : [\n" +
                "    {\"name\": \"WAP00000\",\"macAddress\":\"dirMAC\"},\n" +
                "\t.\n" +
                "\t.\n" +
                "\t.\n" +
                "  ]\n" +
                "}";

        Stage helpWindow = new Stage();
        helpWindow.initModality(Modality.WINDOW_MODAL);
        helpWindow.setTitle("Instructions for using the program");
        helpWindow.setMinWidth(500);
        helpWindow.setMinHeight(300);
        helpWindow.setResizable(true);

        Stage houseSchemeWindow = new Stage();
        houseSchemeWindow.initModality(Modality.WINDOW_MODAL);
        houseSchemeWindow.setTitle("House .json file format");
        houseSchemeWindow.setHeight(600);
        houseSchemeWindow.setWidth(450);
        houseSchemeWindow.setResizable(false);

        Stage dataSchemeWindow = new Stage();
        dataSchemeWindow.initModality(Modality.WINDOW_MODAL);
        dataSchemeWindow.setTitle("House metadata .json file format");
        dataSchemeWindow.setHeight(450);
        dataSchemeWindow.setWidth(350);
        dataSchemeWindow.setResizable(false);

        // Se crean todos los cuadros de texto
        Label helpWindowText = new Label(helpText);
        helpWindowText.setFont(Font.font("Times New Roman", 16.0));

        javafx.scene.control.TextArea houseScheme = new TextArea(house);
        houseScheme.setEditable(false);
        houseScheme.setMinSize(450, 600);

        javafx.scene.control.TextArea dataScheme = new TextArea(data);
        dataScheme.setEditable(false);
        dataScheme.setMinSize(350, 450);

        // Se forman todas las ventanas, añadiendo los contenedores y el contenido
        VBox houseSchemeWindowBox = new VBox();
        houseSchemeWindowBox.getChildren().add(houseScheme);

        VBox dataSchemeWindowBox = new VBox();
        dataSchemeWindowBox.getChildren().add(dataScheme);

        javafx.scene.control.Button showHouseScheme = new Button("Show house .json format");
        javafx.scene.control.Button showDataScheme = new Button("Show data .json format");

        BorderPane helpWindowBox = new BorderPane();
        helpWindowBox.setPadding(new Insets(10, 10, 10, 10));
        helpWindowBox.setTop(helpWindowText);
        BorderPane subBox = new BorderPane();
        subBox.setPadding(new Insets(10, 10, 10, 10));
        subBox.setLeft(showHouseScheme);
        subBox.setRight(showDataScheme);
        helpWindowBox.setBottom(subBox);

        // Las termina de crear y muestra la principal
        Scene helpWindowScene = new Scene(helpWindowBox);
        helpWindow.setScene(helpWindowScene);

        Scene houseSchemeScene = new Scene(houseSchemeWindowBox);
        houseSchemeWindow.setScene(houseSchemeScene);

        Scene dataSchemeScene = new Scene(dataSchemeWindowBox);
        dataSchemeWindow.setScene(dataSchemeScene);

        showHouseScheme.setOnAction(e -> houseSchemeWindow.show());
        showDataScheme.setOnAction(e -> dataSchemeWindow.show());

        helpWindow.show();
    }


    /**
     * =======================
     * === Métodos SETTERS ===
     * =======================
     */
    public void setReadings(Readings readings) {
        this.readings = readings;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
        populateChart();
    }

    public void setMeasures(Measures measures) {
        this.measures = measures;
    }

    /**
     * Cambia la casa actual por otra y cambia la imagen mostrada.
     *
     * @param house
     */
    public void setHouse(House house) {
        if (house != null) {
            this.house = house;
            changeImage(house.getImageDir());
        }
    }


    /**
     * ==========================
     * === Métodos AUXILIARES ===
     * ==========================
     */

    /**
     * Devuelve la extensión del nombre del fichero pasado como parámetro.
     *
     * @return - Extensión
     */
    private String getFileExtension(String fullFileName) {
        String fileExtension = null;

        String[] partsOfFileName = fullFileName.split("\\.");

        if (partsOfFileName.length > 1)
            fileExtension = partsOfFileName[partsOfFileName.length-1];

        return fileExtension;
    }

    /**
     * Devuelve el nombre de la habitación al clicar dentro de la imagen.
     * Se devolverá aquel nombre asociado con la zona de la imagen.
     *
     * @param mouseX - Posición X del ratón.
     * @param mouseY - Posición Y del ratón.
     * @return - Nombre de la habitación.
     */
    private String getClickedPosition(double mouseX, double mouseY) {
        String[] locations = house.getRoomsNames();

        for (String location: locations) {
            Polygon actualLocation = house.getRoom(location);
            if (actualLocation.contains(mouseX, mouseY))
                return location;
        }
        return null;
    }

    /**
     * Cambia la imagen mostrada en la ventana por la que se quiere cargar,
     * si se carga correctamente.
     *
     * @param absoluteURI - Dirección absoluta a la imagen.
     * @return - Si se ha cambiado la imagen correctamente o no.
     */
    private boolean changeImage(String absoluteURI) {
        if (absoluteURI == null || absoluteURI.equals(""))
            return false;

        else {
            String imageUri = "file:" + absoluteURI;
            Image image = new Image(imageUri);
            houseImage.setImage(image);
            return true;
        }
    }


}