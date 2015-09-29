package mvc;

import data.Readings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable, ChangeListener<Number> {
    @FXML
    private Slider mySlider;
    @FXML
    private Label numberWAPs;
    @FXML
    private ComboBox<String> roomCombo;
    private Readings readings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mySlider.valueProperty().addListener((a, b, c) -> {
            numberWAPs.setText(readings.getReadingsGreeterOrEqualTo(c.intValue()).getNumberOfReadings() + "");
        });

        populateRoomCombo();
    }

    private void populateRoomCombo() {
//            numberWAPs.setText(readings.getReadingsGreeterOrEqualTo(c.intValue()).getNumberOfReadings() + "");
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
}
