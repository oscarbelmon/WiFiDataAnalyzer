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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;

import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Slider mySlider;
    @FXML
    private Label numberWAPs;

    @FXML
    private BarChart<String, Number> readingsChart;
    @FXML
    private CategoryAxis waps;
    private Readings readings;
    private MetaData metaData;

    @FXML
    private RadioButton kitchen;
    @FXML
    private RadioButton diningroom;
    @FXML
    private RadioButton balcony;
    @FXML
    private RadioButton bedroom1;
    @FXML
    private RadioButton bedroom2;
    @FXML
    private RadioButton bedroom3;
    @FXML
    private RadioButton corridor;
    @FXML
    private RadioButton drawingroom;
    @FXML
    private RadioButton wc1;
    @FXML
    private RadioButton wc2;

    @FXML
    private Image houseImage;

    private Map<String, XYChart.Series> seriesMap = new HashMap<>();
    private Measures measures;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
         //Añade un escuchador al Slider, usando para ello el método
        // addListener al que se le pasa un ChangeListener
        // Lo que se hace es cambiar el valor del diagrama mediante el valor que tiene
        // el slider en cada momento, llamando al método.
        mySlider.valueProperty().addListener((a, b, c) -> {
//            numberWAPs.setText(readings.getReadingsGreeterOrEqualTo(c.intValue()).getNumberOfReadings() + "");
            updateChartWithIntensity(c.intValue());
        });
    }

    // Actualiza la vista con el entero que se le pasa.
    private void updateChartWithIntensity(int intensity) {
        // Clase Readings -> Conjuntos de lecturas, que pueden instanciar otra clase Readings, que es otro conjunto.
        Readings readingsTotal;
        Readings readingsIntensity;
        Readings readingsIntensityWAP;

        // Enumeración
        Room room;
        for(String roomString: seriesMap.keySet()) {
            // Obtiene el subconjunto de lecturas para las habitaciones seleccionadas
            room = Room.valueOf(roomString);
            readingsTotal = measures.getVisibleReadings().getVisibleReadingByRoom(room);
            readingsIntensity = readingsTotal.getReadingsGreeterOrEqualTo(intensity);

            // Actualiza todas las WAP para la habitación seleccionada, modificando la ventana
            XYChart.Series series = seriesMap.get(room.toString());
            float intensityTotal;
            float intensityLevel;
            int result;
            for(int i = 0; i < metaData.getNumberOfMacs(); i++) {
                intensityTotal = readingsTotal.getVisibleReadingsByWAP(metaData.getWAPByIndex(i)).getNumberOfReadings();
                intensityLevel = readingsIntensity.getVisibleReadingsByWAP(metaData.getWAPByIndex(i)).getNumberOfReadings();
                result = (int)(intensityLevel/intensityTotal*100);
                XYChart.Data<String, Number> data = (XYChart.Data<String, Number>)series.getData().get(i);
                data.setYValue(result);
            }
        }
    }

    private void updateChart(Room room) {
        if(seriesMap.containsKey(room.toString()) == false) {
            Readings readingsRoom = readings.getVisibleReadingByRoom(room);
            if(seriesMap.containsKey("ALL")) setSeries(readingsRoom, room.toString());
            else addSeries(readingsRoom, room.toString());
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        RadioButton source = (RadioButton)(event.getSource());
        String room = source.getId().toUpperCase();
        if(source.isSelected() == true) {
            updateChart(Room.valueOf(room));
        } else if(source.isSelected() == false){
            removeSeries(Room.valueOf(room));
        }
    }

    private void removeSeries(Room room) {
        // Elimina todos los datos de la habitación del diagrama
        readingsChart.getData().removeAll(seriesMap.get(room.toString()));
        // Elimina la habitación del HashMap de series
        seriesMap.remove(room.toString());
    }

    private XYChart.Series<String, Number> seriesMaxIntensity(Readings readingsRoom, String label) {
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName(label);
        String categoryName;
        Number categoryData;
        for(int i = 0; i < metaData.getNumberOfMacs(); i++) {
            categoryData = readingsRoom.getVisibleReadingsByWAP(metaData.getWAPByIndex(i)).getNumberOfReadings();
            categoryName = metaData.getWAPByIndex(i).getName();
            serie.getData().add(new XYChart.Data<>(categoryName, categoryData));
        }
        seriesMap.put(label, serie);
        return serie;
    }

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
//            categoryData = readingsRoom.getVisibleReadingsByWAP(metaData.getWAPByIndex(i)).getNumberOfReadings();
            percentage = (int)(categoryData.floatValue()/totalData.floatValue()*100);
            categoryName = metaData.getWAPByIndex(i).getName();
//            serie.getData().add(new XYChart.Data<>(categoryName, categoryData));
            serie.getData().add(new XYChart.Data<>(categoryName, percentage));
        }
        seriesMap.put(label, serie);
        return serie;
    }

    private XYChart.Series<String, Number> seriesWithIntensity(Readings readingsRoom, String label, int intensity) {
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        String categoryName;
        Number categoryData;
        for(int i = 0; i < metaData.getNumberOfMacs(); i++) {
            categoryData = readingsRoom.getVisibleReadingsByWAP(metaData.getWAPByIndex(i)).getNumberOfReadings();
            categoryName = metaData.getWAPByIndex(i).getName();
            serie.getData().add(new XYChart.Data<>(categoryName, categoryData));
        }
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
}
