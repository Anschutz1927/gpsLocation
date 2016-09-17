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
import android.widget.TextView;

import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.activity.mainActivity.MainActivity;

import static java.lang.String.valueOf;

public class LocationListener implements android.location.LocationListener {
    private String LOG_TAG = "LocationListener";
    private MainActivity mainActivity;
    private TextView networkLatitude;
    private TextView networkLongtitude;
    private TextView networkAltitude;
    private TextView gpsLatitude;
    private TextView gpsLongtitude;
    private TextView gpsAltitude;
    private TextView gpsBearing;
    private TextView gpsSpeed;
    private TextView gpsSatellites;

    public LocationListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.networkLatitude = (TextView) mainActivity.findViewById(R.id.latNetworkLocationTextView);
        this.networkLongtitude = (TextView) mainActivity.findViewById(R.id.longNetworkLocationTextView);
        this.networkAltitude = (TextView) mainActivity.findViewById(R.id.altNetworkLocationTextView);
        this.gpsLatitude = (TextView) mainActivity.findViewById(R.id.latGpsLocationTextView);
        this.gpsLongtitude = (TextView) mainActivity.findViewById(R.id.longGpsLocationTextView);
        this.gpsAltitude = (TextView) mainActivity.findViewById(R.id.altGpsLocationTextView);
        this.gpsBearing = (TextView) mainActivity.findViewById(R.id.brnGpsLocationTextView);
        this.gpsSpeed = (TextView) mainActivity.findViewById(R.id.spdGpsLocationTextView);
        this.gpsSatellites = (TextView) mainActivity.findViewById(R.id.stlGpsLocationTextView);
    }

    private void getLastKnownLocation(Location location) {
        if(checkPermission(mainActivity)) {
            if(location == null) {
                return;
            }
            long diffTime = System.currentTimeMillis() - location.getTime();
            long allowedTime = 5 * 60 * 1000;
            Log.i(LOG_TAG, "System.currentTimeMillis() = " + System.currentTimeMillis() +
                    " location.getTime() = " + location.getTime() +
                    " diffTime = " + diffTime +
                    " allowedTime = " + allowedTime);
            if(diffTime <= allowedTime) {
                showLocation(location, Color.rgb(225, 160, 160));
            }
        }
    }

    private void showLocation(Location location, int rgb) {
        if(location == null) {
            return;
        }
        if(location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
            this.networkLatitude.setText(valueOf(location.getLatitude()));
            this.networkLatitude.setBackgroundColor(rgb);
            this.networkLongtitude.setText(valueOf(location.getLongitude()));
            this.networkLongtitude.setBackgroundColor(rgb);
            this.networkAltitude.setText(valueOf(location.getAltitude()));
            this.networkAltitude.setBackgroundColor(rgb);
            Log.i(LOG_TAG, "Location from network provider: " +
                    "\nlat = " + location.getLatitude() +
                    "\nlon = " + location.getLongitude());
            try {
                Log.i(LOG_TAG, "\nalt = " + location.getAltitude() +
                        "\nbrn = " + location.getBearing() +
                        "\nspd = " + location.getSpeed());
            }
            catch (Exception e) {
                Log.i(LOG_TAG, e.getMessage());
            }
        }
        else {
            this.gpsLatitude.setText(valueOf(location.getLatitude()));
            this.gpsLatitude.setBackgroundColor(rgb);
            this.gpsLongtitude.setText(valueOf(location.getLongitude()));
            this.gpsLongtitude.setBackgroundColor(rgb);
            this.gpsAltitude.setText(valueOf(location.getAltitude()));
            this.gpsAltitude.setBackgroundColor(rgb);
            this.gpsBearing.setText(valueOf(location.getBearing()));
            this.gpsSpeed.setText(valueOf(location.getSpeed()));
            try {
                this.gpsSatellites.setText(valueOf(location.getExtras().getInt("satellites", -1)));
            }
            catch (Exception e) {
                Log.i(LOG_TAG, "No satellites: " + e.getMessage());
            }
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
                    (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);
            if (checkPermission(mainActivity)) {
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
                    (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);
            if (checkPermission(mainActivity)) {
                locationManager.removeUpdates(this);
            }
        }
        catch (Exception e) {
            Log.i(LOG_TAG, e.getMessage());
        }
    }

    private static boolean checkPermission(MainActivity mainActivity) {
        return !(ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onLocationChanged(Location location) {
        showLocation(location, Color.rgb(190, 210, 220));
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
}
