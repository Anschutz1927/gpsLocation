package by.black_pearl.cheloc.location;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import by.black_pearl.cheloc.RunnableInfo;

public class SetterMockLocationService extends Service {
    private final String logTag = "<<<_SMLS_>>>";
    private Handler handlerSetter;
    private Handler handlerViewer;
    private RunnableInfo runnableInfo;
    private RunnableGPS runnableGPS;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        Log.i(logTag, "onCreate()");
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(logTag, "onStartCommand(...)");
        Coordinates coordinates = new Coordinates(intent.getExtras().getDouble("lat", 0.0),
                intent.getExtras().getDouble("lon", 0.0), intent.getExtras().getDouble("alt", 0),
                intent.getExtras().getDouble("speed", 1.2));
        handlerSetter = new Handler(); //for updates location
        handlerViewer = new Handler(); //for updates info messages
        runnableGPS = new RunnableGPS(this.handlerSetter, getBaseContext(), coordinates);
        runnableInfo = new RunnableInfo(this.handlerViewer, getBaseContext(), runnableGPS);
        handlerSetter.postDelayed(runnableGPS, 10000);
        handlerViewer.postDelayed(runnableInfo, 60000);
        Toast.makeText(getBaseContext(), "Set location started...", Toast.LENGTH_SHORT).show();
        return START_REDELIVER_INTENT; // super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i(logTag, "onDestroy()");
        Toast.makeText(getBaseContext(), "Set location stopped...", Toast.LENGTH_SHORT).show();
        handlerSetter.removeCallbacks(runnableGPS);
        handlerViewer.removeCallbacks(runnableInfo);
        runnableGPS.stopMockLocation();
        stopSelf();
    }
}
