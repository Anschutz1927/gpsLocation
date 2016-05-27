package by.black_pearl.cheloc.location;

import java.util.Random;

public class Coordinates {
    private double settedLat;
    private double settedLon;
    private int settedAlt;
    private double Lat;
    private double Lon;
    private double Alt;
    private int count;

    public Coordinates(double lat, double lon, int alt) {
        this.settedLat = lat;
        this.settedLon = lon;
        this.settedAlt = alt;
        this.count = 0;
        leapCoordinates();
    }

    public double getLat() {
        if(count == 1000) {
            this.count = 0;
            this.leapCoordinates();
        }
        else {
            this.count++;
        }
        return this.Lat;
    }

    public double getLon() {
        return this.Lon;
    }

    public double getAlt() {
        return this.Alt;
    }

    private void leapCoordinates() {
        Random random = new Random();
        this.Lat = this.settedLat + (random.nextInt(100) - 50) / 1000000.;
        this.Lon = this.settedLon + (random.nextInt(100) - 50) / 1000000.;
        this.Alt = this.settedAlt + (random.nextInt(100) - 50) / 1000000.;
    }
}
