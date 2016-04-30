package by.black_pearl.cheloc;

import android.annotation.TargetApi;
import android.content.Context;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.List;

public class MockLocationProvider {
    private String networkProvider;
    private String gpsProvider;
    private LocationManager mocLocationManager;
    private MainActivity mainActivity;;

    public MockLocationProvider(Context context) {
        try {
            this.networkProvider = LocationManager.NETWORK_PROVIDER;
            this.gpsProvider = LocationManager.GPS_PROVIDER;
            this.mainActivity = (MainActivity) context;

            mocLocationManager = (LocationManager)
                    context.getSystemService(Context.LOCATION_SERVICE);
            mainActivity.addLogToConsole("\n -mockLocationManager created.");
            mocLocationManager.addTestProvider(networkProvider, false, false, false, false,
                    false, true, true, Criteria.POWER_LOW, Criteria.ACCURACY_FINE);
            mainActivity.addLogToConsole("\n -added network test provider.");
            mocLocationManager.addTestProvider(gpsProvider, false, false, false, false,
                    false, true, true, Criteria.POWER_LOW, Criteria.ACCURACY_FINE);
            mainActivity.addLogToConsole("\n -added gps test provider.");

            mocLocationManager.setTestProviderEnabled(networkProvider, true);
            mocLocationManager.setTestProviderEnabled(gpsProvider, true);
            mocLocationManager.setTestProviderStatus(gpsProvider, LocationProvider.AVAILABLE,
                    null, System.currentTimeMillis());
            mainActivity.addLogToConsole("\n -test providers enabled.");

        }
        catch (Exception e) {
            mainActivity.addLogToConsole("\n -EXEPTION -test provider has not created: " + e.getMessage());
        }

    }

    public void setLocation(double lat, double lon, int alt){
        try {
            Location mocNetworkLocation = new Location(this.networkProvider);
            Location mocGpsLocation = new Location(this.gpsProvider);

            mocNetworkLocation.setLatitude(lat + 0.001);
            mocNetworkLocation.setLongitude(lon + 0.001);
            mocNetworkLocation.setAltitude(alt + 0.001);

            mocGpsLocation.setLatitude(lat);
            mocGpsLocation.setLongitude(lon);
            mocGpsLocation.setAltitude(alt);

            try {
                Method locationJellyBeanFixMethod = Location.class.getMethod("makeComplete");
                if (locationJellyBeanFixMethod != null) {
                    locationJellyBeanFixMethod.invoke(mocNetworkLocation);
                    locationJellyBeanFixMethod.invoke(mocGpsLocation);
                }
            } catch (Exception e) {
                // There's no action to take here.  This is a fix for Jelly Bean and no reason to report a failure.
            }

            mocNetworkLocation.setTime(System.currentTimeMillis());
            mocGpsLocation.setTime(System.currentTimeMillis());

            try {
                this.mocLocationManager.setTestProviderLocation(gpsProvider, mocGpsLocation);
                mainActivity.addLogToConsole("\n -gps test provider setted.");
            } catch (Exception e) {
                mainActivity.addLogToConsole("\n -EXEPTION - error set GPS test provider: " + e.getMessage());
            }

            try {
                this.mocLocationManager.setTestProviderLocation(networkProvider, mocNetworkLocation);
                mainActivity.addLogToConsole("\n -network test provider setted.");
            }
            catch (Exception e) {
                mainActivity.addLogToConsole("\n -EXEPTION - error set network test provider: " + e.getMessage());
            }
        }
        catch (Exception e) {
            mainActivity.addLogToConsole("\n -EXEPTION - set location error: " + e.getMessage());
        }
    }

    public void cancelSetLocation() {
        try{
            this.mocLocationManager.removeTestProvider(networkProvider);
            mainActivity.addLogToConsole("\n -network provider canceled.");
            this.mocLocationManager.removeTestProvider(gpsProvider);
            mainActivity.addLogToConsole("\n -gps provider canceled.");
        }
        catch (Exception e) {
            mainActivity.addLogToConsole("\n -EXEPTION - cancel test provider error: " + e.getMessage());
        }
    }

}
