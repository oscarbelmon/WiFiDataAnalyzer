package data;

import javafx.fxml.FXML;

import javax.swing.text.html.ImageView;
import java.awt.Polygon;
import java.util.TreeMap;

/**
 * Created by daniel on 28/10/15.
 */
public class House {

    private TreeMap<Room, Polygon> house = new TreeMap();

    @FXML
    private ImageView houseImage;

    public House() {}

    /**
     * Añade habitaciones.
     */
    public boolean addRoom(Room room, int[] xCoords, int[] yCoords) {
        if (xCoords.length == yCoords.length) {
            Polygon roomPolygon = new Polygon(xCoords, yCoords, xCoords.length);
            house.put(room, roomPolygon);
            return true;
        }
        return false;
    }

    public boolean addRoom(Room room, Polygon polygon) {
        if (polygon != null) {
            house.put(room, polygon);
            return true;
        }
        return false;
    }


    /**
     * Elimina habitaciones.
     */
    public boolean deleteRoom(Room room) {
        if (house.containsKey(room)) {
            house.remove(room);
            return true;
        }
        return false;
    }

    /**
     * Obtiene datos de la clase.
     */
    public Polygon getRoom(Room room) {
        return house.get(room);
    }

    public int numberOfRooms() {
        return house.size();
    }

    public ImageView getHouseImage() {
        return houseImage;
    }


    /**
     * Modifica datos de la clase.
     */
    public boolean setHouseImage(ImageView house) {
        if (house != null) {
            houseImage = house;
            return true;
        }
        return false;
    }
}
