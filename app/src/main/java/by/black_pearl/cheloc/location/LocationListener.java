package by.black_pearl.cheloc.location;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class LocationListener implements android.location.LocationListener {
    private String LOG_TAG = "LocationListener";
    private Context mContext;
    private OnLocationChanged mListener = null;

    public LocationListener(Context context) {
        this.mContext = context;
    }

    public Location getLastKnownLocation() {
        LocationManager locationManager =
                (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (checkPermission()) {
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return null;
    }

    public void setRequestLocationUpdates() {
        try {
            LocationManager locationManager =
                    (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            if (checkPermission()) {
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
        return !(ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                        != PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            return;
        }
        mListener.onLocationChanged(location);
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

    public void registerOnLocationChangedListener(OnLocationChanged listener) {
        this.mListener = listener;
    }

    public void removeOnLocationChangedListener() {
        this.mListener = null;
    }

    public interface OnLocationChanged {
        void onLocationChanged(@Nullable Location location);
    }
}
