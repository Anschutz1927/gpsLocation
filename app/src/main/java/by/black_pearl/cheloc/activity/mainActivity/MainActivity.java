package by.black_pearl.cheloc.activity.mainActivity;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.activity.scrollActivity.ScrollActivity;
import by.black_pearl.cheloc.fragments.SetPosFragment;
import by.black_pearl.cheloc.fragments.StartFragment;
import by.black_pearl.cheloc.location.LocationListener;
import by.black_pearl.cheloc.location.service.ChelocService;
import by.black_pearl.cheloc.location.service.ServiceBinder;

public class MainActivity extends AppCompatActivity {
    private final static String LOG_TAG = "ChelocMainActivity";
    public final static int CHANGE_TO_SETPOSFRAGMENT = 0;
    public final static int CHANGE_TO_STARTFRAGMENT = 1;
    private ChelocService chelocService;
    private boolean bound;
    private LocationListener locationListener;
    private int build;
    private static int sAddSize = 5;

    public MainActivity() {
        Log.i(LOG_TAG, "MainActivity");
        this.bound = false;
        this.build = 69;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.locationListener = new LocationListener(this);
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
        tabResizer((Button) findViewById(R.id.tabOnceButton), null);
    }

    @Override
    public void onBackPressed() {
        Log.i(LOG_TAG, "onBackPressed");
        if ((findViewById(R.id.startLayout)).getVisibility() == View.VISIBLE) {
            Dialogs.showExitDialog(MainActivity.this);
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
        try {
            ((TextView)findViewById(R.id.verTextView))
                    .setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionName
                            + "build" + this.build);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        findViewById(R.id.startLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.setPosScrollLayout).setVisibility(View.GONE);
    }

    public void tabResizer(Button button1, @Nullable Button button2) {
        int size_width = button1.getWidth();
        int size_height = button1.getHeight();
        button1.setWidth(size_width + sAddSize);
        button1.setHeight(size_height + sAddSize);
        if (button2 != null) {
            size_width = button1.getWidth();
            size_height = button1.getHeight();
            button2.setWidth(size_width - sAddSize);
            button2.setHeight(size_height - sAddSize);
        }
    }

    private void setOnClickListeners() {
        ButtonClickListener listener = new ButtonClickListener(MainActivity.this);
        findViewById(R.id.exitButton).setOnClickListener(listener);
        findViewById(R.id.toSetPosButton).setOnClickListener(listener);
        findViewById(R.id.savePosButon).setOnClickListener(listener);
        findViewById(R.id.loadPosButton).setOnClickListener(listener);
        findViewById(R.id.setPosWithoutUpdatesButton).setOnClickListener(listener);
        findViewById(R.id.setPosWithUpdatesButton).setOnClickListener(listener);
        findViewById(R.id.cancelSetLocationButton).setOnClickListener(listener);
        findViewById(R.id.stopMockLocationButton).setOnClickListener(listener);
    }

    public void fragmentChanger(int id) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (id) {
            case CHANGE_TO_STARTFRAGMENT:
                ft.replace(R.id.mainActivityLyaout, StartFragment.newInstance());
                break;
            case CHANGE_TO_SETPOSFRAGMENT:
                ft.replace(R.id.mainActivityLyaout, SetPosFragment.newInstance());
                break;
        }
        ft.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            ButtonClickListener.setSetPosScrollLayout(this,
                    data.getExtras().getString(ScrollActivity.EXTRA_LATITUDE),
                    data.getExtras().getString(ScrollActivity.EXTRA_LONGTITUDE),
                    data.getExtras().getString(ScrollActivity.EXTRA_ALTITUDE));
        }
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
