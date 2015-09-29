package mvc;

import data.MetaData;
import data.Readings;
import data.Room;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Initializable, ChangeListener<Number> {
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

    private Map<String, XYChart.Series> seriesMap = new HashMap<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mySlider.valueProperty().addListener((a, b, c) -> {
            numberWAPs.setText(readings.getReadingsGreeterOrEqualTo(c.intValue()).getNumberOfReadings() + "");
        });
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
        readingsChart.getData().removeAll(seriesMap.get(room.toString()));
        seriesMap.remove(room.toString());
    }

    private XYChart.Series<String, Number> newSeries(Readings readingsRoom, String label) {
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

    private void addSeries(Readings readings, String label) {
        XYChart.Series series = newSeries(readings, label);
        readingsChart.getData().addAll(series);
        seriesMap.put(label, series);
    }

    private void setSeries(Readings readings, String label) {
        XYChart.Series series = newSeries(readings, label);
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

    private void escuchador(Number a, Number b, Number c) {
        System.out.println(c);
    }

    @FXML
    public void holaPulsado() {
        System.out.println("Pulsado");
    }

    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

    }

    public void setReadings(Readings readings) {
        this.readings = readings;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
        populateChart();
    }
}
