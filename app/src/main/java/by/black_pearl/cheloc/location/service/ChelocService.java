package by.black_pearl.cheloc.location.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import by.black_pearl.cheloc.ViewSetupOnceModel;
import by.black_pearl.cheloc.location.Coordinates;
import by.black_pearl.cheloc.location.LocationProvider;

public class ChelocService extends Service {

    private static final String FILE_NAME = "tmpManager.txt";
    final String LOG_TAG = "ChelocService";
    private IBinder iBinder = new ServiceBinder(ChelocService.this);
    InfoMessages infoMessages;
    private boolean isWork = false;
    private LocationProvider locationProvider;

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(LOG_TAG, "onBind");
        return iBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(LOG_TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        Log.i(LOG_TAG, "onCreate");
        super.onCreate();
        infoMessages = new InfoMessages(this.getBaseContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_TAG, "onStartCommand");
        setMockLocation(getCoordinates(intent), true);
        this.isWork = true;
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Log.i(LOG_TAG, "onDestroy");
        super.onDestroy();
    }

    public boolean isWork() {
        Log.i(LOG_TAG, "isWork");
        return this.isWork;
    }

    private void setIsWorkToFalse() {
        Log.i(LOG_TAG, "setIsWorkToFalse");
        this.isWork = false;
    }

    public void setMockLocation(Coordinates coordinates, boolean withUpdates) {
        Log.i(LOG_TAG, "setMockLocation");
        this.locationProvider = new LocationProvider(this.getBaseContext(), false);
        this.locationProvider.setLocation(coordinates, withUpdates);
        infoMessages.startingMessages();
    }

    public void changeMockLocation(Coordinates coordinates, boolean withUpdates) {
        Log.i(LOG_TAG, "changeMockLocation");
        if(this.locationProvider != null) {
            this.locationProvider.changeLocation(coordinates, withUpdates);
            infoMessages.changeMessage();
        }
    }

    public void stopMockLocation() {
        Log.i(LOG_TAG, "stopMockLocation");
        if(this.locationProvider != null) {
            this.locationProvider.stopSetLocation();
            this.locationProvider = null;
            setIsWorkToFalse();
            infoMessages.stoppingMessage();
            stopSelf();
        }
    }

    private Coordinates getCoordinates(Intent intent) {
        Log.i(LOG_TAG, "getCoordinates");
        return new Coordinates(
                intent.getDoubleExtra("lat", 0.0),
                intent.getDoubleExtra("lon", 0.0),
                intent.getDoubleExtra("alt", 0.0),
                intent.getDoubleExtra("bearing", 0.0),
                intent.getIntExtra("speedMode", 0),
                intent.getBooleanExtra("randPos", true)
        );
    }

    public void setManagerMockLocation(ArrayList<ViewSetupOnceModel> listModels) {
        if(this.locationProvider != null) {
            this.locationProvider.stopSetLocation();
        }
        else {
            this.locationProvider = new LocationProvider(getBaseContext(), false);
        }
        this.locationProvider.runManagerMockLocation(listModels);
    }
}
