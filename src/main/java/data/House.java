package data;

import com.google.gson.stream.JsonReader;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.Polygon;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

    String imageUri;

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
            if (!rooms.contains(room))
                rooms.add(room);
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
     * Devuelve la dirección de la imagen asociada a la casa.
     */
    public String getImageDir() {
        return new String(imageUri);
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
    public boolean loadHouseFromJsonFile(final String jsonFile) {
        //TODO Refactorizar
        try {
            FileReader file = new FileReader(jsonFile);
            JsonReader reader = new JsonReader(file);

            // Salto la primera parte del documento (id y title)
            reader.beginObject();

            for(int i = 0; i < 2; i++) {
                reader.nextName();
                reader.nextString();
            }

            reader.nextName();
            reader.beginObject();
            reader.nextName();

            // Extrae y añade la imagen de la casa
            imageUri = reader.nextString();

            reader.endObject();

            // Etiqueta geo -> Comienzo a crear los polígonos
            reader.nextName();
            reader.beginObject();

            reader.nextName();
            reader.nextString();
            reader.nextName();

            // Array features (featureCollection)
            // Cada iteración del siguiente bucle -> Lee e introduce una habitación y le asocia su polígono
            reader.beginArray();

            while (reader.hasNext()) {
                reader.beginObject();
                reader.nextName();
                reader.nextString();
                reader.nextName();
                reader.beginObject();
                reader.endObject();
                reader.nextName();
                // Habitación
                String room = reader.nextString();
                this.addNewRoom(room);

                // Objeto geometry -> Puntos del polígono
                reader.nextName();
                reader.beginObject();
                reader.nextName();
                reader.nextString();
                reader.nextName();
                // Coordenadas del objeto
                reader.beginArray();
                reader.beginArray();

                Polygon associated = new Polygon();

                while (reader.hasNext()) {
                    reader.beginArray();
                    int x = reader.nextInt();
                    int y = reader.nextInt();
                    associated.addPoint(x, y);
                    reader.endArray();
                }

                this.addRoomPolygon(room, associated);

                reader.endArray();
                reader.endArray();
                reader.endObject();
                reader.endObject();
            }

            // Termino de leer los polígonos y no me interesa más información -> Cierro
            reader.close();
            return true;
        }
        catch (IOException f) {
            f.printStackTrace();
            return false;
        }
    }
}
