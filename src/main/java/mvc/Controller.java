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
        Readings readingsIntensityWAP;

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
            if(seriesMap.containsKey("ALL")) setSeries(readingsRoom, room.toString());
            else addSeries(readingsRoom, room.toString());
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
        int level = (int)mySlider.getValue();
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
        //seriesMap.remove("ALL");
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
        //setSeries(readings, "ALL");
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
        chooser.setTitle("Select a house .json file");
        File houseJsonFile = chooser.showOpenDialog(new Stage());

        if (houseJsonFile != null) {
            String[] fileNameParts = houseJsonFile.getName().split("\\.");
            String fileExtension = fileNameParts[fileNameParts.length-1];

            if (fileExtension.equals("json") && houseJsonFile.canRead()) {
                Alert houseOK = new Alert(Alert.AlertType.INFORMATION);
                houseOK.setTitle("House file loaded correctly");
                houseOK.setHeaderText("The house has been loaded correctly.");
                houseOK.setContentText("Now, it's necessary to load a training data .json file \n" +
                        "in order to create the graph.");
                houseOK.showAndWait();

                chooser.setTitle("Select a measures data .json file");
                File dataFile = chooser.showOpenDialog(new Stage());

                if (dataFile != null) {
                    String[] dataFileNameParts = dataFile.getName().split("\\.");
                    String dataFileExtension = dataFileNameParts[dataFileNameParts.length-1];

                    if (dataFileExtension.equals("json") && dataFile.canRead()) {
                        House newHouse = new House();
                        if (newHouse.loadHouseFromJsonFile(houseJsonFile.getAbsolutePath())) {
                            MetaData newMetadata = MetaDataReader.fromFile(dataFile.getPath());
                            if (newMetadata != null) {
                                Measures newMeasures = MeasuresReader.fromFile(newMetadata, newHouse.getRoomsNames());
                                if (newMeasures != null) {
                                    this.setMetaData(newMetadata);
                                    this.setMeasures(newMeasures);
                                    this.setHouse(newHouse);
                                    this.setReadings(measures.getVisibleReadings());
                                }
                                else
                                    showErrorMessageLoadingADataFile();

                            }
                            else
                                showErrorMessageLoadingADataFile();
                            
                        }
                        else {
                            Alert houseFileFormatError = new Alert(Alert.AlertType.ERROR);
                            houseFileFormatError.setTitle("Invalid house file format");
                            houseFileFormatError.setHeaderText("There has been a problem loading the house.");
                            houseFileFormatError.setContentText("Please, revise the file format to ensure that there\n" +
                                    "are not mistakes in the file. ");
                            houseFileFormatError.show();
                        }
                    }
                    else
                        showErrorMessageLoadingAJsonFile();
                }
            }
            else
                showErrorMessageLoadingAJsonFile();

        }
    }

    private void showErrorMessageLoadingAJsonFile() {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Error");
        error.setHeaderText("Invalid file selected");
        error.setContentText("The selected file is not a valid .json file.");
        error.show();
    }

    private void showErrorMessageLoadingADataFile() {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Invalid data file format");
        error.setHeaderText("There has been a problem loading the house data.");
        error.setContentText("Please, revise the file format to ensure that there\n" +
                "are not mistakes in the file. ");
        error.show();
    }

    @FXML
    private void handleHelpMenuAction(javafx.event.Event event) {
        String helpText = "\n" +
        "1) First of all, a valid house, or area data .json file must be loaded.\n" +
                "That file contains the image used by the house and the polygons data,\n" +
                "that represents the rooms of that house, where the measurements are made.\n\n" +
                "2) Once the house file has been loaded correctly, is necessary to load the measures\n " +
                "data file in order to generate the graphics of the measurements. This file must be .json. \n" +
                "Once it has been loaded, the house map will appear in the left part of the window.\n\n" +
                "3) The rooms are selected clicking it in the house image, and the level of intensity\n" +
                "changing the slider value at the bottom of the window."+
                "\n\n\n\n";

        String house = "{\n" +
                "\t\"id\": \"...\",\n" +
                "\t\"title\": \"...\",\n" +
                "\t\"raster\": {\n" +
                "\t\t\"uri\": \"imageDir\"\n" +
                "\t},\n" +
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
        dataSchemeWindow.setTitle("House data .json file format");
        dataSchemeWindow.setHeight(450);
        dataSchemeWindow.setWidth(350);
        dataSchemeWindow.setResizable(false);

        Label helpWindowText = new Label(helpText);
        helpWindowText.setFont(Font.font("Times New Roman", 16.0));

        javafx.scene.control.TextArea houseScheme = new TextArea(house);
        houseScheme.setEditable(false);
        houseScheme.setMinSize(450, 600);

        javafx.scene.control.TextArea dataScheme = new TextArea(data);
        dataScheme.setEditable(false);
        dataScheme.setMinSize(350, 450);

        VBox houseSchemeWindowBox = new VBox();
        houseSchemeWindowBox.getChildren().add(houseScheme);

        VBox dataSchemeWindowBox = new VBox();
        dataSchemeWindowBox.getChildren().add(dataScheme);

        javafx.scene.control.Button showHouseScheme = new Button("Show house .json format");
        javafx.scene.control.Button showDataScheme = new Button("Show data .json format");

        VBox helpWindowBox = new VBox();
        helpWindowBox.getChildren().addAll(helpWindowText, showHouseScheme, showDataScheme);
        helpWindowBox.setAlignment(Pos.TOP_LEFT);

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
        this.house = house;
        changeImage(house.getImageDir());
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
        //TODO revisar si falla
        if (uri == null || uri.equals(""))
            return false;

        else {
            Image image = new Image(uri);
            houseImage.setImage(image);
            return true;
        }
    }


}