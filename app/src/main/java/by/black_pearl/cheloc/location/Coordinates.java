package by.black_pearl.cheloc.location;

import java.util.Random;

public class Coordinates {
    Random random = new Random();
    private double settedLat;
    private double settedLon;
    private double settedAlt;
    private double bearing;
    private int settedSpeedMode;
    private boolean randPos;
    private double Lat;
    private double Lon;
    private double Alt;
    private double Speed;
    private int count;

    public Coordinates(double lat, double lon, double alt, double bearing, int speedMode, boolean randPos) {
        this.settedLat = lat;
        this.settedLon = lon;
        this.settedAlt = alt;
        this.bearing = bearing;
        this.settedSpeedMode = speedMode;
        this.randPos = randPos;
        this.count = 0;
        leapCoordinates();
    }

    public double getSettedLat() {
        return settedLat;
    }

    public double getSettedLon() {
        return settedLon;
    }

    public double getSettedAlt() {
        return settedAlt;
    }

    public double getLat() {
        return this.Lat;
    }

    public double getLon() {
        return this.Lon;
    }

    public double getAlt() {
        return this.Alt;
    }

    public double getSpeed() {
        this.leapCoordinates();
        leapSpeed();
        return this.Speed;
    }

    public double getBearing() {
        if(this.bearing == 0.0 && this.settedSpeedMode == 1) {
            Random random = new Random();
            if(random.nextBoolean()) {
                return random.nextInt(330);
            }
        }
        return this.bearing;
    }

    private void leapCoordinates() {
        if(this.randPos && this.count == 100) {
            this.Lat = this.settedLat + ((random.nextInt(100) - 50) / 10000000.) +
                    ((random.nextInt(1000)) / 100000000000.);
            this.Lon = this.settedLon + ((random.nextInt(100) - 50) / 10000000.) +
                    ((random.nextInt(1000)) / 100000000000.);
            this.Alt = this.settedAlt + (random.nextInt(26) - 13);
            count = 0;
        }
        else {
            this.Lat = this.settedLat + (random.nextInt(100) - 50) / 100000000.;
            this.Lon = this.settedLon + (random.nextInt(100) - 50) / 100000000.;
            this.Alt = this.settedAlt + (random.nextInt(26) - 13);
            if(this.randPos) {
                count++;
            }
        }
    }

    private void leapSpeed() {
        if(this.count % 50 == 0 || this.count == 0 || this.count == 10) {
            if(this.settedSpeedMode == 0) {
                this.Speed = 0.0;
            }
            else {
                Random random = new Random();
                switch (this.settedSpeedMode) {
                    case 1:
                        this.Speed = random.nextInt(15) / 10. + random.nextInt(99) / 1000.;
                        break;
                    case 2:
                        this.Speed = random.nextInt(1000) / 1000. + random.nextInt(5) + 11;
                        break;
                }
            }
        }
    }
}
