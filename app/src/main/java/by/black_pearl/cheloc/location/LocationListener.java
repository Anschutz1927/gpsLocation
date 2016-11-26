package by.black_pearl.cheloc.location;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;

public class LocationListener implements android.location.LocationListener {
    private String LOG_TAG = "LocationListener";
    private Context mContext;
    private ArrayList<OnLocationChanged> listeners;

    public LocationListener(Context context) {
        this.mContext = context;
        this.listeners = new ArrayList<>();
    }

    public void addOnLocationChangedListener(OnLocationChanged listener) {
        this.listeners.add(listener);
    }

    private void getLastKnownLocation(Location location) {
        if (location == null) {
            return;
        }
        long diffTime = System.currentTimeMillis() - location.getTime();
        long allowedTime = 5 * 60 * 1000;
        Log.i(LOG_TAG, "System.currentTimeMillis() = " + System.currentTimeMillis() +
                " location.getTime() = " + location.getTime() +
                " diffTime = " + diffTime +
                " allowedTime = " + allowedTime);
        if (diffTime <= allowedTime) {
            showLocation(location, Color.rgb(225, 160, 160));
        }

    }

    private void showLocation(Location location, int rgb) {
        if (location == null) {
            return;
        }
        if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
            Log.i(LOG_TAG, "Location from network provider: " +
                    "\nlat = " + location.getLatitude() +
                    "\nlon = " + location.getLongitude());
            try {
                Log.i(LOG_TAG, "\nalt = " + location.getAltitude() +
                        "\nbrn = " + location.getBearing() +
                        "\nspd = " + location.getSpeed());
            } catch (Exception e) {
                Log.i(LOG_TAG, e.getMessage());
            }
        } else {
            Log.i(LOG_TAG, "Location from gps provider: " +
                    "\nlat = " + location.getLatitude() +
                    "\nlon = " + location.getLongitude() +
                    "\nalt = " + location.getAltitude() +
                    "\nbrn = " + location.getBearing() +
                    "\nspd = " + location.getSpeed());
        }
    }

    public void setRequestLocationUpdates() {
        try {
            LocationManager locationManager =
                    (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            if (checkPermission()) {
                getLastKnownLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
                //getLastKnownLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
                //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3900, 0, locationListener);
            }
        }
        catch (Exception e) {
            Log.i(LOG_TAG, e.getMessage());
        }
    }

    public void removeRequestLocationUpdates() {
        try {
            LocationManager locationManager =
                    (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            if (checkPermission()) {
                locationManager.removeUpdates(this);
            }
        }
        catch (Exception e) {
            Log.i(LOG_TAG, e.getMessage());
        }
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                        != PackageManager.PERMISSION_GRANTED
                ) {
            return false;
        }
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        //showLocation(location, Color.rgb(190, 210, 220));
        if (this.listeners.size() != 0) {
            for (int i = 0; i < listeners.size(); i++) {
                this.listeners.get(i).onLocationChanged(location);
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    public interface OnLocationChanged {
        void onLocationChanged(Location location);
    }
}
