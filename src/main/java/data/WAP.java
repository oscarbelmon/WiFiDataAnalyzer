package data;

/**
 * WAP
 *
 * Almacena los datos de una WAP.
 * - Nombre
 * - MAC
 *
 * Contiene getters para obtener estos datos.
 */
public class WAP {
    private String name;
    private String macAddress;

    public String getName() {
        return name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", macAddress='" + macAddress + '\'' +
                '}';
    }
}
