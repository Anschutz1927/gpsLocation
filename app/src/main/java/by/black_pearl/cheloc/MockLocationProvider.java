package by.black_pearl.cheloc;

import android.annotation.TargetApi;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;

import java.lang.reflect.Method;

public class MockLocationProvider {
    private String networkProvider;
    private String gpsProvider;
    LocationManager mocLocationManager;

    public MockLocationProvider(Context context) {
        this.networkProvider = LocationManager.NETWORK_PROVIDER;
        this.gpsProvider = LocationManager.GPS_PROVIDER;

        mocLocationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
        mocLocationManager.addTestProvider(networkProvider, false, false, false, false,
                false, true, true, Criteria.POWER_LOW, Criteria.ACCURACY_FINE);
        mocLocationManager.addTestProvider(gpsProvider, false, false, false, false,
                false, true, true, Criteria.POWER_LOW, Criteria.ACCURACY_FINE);

        mocLocationManager.setTestProviderEnabled(networkProvider, true);
        mocLocationManager.setTestProviderEnabled(gpsProvider, true);


    }

    public void setLocation(double lat, double lon, int alt) {
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
        this.mocLocationManager.setTestProviderLocation(networkProvider, mocNetworkLocation);
        this.mocLocationManager.setTestProviderLocation(gpsProvider, mocGpsLocation);
    }

    public void cancelSetLocation() {
        try{
            this.mocLocationManager.removeTestProvider(networkProvider);
            this.mocLocationManager.removeTestProvider(gpsProvider);
        }
        catch (Exception e) {
            //
        }
    }

}
