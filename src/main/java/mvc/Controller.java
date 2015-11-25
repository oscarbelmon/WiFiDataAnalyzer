package mvc;

import data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.awt.Polygon;
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
    @FXML
    private Button changeHouse;
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
        seriesMap.remove("ALL");
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
        setSeries(readings, "ALL");
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

    @FXML
    private void handleHouseLoadButtonAction(ActionEvent event) {
        System.out.println("Pulsado");
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
}
