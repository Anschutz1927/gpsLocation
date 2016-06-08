package by.black_pearl.cheloc.location;

import java.util.Random;

public class Coordinates {
    Random random = new Random();
    private double settedLat;
    private double settedLon;
    private double settedAlt;
    private double settedSpeed;
    private double Lat;
    private double Lon;
    private double Alt;
    private double Speed;
    private int count;

    public Coordinates(double lat, double lon, double alt, double speed) {
        this.settedLat = lat;
        this.settedLon = lon;
        this.settedAlt = alt;
        this.settedSpeed = speed;
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

    public double getSpeed() {
        leapSpeed();
        return this.Speed;
    }

    private void leapCoordinates() {
        this.Lat = this.settedLat + (random.nextInt(100) - 50) / 1000000.;
        this.Lon = this.settedLon + (random.nextInt(100) - 50) / 1000000.;
        this.Alt = this.settedAlt + (random.nextInt(100) - 50) / 1000000.;
    }

    private void leapSpeed() {
        if(this.count % 100 == 0) {
            this.Speed = this.settedSpeed + (random.nextInt(16) - 11) / 10.;
        }
    }
}
