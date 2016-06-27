package by.black_pearl.cheloc.location;

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
            this.locationManager.addTestProvider(LocationManager.GPS_PROVIDER, false, false, false, false,
                    false, true, true, Criteria.POWER_MEDIUM, Criteria.ACCURACY_FINE);
            this.locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
            this.locationManager.setTestProviderStatus(LocationManager.GPS_PROVIDER, LocationProvider.AVAILABLE,
                    null, System.currentTimeMillis());
        }
        catch (Exception e) {
            Log.i(logTag, e.getMessage());
        }
    }

    public boolean setMockLocation(Coordinates coordinates) {
        try {
            Location location = new Location(LocationManager.GPS_PROVIDER);
            double lat = coordinates.getLat();
            double lon = coordinates.getLon();
            double alt = coordinates.getAlt();
            double bearing = coordinates.getBearing();
            double speed = coordinates.getSpeed();
            location.setLatitude(lat);
            location.setLongitude(lon);
            location.setAltitude(alt);
            location.setBearing((float) bearing);
            location.setSpeed((float) speed);



            try {
                Method locationJellyBeanFixMethod = Location.class.getMethod("makeComplete");
                if (locationJellyBeanFixMethod != null) {
                    locationJellyBeanFixMethod.invoke(location);
                }
            } catch (Exception ignored) {}

            location.setTime(System.currentTimeMillis());
            this.locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, location);
            Log.i(logTag, "Location updated (" + lat + ", " + lon + ", " + alt + ", " + speed + ").");
            return true;
        }
        catch (Exception e) {
            Log.i(logTag, e.getMessage());
            return false;
        }
    }

    public boolean removeTestProviders() {
        try {
            this.locationManager.removeTestProvider(LocationManager.GPS_PROVIDER);
            this.locationManager.removeTestProvider(LocationManager.NETWORK_PROVIDER);
            return true;
        }
        catch (Exception e) {
            Log.i(logTag, e.getMessage());
            return false;
        }
    }
}
