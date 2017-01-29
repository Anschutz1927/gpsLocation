package by.black_pearl.cheloc.location;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;

import by.black_pearl.cheloc.ViewSetupOnceModel;

/**
 * Provide access to set new location.
 */
public class LocationProvider {
    private String LOG_TAG = "LocationProvider";
    private boolean isMockNetworkProvider;
    private LocationManager locationManager;
    private Context context;
    private Handler handler;
    private LocationRunnable locationRunnable;

    public LocationProvider(Context context, boolean isMockNetworkProvider) {
        Log.i(LOG_TAG, "LocationProvider");
        this.isMockNetworkProvider = isMockNetworkProvider;
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        addTestProviders();
    }

    private boolean addTestProviders() {
        Log.i(LOG_TAG, "addTestProviders");
        try {
            locationManager.addTestProvider(LocationManager.GPS_PROVIDER, false, false, false, false,
                    false, true, true, Criteria.POWER_LOW, Criteria.ACCURACY_FINE);
            locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
            locationManager.setTestProviderStatus(LocationManager.GPS_PROVIDER,
                    android.location.LocationProvider.AVAILABLE, null, System.currentTimeMillis());

        }
        catch (Exception e) {
            Toast.makeText(context, "Ошибка настройки положения по спутникам!",
                    Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, e.getMessage());
            return false;
        }
        if(isMockNetworkProvider) {
            try {
                locationManager.addTestProvider(LocationManager.NETWORK_PROVIDER, false, false, false, false,
                        false, true, true, Criteria.POWER_LOW, Criteria.ACCURACY_FINE);
                locationManager.setTestProviderEnabled(LocationManager.NETWORK_PROVIDER, true);
                locationManager.setTestProviderStatus(LocationManager.NETWORK_PROVIDER,
                        android.location.LocationProvider.AVAILABLE, null, System.currentTimeMillis());
            } catch (Exception e) {
                Toast.makeText(context, "Ошибка настройки положения по сети!", Toast.LENGTH_SHORT).show();
                Log.i(LOG_TAG, e.getMessage());
                isMockNetworkProvider = false;
                return false;
            }
        }
        return true;
    }

    protected void setLocation(Coordinates coordinates) {
        Log.i(LOG_TAG, "setLocation(coord)");
        try {
            if(isMockNetworkProvider) {
                Location mocNetworkLocation = new Location(LocationManager.NETWORK_PROVIDER);
                mocNetworkLocation.setLatitude(coordinates.getSettedLat() + 0.0010352419);
                mocNetworkLocation.setLongitude(coordinates.getSettedLon() + 0.001324563);
                mocNetworkLocation.setAltitude(coordinates.getSettedAlt() + 0.001213836);
                try {
                    Method locationJellyBeanFixMethod = Location.class.getMethod("makeComplete");
                    if (locationJellyBeanFixMethod != null) {
                        locationJellyBeanFixMethod.invoke(mocNetworkLocation);
                    }
                } catch (Exception e) {
                    Log.i(LOG_TAG, e.getMessage());
                }
                mocNetworkLocation.setTime(System.currentTimeMillis());
                try {
                    this.locationManager.setTestProviderLocation(LocationManager.NETWORK_PROVIDER,
                            mocNetworkLocation);
                    Log.i(LOG_TAG, "Network location setted.");
                    Log.i(LOG_TAG, mocNetworkLocation.getLatitude() + ", " +
                            mocNetworkLocation.getLongitude() + ", " +
                            mocNetworkLocation.getAltitude() + ", " +
                            mocNetworkLocation.getSpeed() + ", " +
                            mocNetworkLocation.getBearing() + ".");
                }
                catch (Exception e) {
                    Log.i(LOG_TAG, "Network location not setted!");
                    Log.i(LOG_TAG, e.getMessage());
                    Toast.makeText(context,
                            "Ошибка установки положения по координатам сети! Проверьте настройки.",
                            Toast.LENGTH_LONG).show();
                    this.isMockNetworkProvider = false;
                }
            }
            Location mocGpsLocation = new Location(LocationManager.GPS_PROVIDER);
            mocGpsLocation.setLatitude(coordinates.getLat());
            mocGpsLocation.setLongitude(coordinates.getLon());
            mocGpsLocation.setAltitude(coordinates.getAlt());
            mocGpsLocation.setSpeed((float) coordinates.getSpeed());
            mocGpsLocation.setBearing((float) coordinates.getBearing());
            try {
                Method locationJellyBeanFixMethod = Location.class.getMethod("makeComplete");
                if (locationJellyBeanFixMethod != null) {
                    locationJellyBeanFixMethod.invoke(mocGpsLocation);
                }
            }
            catch (Exception e) {
                Log.i(LOG_TAG, e.getMessage());
            }
            mocGpsLocation.setTime(System.currentTimeMillis());
            try {
                this.locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER,
                        mocGpsLocation);
                Log.i(LOG_TAG, "GPS location setted!");
                Log.i(LOG_TAG, mocGpsLocation.getLatitude() + ", " +
                        mocGpsLocation.getLongitude() + ", " +
                        mocGpsLocation.getAltitude() + ", " +
                        mocGpsLocation.getSpeed() + ", " +
                        mocGpsLocation.getBearing() + ".");
            }
            catch (Exception e) {
                Log.i(LOG_TAG, "GPS location not setted!");
                Log.i(LOG_TAG, e.getMessage());
                Toast.makeText(context,
                        "Ошибка установки положения по координатам спутников! " +
                                "Координаты не установлены.",
                        Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            Log.i(LOG_TAG, "Set location error!");
            Log.i(LOG_TAG, e.getMessage());
        }
    }

    public void setLocation(Coordinates coordinates, boolean withUpdates) {
        Log.i(LOG_TAG, "setLocation(coord, upd)");
        this.handler = new Handler();
        this.locationRunnable = new LocationRunnable(coordinates, handler, withUpdates, this);
        handler.postDelayed(this.locationRunnable, 10000);
    }

    public void stopSetLocation() {
        Log.i(LOG_TAG, "stopSetLocation");
        if(handler != null) {
            this.handler.removeCallbacks(locationRunnable);
        }
        deleteTestProviders();
    }

    public void changeLocation(Coordinates coordinates, boolean withUpdates) {
        Log.i(LOG_TAG, "changeLocation");
        locationRunnable.setNewCoordinates(coordinates, withUpdates);
    }

    private void deleteTestProviders() {
        Log.i(LOG_TAG, "deleteTestProviders");
        try {
            if (isMockNetworkProvider) {
                locationManager.removeTestProvider(LocationManager.NETWORK_PROVIDER);
            }
            locationManager.removeTestProvider(LocationManager.GPS_PROVIDER);
        }
        catch (Exception e) {
            Log.i(LOG_TAG, e.getMessage());
        }
    }

    public void runManagerMockLocation(final ArrayList<ViewSetupOnceModel> listModels) {
        final Handler managerHandler = new Handler();
        final Runnable managerRunnable = new Runnable() {
            private boolean isStarted = false;
            private int n = 0;
            @Override
            public void run() {
                if(listModels.get(n).isDisableEmulationGps() && listModels.get(n).isDisableGps()) {
                    if(listModels.get(n).isDisableEmulationGps()) {
                        Toast.makeText(context, "DisableEmulationGps", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context, "DisableGps", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(!isStarted) {
                        isStarted = true;
                        setLocation(getCoordinates(listModels.get(n)), true);
                    }
                    else {
                        changeLocation(getCoordinates(listModels.get(n)), true);
                    }

                }
                n++;
                if(n < listModels.size()) {
                    managerHandler.postDelayed(this, listModels.get(n - 1).getTime() * 1000 * 60);
                }
                else {
                    stopSetLocation();
                }
            }

            private Coordinates getCoordinates(ViewSetupOnceModel viewSetupOnceModel) {
                return new Coordinates(
                        viewSetupOnceModel.getLatitude(),
                        viewSetupOnceModel.getLongtitude(),
                        viewSetupOnceModel.getAltitude(),
                        0.0f,
                        0,
                        true
                );
            }
        };
        handler.post(managerRunnable);
    }
}
