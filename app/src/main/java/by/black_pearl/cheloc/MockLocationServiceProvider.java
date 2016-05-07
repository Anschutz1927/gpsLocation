package by.black_pearl.cheloc;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.util.Log;

import java.lang.reflect.Method;

public class MockLocationServiceProvider {
    private LocationManager locationManager;
    private  final String logTag = "<<<_MLSP_>>>";

    public MockLocationServiceProvider(Context context) {
        try {
            this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            //gps test provider
            this.locationManager.clearTestProviderEnabled(LocationManager.GPS_PROVIDER);
            this.locationManager.addTestProvider(LocationManager.GPS_PROVIDER, false, true, false, false,
                    true, true, true, Criteria.POWER_MEDIUM, Criteria.ACCURACY_FINE);
            this.locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
            this.locationManager.setTestProviderStatus(LocationManager.GPS_PROVIDER, LocationProvider.AVAILABLE,
                    null, System.currentTimeMillis());
        }
        catch (Exception e) {
            Log.i(logTag, e.getMessage());
        }
    }

    public boolean setMockLocation(double lat, double lon, int alt) {
        try {
            Location location = new Location(LocationManager.GPS_PROVIDER);
            location.setLatitude(lat);
            location.setLongitude(lon);
            location.setAltitude(alt);

            try {
                Method locationJellyBeanFixMethod = Location.class.getMethod("makeComplete");
                if (locationJellyBeanFixMethod != null) {
                    locationJellyBeanFixMethod.invoke(location);
                }
            } catch (Exception ignored) {}

            location.setTime(System.currentTimeMillis());
            location.setTime(System.currentTimeMillis());
            this.locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, location);
            Log.i(logTag, "Location updated.");
            return true;
        }
        catch (Exception e) {
            Log.i(logTag, e.getMessage());
            return false;
        }
    }

    public boolean removeTestProviders() {
        try {
            this.locationManager.clearTestProviderEnabled(LocationManager.GPS_PROVIDER);
            return true;
        }
        catch (Exception e) {
            Log.i(logTag, e.getMessage());
            return false;
        }
    }
}
