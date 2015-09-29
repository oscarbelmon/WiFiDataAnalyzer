package mvc;

import data.MetaData;
import data.Reading;
import data.Readings;
import data.Room;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class Controller implements Initializable, ChangeListener<Number> {
    @FXML
    private Slider mySlider;
    @FXML
    private Label numberWAPs;
    @FXML
    private ComboBox<String> roomCombo;
    @FXML
    private BarChart<String, Number> readingsChart;
    @FXML
    private CategoryAxis waps;
    private Readings readings;
    private MetaData metaData;
    private Set<Room> roomSet = new HashSet<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mySlider.valueProperty().addListener((a, b, c) -> {
            numberWAPs.setText(readings.getReadingsGreeterOrEqualTo(c.intValue()).getNumberOfReadings() + "");
        });

        populateRoomCombo();

        roomCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
            updateChart(Room.valueOf(newValue));
        });
    }

    private void updateChart(Room room) {
        if(roomSet.contains(room) == false) {
            Readings readingsRoom = readings.getVisibleReadingByRoom(room);
            if(roomSet.isEmpty()) setSeries(readingsRoom, room.toString());
            else addSeries(readingsRoom, room.toString());
            roomSet.add(room);
        }
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
        return serie;
    }

    private void addSeries(Readings readings, String label) {
        readingsChart.getData().addAll(newSeries(readings, label));
    }

    private void setSeries(Readings readings, String label) {
        readingsChart.getData().setAll(newSeries(readings, label));
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

    private void populateRoomCombo() {
        for(Room room: Room.values())
            roomCombo.getItems().add(room.toString());
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
