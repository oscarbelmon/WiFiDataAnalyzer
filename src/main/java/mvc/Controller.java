package mvc;

import data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
     * @param room - Habitación a eliminar.
     */
    private void removeSeries(String room) {
        // Elimina todos los datos de la habitación del diagrama
        readingsChart.getData().removeAll(seriesMap.get(room));
        // Elimina la habitación del HashMap de series
        seriesMap.remove(room);
    }


    /**
     * Crea y devuelve el gráfico. Lo inicializa con "ALL" y todos los valores a su máxima intensidad (100%).
     * También le pone el nombre.
     *
     * @param readingsRoom - Lecturas de todas las habitaciones.
     * @param label - Nombre del gráfico.
     * @return - Gráfico.
     */
    private XYChart.Series<String, Number> seriesMaxIntensity2(Readings readingsRoom, String label) {
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
        XYChart.Series series = seriesMaxIntensity2(readings, label);
        readingsChart.getData().addAll(series);
        seriesMap.put(label, series);
    }


    private void setSeries(Readings readings, String label) {
        XYChart.Series series = seriesMaxIntensity2(readings, label);
        readingsChart.getData().setAll(series);
        seriesMap.put(label, series);
    }


    /**
     * Coloca las WAPs en el gráfico, en la parte inferior.
     * No varían durante el transcurso de la aplicación.
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
     * Escuchadores
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
     * Carga una casa a partir de dos ficheros .json, uno que define la imagen y los polígonos de la casa
     * y otro que aporta los datos con todas las mediciones.
     * @param event
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
     * @param houseJsonFile - Fichero .json de la casa, introducido como una objeto File.
     * @return - Estado final de la carga.
     */
    //TODO: Refactor urgently needed
    public boolean load(File houseJsonFile) {
        boolean correctlyLoaded = false;

        String[] houseFileNameParts = houseJsonFile.getName().split("\\.");
        String fileExtension = houseFileNameParts[houseFileNameParts.length-1];

        if (fileExtension.equals("json") && houseJsonFile.canRead()) {
            House newHouse = new House();
            //Carga los datos de la casa del fichero .json, incluye la imagen de la interfaz
            if (newHouse.loadHouseFromJsonFile(houseJsonFile.getAbsolutePath())) {
                //Dirección del fichero de datos (también .json)
                String dataFileDir = newHouse.getDataFileDir();

                if (!dataFileDir.equals("")) {
                    String[] dataFileNameParts = dataFileDir.split("\\.");
                    fileExtension = dataFileNameParts[dataFileNameParts.length-1];

                    if (fileExtension.equals("json")) {
                        Path dataPath = Paths.get(houseJsonFile.getParent());
                        dataPath = dataPath.resolve(Paths.get(dataFileDir).normalize()).normalize();
                        dataFileDir = dataPath.toString();

                        MetaData newMetadata = MetaDataReader.fromFile(dataFileDir);
                        newMetadata.setMetaDataFile(dataFileDir);
                        Measures newMeasures = MeasuresReader.fromFile(newMetadata, newHouse.getRoomsNames());

                        if (newMeasures != null) {
                            Object[] roomsInChart = seriesMap.keySet().toArray();
                            for (int i = 0; i < roomsInChart.length; i++)
                                this.removeSeries((String) roomsInChart[i]);

                            this.setHouse(newHouse);
                            this.setMeasures(newMeasures);
                            this.setMetaData(newMetadata);
                            this.setReadings(newMeasures.getVisibleReadings());
                            this.populateChart();

                            correctlyLoaded = true;
                        }

                    }
                }

            }
        }

        return correctlyLoaded;
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
     * @param event
     */
    @FXML
    private void handleHelpMenuAction(javafx.event.Event event) {
        String helpText = "\n" +
        "1) First of all, a valid house, or area data .json file must be loaded.\n" +
                "That file contains the image used by the house and the polygons data,\n" +
                "that represents the rooms of that house, where the measurements are made, \n" +
                "and the direction of the .json data file, that contains the measurements." +
                "\n2) The rooms are selected clicking it in the house image, and the level of intensity\n" +
                "changing the slider value at the bottom of the window."+
                "\n\n\n\n";

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

        // Se crean todas las ventanas
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
        dataSchemeWindow.setTitle("House data .json file format");
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

        VBox helpWindowBox = new VBox();
        helpWindowBox.getChildren().addAll(helpWindowText, showHouseScheme, showDataScheme);
        helpWindowBox.setAlignment(Pos.TOP_LEFT);

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
     * Setters
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

    public void setHouse(House house) {
        if (house != null) {
            this.house = house;
            changeImage(house.getImageDir());
        }

    }


    /**
     * Métodos auxiliares
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

    private boolean changeImage(String uri) {
        if (uri == null || uri.equals(""))
            return false;

        else {
            String imageUri = "file:" + uri;
            Image image = new Image(imageUri);
            houseImage.setImage(image);
            return true;
        }
    }


}