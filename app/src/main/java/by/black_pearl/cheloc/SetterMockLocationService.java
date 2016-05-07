package by.black_pearl.cheloc;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class SetterMockLocationService extends Service {
    private final String logTag = "<<<_SMLS_>>>";
    private Handler handlerSetter;
    private Handler handlerViewer;
    private Runnable runnableSetter;
    private Runnable runnableViewer;
    private int countSetts;
    private MockLocationServiceProvider mockLocationServiceProvider;

    public SetterMockLocationService() {
        countSetts = 0;
        //mockLocationServiceProvider = new MockLocationServiceProvider(this);
        //for updates location
        handlerSetter = new Handler();
        runnableSetter = new Runnable() {
            @Override
            public void run() {
                //mockLocationServiceProvider.setMockLocation(53.92, 27.59, 200);
                handlerSetter.postDelayed(this, 100);
            }
        };
        handlerSetter.postDelayed(runnableSetter, 100);
        //for updates info messages
        handlerViewer = new Handler();
        runnableViewer = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), "location setted " + countSetts +
                        " times.", Toast.LENGTH_SHORT).show();
                countSetts = 0;
                handlerViewer.postDelayed(this, 60000);
            }
        };
        handlerViewer.postDelayed(runnableViewer, 60000);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        mockLocationServiceProvider = new MockLocationServiceProvider(this);
        mockLocationServiceProvider.setMockLocation(53.92, 27.59, 200);
        Toast.makeText(getBaseContext(), "Set location started...", Toast.LENGTH_SHORT).show();
    }

    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getBaseContext(), "Set location stopped...", Toast.LENGTH_SHORT).show();
        handlerSetter.removeCallbacks(runnableSetter);
        handlerViewer.removeCallbacks(runnableViewer);
        if(mockLocationServiceProvider.removeTestProviders()) {
            Log.i(logTag, "True remove TP.");
        }
        else {
            Log.i(logTag, "False remove TP.");
        }
    }


}
