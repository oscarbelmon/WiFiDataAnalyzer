package data;

import com.google.gson.*;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import java.awt.Polygon;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;

/**
 * House
 *
 * Contiene toda la información de una casa, o cualquier estructura subdividida en
 * habitaciones o áreas. La información que se guarda es:
 *
 * -Habitaciones/Áreas
 * -Imagen asociada (plano).
 * -Polígonos de las habitaciones en la imagen.
 *  Estos polígonos engloban el área de cada habitación.
 *
 *  Se incluyen métodos para trabajar con estos datos.
 */
public class House {

    private HashMap<String, Polygon> house = new HashMap();
    private ArrayList<String> rooms = new ArrayList<>();

    @FXML
    private ImageView houseImage;

    public House() {}

    /**
     * Guarda un polígono asociado a una habitación.
     *
     * @param room - Nombre de la habitación.
     * @param polygon - Polígino asociado.
     * @return - True si ha tenido éxito al añadir. False si no.
     */
    public boolean addRoomPolygon(String room, Polygon polygon) {
        if (polygon != null) {
            house.put(room, polygon);
            return true;
        }
        return false;
    }

    /**
     * Añade una habitación, sin polígono asociado.
     *
     * @param room - Nombre de la habitación.
     * @return - True si lo añade. False si ya existía.
     */
    public boolean addNewRoom(String room) {
        if (room != null)
            return rooms.add(room);
        return false;
    }

    /**
     * Elimina una habitación y sus polígonos asociados.
     *
     * @param room - Identificador de la habitación.
     * @return - True si la elimina. False si no.
     */
    public boolean deleteRoom(String room) {
        if (room != null) {
            if (house.containsKey(room)) {
                house.remove(room);
                rooms.remove(room);
                return true;
            }
        }
        return false;
    }

    /**
     * Obtiene el polígono asociado a una habitación.
     *
     * @param room - Nombre de la habitación.
     * @return - Objeto Polygon asociado.
     */
    public Polygon getRoom(String room) { return house.get(room); }


    /**
     * Obtiene todos los nombres de las habitaciones.
     * @return - Array de Strings, siendo cada elemento una habitación.
     */
    public String[] getRoomsNames() {
        Object[] aux = rooms.toArray();
        String[] roomsNames = new String[aux.length];
        for (int i = 0; i < aux.length; i++) {
            roomsNames[i] = (String) aux[i];
        }
        return roomsNames;
    }


    /**
     * Devuelve el número de habitaciones.
     * @return - Número de habitaciones. (int)
     */
    public int numberOfRooms() {
        return house.size();
    }


    /**
     * Devuelve la imagen asociada a la casa.
     * @return - ImageView asociada.
     */
    public ImageView getHouseImage() {
        return houseImage;
    }


    /**
     * Modifica la imagen de la casa por otra.
     *
     * @param house - ImageView con la imagen ya cargada.
     * @return - True si la cambia. False si no.
     */
    public boolean setHouseImage(ImageView house) {
        if (house != null) {
            houseImage = house;
            return true;
        }
        return false;
    }


    /**
     * Carga todos los datos de la casa a partir de un fichero Json que sigue el formato GeoJson y un esquema correcto.
     * Si se ejcuta con éxito se tendrá una casa con habitaciones, polígonos e imagen asociada.
     *
     * @param jsonFile - Dirección al fichero Json.
     * @throws FileNotFoundException - Si no se encuentra el fichero Json.
     * @throws InvalidPropertiesFormatException - Si el formato del fichero Json no es correcto.
     * @throws IllegalArgumentException - Si el formato de los argumentos en el fichero Json no es correcto.
     * Ej: Número de puntos X distinto al número de puntos Y.
     */
    public void loadHouseFromJsonFile(final String jsonFile) {
        //TODO
        Gson gsonParser = new Gson();

    }
}
