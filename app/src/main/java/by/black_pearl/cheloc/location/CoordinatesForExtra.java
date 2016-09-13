package by.black_pearl.cheloc.location;

/**
 * .
 */
public class CoordinatesForExtra {
    private String latitude;
    private String longtitude;
    private String altitude;

    public CoordinatesForExtra(String latitude, String longtitude, String altitude) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.altitude = altitude;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public String getAltitude() {
        return altitude;
    }
}
