package by.black_pearl.cheloc;

import android.support.annotation.Nullable;

/**
 * Created by BLACK_Pearl.
 */

public class ViewSetupOnceModel {
    private boolean disableEmulationGps;
    private boolean disableGps;
    private Double latitude;
    private Double longtitude;
    private Double altitude;
    private int time;

    public ViewSetupOnceModel(boolean disableEmulationGps,
                              boolean disableGps, @Nullable Double latitude,
                              @Nullable Double longtitude, @Nullable Double altitude, int time) {
        this.disableEmulationGps = disableEmulationGps;
        this.disableGps = disableGps;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.altitude = altitude;
        this.time = time;
    }

    public boolean isDisableEmulationGps() {
        return disableEmulationGps;
    }

    public boolean isDisableGps() {
        return disableGps;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public int getTime() {
        return time;
    }
}
