package by.black_pearl.cheloc.location;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.util.Log;

import java.lang.reflect.Method;

import by.black_pearl.cheloc.activity.ConsoleLinearLayout;
import by.black_pearl.cheloc.activity.MainActivity;

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
            ConsoleLinearLayout.addLineToConsole(mainActivity, "MockLocationManager created.");
            mocLocationManager.addTestProvider(networkProvider, false, false, false, false,
                    false, true, true, Criteria.POWER_LOW, Criteria.ACCURACY_FINE);
            ConsoleLinearLayout.addLineToConsole(mainActivity, "Added network test provider.");
            mocLocationManager.addTestProvider(gpsProvider, false, false, false, false,
                    false, true, true, Criteria.POWER_LOW, Criteria.ACCURACY_FINE);
            ConsoleLinearLayout.addLineToConsole(mainActivity, "Added gps test provider.");

            mocLocationManager.setTestProviderEnabled(networkProvider, true);
            mocLocationManager.setTestProviderEnabled(gpsProvider, true);
            mocLocationManager.setTestProviderStatus(gpsProvider, LocationProvider.AVAILABLE,
                    null, System.currentTimeMillis());
            ConsoleLinearLayout.addLineToConsole(mainActivity, "Test providers enabled.");

        }
        catch (Exception e) {
            ConsoleLinearLayout.addLineToConsole(mainActivity, "EXEPTION -test provider has not created: "
                    + e.getMessage());
        }

    }

    public void setLocation(double lat, double lon, double alt, double speed){
        try {
            Location mocNetworkLocation = new Location(this.networkProvider);
            Location mocGpsLocation = new Location(this.gpsProvider);

            mocNetworkLocation.setLatitude(lat + 0.001);
            mocNetworkLocation.setLongitude(lon + 0.001);
            mocNetworkLocation.setAltitude(alt + 0.001);
            mocNetworkLocation.setSpeed((float) speed);

            mocGpsLocation.setLatitude(lat);
            mocGpsLocation.setLongitude(lon);
            mocGpsLocation.setAltitude(alt);
            mocGpsLocation.setSpeed((float) speed);

            try {
                Method locationJellyBeanFixMethod = Location.class.getMethod("makeComplete");
                if (locationJellyBeanFixMethod != null) {
                    locationJellyBeanFixMethod.invoke(mocNetworkLocation);
                    locationJellyBeanFixMethod.invoke(mocGpsLocation);
                }
            } catch (Exception e) {
                Log.i("<<<__MLP__>>>", e.getMessage());
            }

            mocNetworkLocation.setTime(System.currentTimeMillis());
            mocGpsLocation.setTime(System.currentTimeMillis());

            try {
                this.mocLocationManager.setTestProviderLocation(gpsProvider, mocGpsLocation);
                ConsoleLinearLayout.addLineToConsole(mainActivity, "Gps test provider setted.");
            } catch (Exception e) {
                ConsoleLinearLayout.addLineToConsole(mainActivity,
                        "EXEPTION - error set GPS test provider: " + e.getMessage());
            }

            try {
                this.mocLocationManager.setTestProviderLocation(networkProvider, mocNetworkLocation);
                ConsoleLinearLayout.addLineToConsole(mainActivity, "Network test provider setted.");
            }
            catch (Exception e) {
                ConsoleLinearLayout.addLineToConsole(mainActivity,
                        "EXEPTION - error set network test provider: " + e.getMessage());
            }
        }
        catch (Exception e) {
            ConsoleLinearLayout.addLineToConsole(mainActivity,
                    "EXEPTION - set location error: " + e.getMessage());
        }
    }

    public void cancelSetLocation() {
        try{
            this.mocLocationManager.removeTestProvider(networkProvider);
            ConsoleLinearLayout.addLineToConsole(mainActivity, "Network provider canceled.");
            this.mocLocationManager.removeTestProvider(gpsProvider);
            ConsoleLinearLayout.addLineToConsole(mainActivity, "Gps provider canceled.");
        }
        catch (Exception e) {
            ConsoleLinearLayout.addLineToConsole(mainActivity,
                    "EXEPTION - cancel test provider error: " + e.getMessage());
        }
    }

}
