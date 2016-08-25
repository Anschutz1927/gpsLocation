package by.black_pearl.cheloc.location;

import android.os.Handler;
import android.util.Log;

/**
 * This class provide access to run code for set location.
 */
public class LocationRunnable implements Runnable {
    private String LOG_TAG = "LocationRunnable";
    private Coordinates coordinates;
    private Handler handler;
    private LocationProvider locationProvider;
    private boolean withUpdates;
    private int updateTime = 799;

    public LocationRunnable(Coordinates coordinates, Handler handler,
                            boolean withUpdates, LocationProvider locationProvider) {
        Log.i(LOG_TAG, "LocationRunnable");
        this.coordinates = coordinates;
        this.handler = handler;
        this.withUpdates = withUpdates;
        this.locationProvider = locationProvider;
    }

    public void setNewCoordinates(Coordinates newCoordinates, boolean withUpdates) {
        Log.i(LOG_TAG, "setNewCoordinates");
        this.coordinates = newCoordinates;
        this.withUpdates = withUpdates;
    }

    @Override
    public void run() {
        Log.i(LOG_TAG, "run");
        locationProvider.setLocation(coordinates);
        if(withUpdates) {
            handler.postDelayed(this, updateTime);
        }
    }
}
