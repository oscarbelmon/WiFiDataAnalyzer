package data;

/**
 * Created by oscar on 26/09/15.
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
