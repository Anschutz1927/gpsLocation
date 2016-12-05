package by.black_pearl.cheloc.activity.mainActivity;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.fragments.SetPosFragment;
import by.black_pearl.cheloc.fragments.StartFragment;
import by.black_pearl.cheloc.location.CoordinatesForExtra;
import by.black_pearl.cheloc.location.LocationListener;
import by.black_pearl.cheloc.location.service.ChelocService;
import by.black_pearl.cheloc.location.service.ServiceBinder;

public class MainActivity extends AppCompatActivity {
    private final static String LOG_TAG = "ChelocMainActivity";
    public final static int CHANGE_TO_SETPOSFRAGMENT = 0;
    public final static int CHANGE_TO_STARTFRAGMENT = 1;
    private ChelocService mChelocService;
    private boolean mBound;
    private LocationListener mLocationListener;
    private boolean isStarted = true;

    public MainActivity() {
        Log.i(LOG_TAG, "MainActivity");
        this.mBound = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mLocationListener = new LocationListener(this);
    }

    @Override
    protected void onStart() {
        Log.i(LOG_TAG, "onStart");
        super.onStart();
        bindService(new Intent(MainActivity.this, ChelocService.class),
                serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.isStarted = savedInstanceState.getBoolean(LOG_TAG, true);
    }

    @Override
    protected void onResume() {
        Log.i(LOG_TAG, "onResume");
        super.onResume();
        mLocationListener.setRequestLocationUpdates();
    }

    @Override
    protected void onResumeFragments() {
        Log.i(LOG_TAG, "onResumeFragments");
        super.onResumeFragments();
        if (isStarted) {
            fragmentChanger(CHANGE_TO_STARTFRAGMENT, null);
        }
    }

    @Override
    public void onBackPressed() {
        Log.i(LOG_TAG, "onBackPressed");
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        Log.i(LOG_TAG, "onPause");
        super.onPause();
        mLocationListener.removeRequestLocationUpdates();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean(LOG_TAG, this.isStarted);
    }

    @Override
    protected void onStop() {
        Log.i(LOG_TAG, "onStop");
        super.onStop();
        if (mBound) {
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

    private void setBound(boolean b) {
        this.mBound = b;
        Log.i(LOG_TAG, "setBound = " + String.valueOf(mBound));
    }

    public boolean getBound() {
        Log.i(LOG_TAG, "getBound = " + String.valueOf(mBound));
        return this.mBound;
    }

    public ChelocService getChelocService() {
        Log.i(LOG_TAG, "getChelocService");
        return this.mChelocService;
    }

    public LocationListener getLocationListener() {
        return this.mLocationListener;
    }

    public void fragmentChanger(int changeTo, @Nullable CoordinatesForExtra coordinatesForExtra) {
        this.isStarted = false;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (changeTo) {
            case CHANGE_TO_STARTFRAGMENT:
                ft.replace(R.id.mainActivityLyaout, StartFragment.newInstance());
                break;
            case CHANGE_TO_SETPOSFRAGMENT:
                ft.replace(R.id.mainActivityLyaout, SetPosFragment.newInstance(coordinatesForExtra));
                ft.addToBackStack("setpos");
                break;
        }
        ft.commit();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(LOG_TAG, "onServiceConnected");
            mChelocService = ((ServiceBinder) service).getService();
            setBound(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(LOG_TAG, "onServiceDisconnected");
            setBound(false);
        }
    };

}
