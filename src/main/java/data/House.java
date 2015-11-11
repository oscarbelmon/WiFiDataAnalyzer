package data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import com.google.gson.JsonParser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.Polygon;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Created by daniel on 28/10/15.
 */
public class House {

    private HashMap<String, Polygon> house = new HashMap();
    private HashSet<String> rooms = new HashSet();
    private String[] stringRooms;
    @FXML
    private ImageView houseImage;

    public House() {}

    public boolean addRoomPolygon(String room, Polygon polygon) {
        if (polygon != null) {
            house.put(room, polygon);
            return true;
        }
        return false;
    }

    public boolean addNewRoom(String room) {
        if (room != null) {
            boolean added = rooms.add(room);
            if (added) {
                Object[] roomsInSet = rooms.toArray();
                stringRooms = new String[roomsInSet.length];
                for (int i = 0; i < roomsInSet.length; i++) {
                    stringRooms[i] = (String) roomsInSet[i];
                }
            }
        }
        return false;
    }

    public Polygon getRoom(String room) { return house.get(room); }

    public String[] getRoomsNames() { return stringRooms; }

    public int numberOfRooms() {
        return house.size();
    }

    public ImageView getHouseImage() {
        return houseImage;
    }


    public boolean setHouseImage(ImageView house) {
        if (house != null) {
            houseImage = house;
            return true;
        }
        return false;
    }

    public void loadHouseFromJsonFile(String jsonFile) throws FileNotFoundException, InvalidPropertiesFormatException, IllegalArgumentException {
        // Carga fichero JSON de la casa
        JsonParser parser = new JsonParser();
        Object parsedFile = parser.parse(new FileReader(jsonFile));
        JsonObject houseLoaded = (JsonObject) parsedFile;

        // Carga parámetros de la casa
        String houseMapDir = houseLoaded.get("imageSource").getAsString();
        Image houseImage = new Image(houseMapDir);
        JsonArray jsonRoomsArray = houseLoaded.getAsJsonArray("rooms");
        HashSet<String> rooms = new HashSet();
        HashMap<String, Polygon> house = new HashMap();

        // Recorro todas las habitaciones del fichero
        // comprobando que no hayan dos habitaciones iguales y que el nº de puntos sea correcto.
        Iterator<JsonElement> roomsIterator = jsonRoomsArray.iterator();
        for (int i = 0; i < jsonRoomsArray.size(); i++) {
            JsonArray room = roomsIterator.next().getAsJsonArray();
            //TODO comprobar si funciona
            String roomName = room.getAsString();
            if (!rooms.add(roomName)) {
                throw new InvalidPropertiesFormatException("The names of all the rooms must be different.\n" + "Failed in: " + roomName);
            }

            JsonArray roomXPoints = room.get(0).getAsJsonArray();
            JsonArray roomYPoints = room.get(1).getAsJsonArray();
            int[] xPoints = new int[roomXPoints.size()];
            int[] yPoints = new int[roomYPoints.size()];

            if (xPoints.length != yPoints.length) {
                throw new InvalidPropertiesFormatException("The number of points in x must be equal to the number of points in y.\n" + "Failed in: " + roomName);
            }

            Iterator<JsonElement> pointsXIterator = roomXPoints.iterator();
            Iterator<JsonElement> pointsYIterator = roomYPoints.iterator();
            for (int j = 0; i < xPoints.length; i++) {
                xPoints[j] = pointsXIterator.next().getAsInt();
                yPoints[j] = pointsYIterator.next().getAsInt();

            }

            Polygon roomPolygon = new Polygon(xPoints, yPoints, xPoints.length);
            house.put(roomName, roomPolygon);
        }
        // Si el cargado ha sido correcto:
        this.house = house;
        this.rooms = rooms;
        this.houseImage = new ImageView();
        this.houseImage.setImage(houseImage);
    }
}
