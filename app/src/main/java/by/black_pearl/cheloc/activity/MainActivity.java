package by.black_pearl.cheloc.activity;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import by.black_pearl.cheloc.DataBaser;
import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.location.LocationListener;
import by.black_pearl.cheloc.location.service.ChelocService;
import by.black_pearl.cheloc.location.service.ServiceBinder;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = "ChelocMainActivity";
    private ChelocService chelocService;
    private boolean bound;
    private LocationListener locationListener;
    private DataBaser dataBaser;

    public MainActivity() {
        Log.i(LOG_TAG, "MainActivity");
        this.bound = false;
        Log.i(LOG_TAG, "ver57");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.locationListener = new LocationListener(this);
        this.dataBaser = new DataBaser(this);
        setOnClickListeners();
        setProperties();
    }

    @Override
    protected void onStart() {
        Log.i(LOG_TAG, "onStart");
        super.onStart();
        bindService(new Intent(MainActivity.this, ChelocService.class),
                serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        Log.i(LOG_TAG, "onResume");
        super.onResume();
        locationListener.setRequestLocationUpdates();
    }

    @Override
    public void onBackPressed() {
        Log.i(LOG_TAG, "onBackPressed");
        if ((findViewById(R.id.startLayout)).getVisibility() == View.VISIBLE) {
            ExitDialog.showExitDialog(MainActivity.this);
        } else {
            (findViewById(R.id.startLayout)).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        Log.i(LOG_TAG, "onPause");
        super.onPause();
        locationListener.removeRequestLocationUpdates();
    }

    @Override
    protected void onStop() {
        Log.i(LOG_TAG, "onStop");
        super.onStop();
        if(bound) {
            Log.i(LOG_TAG, "unbindService");
            unbindService(serviceConnection);
            setBound(false);
        }
    }

    @Override
    public void onDestroy() {
        Log.i(LOG_TAG, "onDestroy");
        super.onDestroy();
    }

    public void setBound(boolean b) {
        this.bound = b;
        Log.i(LOG_TAG, "setBound = " + String.valueOf(bound));
    }

    public boolean getBound() {
        Log.i(LOG_TAG, "getBound = " + String.valueOf(bound));
        return this.bound;
    }

    public ChelocService getChelocService() {
        Log.i(LOG_TAG, "getChelocService");
        return this.chelocService;
    }

    public void setServiceStatusOnTextView() {
        Log.i(LOG_TAG, "setServiceStatusOnTextView");
        if(this.chelocService.isWork()) {
            ((TextView) (findViewById(R.id.serviceStatusTextView))).setText(R.string.serviceRun);
        }
        else {
            ((TextView) (findViewById(R.id.serviceStatusTextView))).setText(R.string.serviceStop);
        }
    }

    private void setProperties() {

    }

    private void setOnClickListeners() {
        ButtonClickListener listener = new ButtonClickListener(MainActivity.this);
        findViewById(R.id.exitButton).setOnClickListener(listener);
        findViewById(R.id.toSetPosButton).setOnClickListener(listener);
        findViewById(R.id.setPosWithoutUpdatesButton).setOnClickListener(listener);
        findViewById(R.id.setPosWithUpdatesButton).setOnClickListener(listener);
        findViewById(R.id.cancelSetLocationButton).setOnClickListener(listener);
        findViewById(R.id.stopMockLocationButton).setOnClickListener(listener);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(LOG_TAG, "onServiceConnected");
            chelocService = ((ServiceBinder) service).getService();
            setBound(true);
            setServiceStatusOnTextView();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(LOG_TAG, "onServiceDisconnected");
            setBound(false);
        }
    };
}
