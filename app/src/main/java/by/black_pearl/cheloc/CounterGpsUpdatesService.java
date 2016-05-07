package by.black_pearl.cheloc;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.security.Provider;

public class CounterGpsUpdatesService extends Service implements LocationListener {
    private int count;
    private Handler handler;
    private Runnable runnable;
    private LocationManager locationManager;

    public CounterGpsUpdatesService() {
        count = 0;
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 6000);
                toastCountViewer(count);
            }
        };
        handler.postDelayed(runnable, 6000);
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        catch (Exception e) {
            stopSelf();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return 0;
            }
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, this);
        }
        catch (Exception e) {
            Toast.makeText(getBaseContext(), "passive provider failed ;(", Toast.LENGTH_SHORT).show();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void onCreate() {
        super.onCreate();
        Toast.makeText(getBaseContext(), "Service gpsUpdate started", Toast.LENGTH_SHORT).show();
    }

    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getBaseContext(), "Service gpsUpdate stopped", Toast.LENGTH_SHORT).show();
        handler.removeCallbacks(runnable);
    }

    private void toastCountViewer(int count) {
        this.toastViewer("GpsUpdtsPrMin = " + String.valueOf(count));
    }

    private void toastViewer (String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        count++;
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
