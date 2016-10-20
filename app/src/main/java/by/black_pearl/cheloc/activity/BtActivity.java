package by.black_pearl.cheloc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.bluetooth.BluetoothManager;
import by.black_pearl.cheloc.bluetooth.BtProcessListener;


public class BtActivity extends AppCompatActivity {
    private static final int KEY_EXIST = 0;
    private static final int KEY_ACCESS = 1;
    private static final int KEY_NOT_ENABLED = 2;
    private static final String LOG_TAG = "BtActivity";
    public static final String INTENT_SERVER_MODE = "IntentServerMode";
    public static final String INTENT_EXTRA_STRING_ARRAYLIST = "ExtraStrings";
    private BluetoothManager mBluetoothManager;
    private boolean mBtIsEnabled = false;
    private boolean mIsServerMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate()");
        setContentView(R.layout.activity_bt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialize();
        checker();
    }

    /**
     * Check to work vith BT.
     */
    private void checker() {
        Log.i(LOG_TAG, "checker()");
        if (!mBluetoothManager.isBtExist()) {
            finisher(BtActivity.KEY_EXIST);
        }
        if (!mBluetoothManager.isBtAccess()) {
            finisher(KEY_ACCESS);
        } else {
            this.mBtIsEnabled = true;
        }
    }

    /**
     * Start initialize.
     */
    private void initialize() {
        Log.i(LOG_TAG, "initialize()");
        mBluetoothManager = new BluetoothManager(this);
        this.mIsServerMode = getIntent().getBooleanExtra(BtActivity.INTENT_SERVER_MODE, false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart()");
        if (!mIsServerMode) {
            ListView listViewBtDevices = new ListView(this);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            listViewBtDevices.setLayoutParams(params);
            mBluetoothManager.fillDevicesToListView(listViewBtDevices);
            addListener(listViewBtDevices);
            ((LinearLayout) findViewById(R.id.content_bt)).addView(listViewBtDevices);
        }
    }

    private void addListener(ListView listViewBtDevices) {
        listViewBtDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String address = ((android.widget.TextView) view
                        .findViewById(R.id.addressListLayoutTextView)).getText().toString();
                mBluetoothManager.connectAndSend(
                        address,
                        getIntent().getStringArrayListExtra(BtActivity.INTENT_EXTRA_STRING_ARRAYLIST),
                        (ProgressBar) findViewById(R.id.btProgressBar)
                );
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume()");
        if (!this.mBtIsEnabled) return;
        if (this.mIsServerMode) {
            mBluetoothManager.startServerToAccept(
                    (LinearLayout) findViewById(R.id.content_bt),
                    getBtProcessListener()
            );
            setOnClickListener(0);
        } else {
            mBluetoothManager.registerReceiver();
            setOnClickListener(2);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause()");
        if (this.mIsServerMode) {
            mBluetoothManager.stopServer();
        } else {
            mBluetoothManager.unregisterReceiver();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy()");
    }

    private void setOnClickListener(int key) {
        switch (key) {
            case 0:
                View.OnClickListener listener = getDefaultListener();
                ((FloatingActionButton) findViewById(R.id.fab)).setImageResource(android.R.drawable.ic_menu_save);
                findViewById(R.id.fab).setOnClickListener(listener);
                break;
            case 1:
                mBluetoothManager.setDataSaveButton((FloatingActionButton) findViewById(R.id.fab));
                break;
            case 2:
                ((FloatingActionButton) findViewById(R.id.fab)).setImageResource(android.R.drawable.ic_menu_search);
                findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBluetoothManager.startSearchBtDevices();
                    }
                });
                break;
        }
    }

    private View.OnClickListener getDefaultListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
    }

    private BtProcessListener getBtProcessListener() {
        return new BtProcessListener() {
            @Override
            public void sendComplete() {
                Handler h = new Handler(getMainLooper());
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BtActivity.this, "Complete!", Toast.LENGTH_SHORT).show();
                        setOnClickListener(1);
                    }
                });
                //Toast.makeText(BtActivity.this, "Complete!", Toast.LENGTH_SHORT).show();
                //setOnClickListener(1);
            }

            @Override
            public void finishApp() {
                finish();
            }
        };
    }

    /**
     * Method to finish this activity viwth bt exception.
     *
     * @param key
     */
    private void finisher(int key) {
        Log.i(LOG_TAG, "finisher()");
        switch (key) {
            case KEY_EXIST:
                Toast.makeText(this, "Не удалось обнаружть bluetooth!", Toast.LENGTH_SHORT).show();
                break;
            case KEY_ACCESS:
                Toast.makeText(this, "Bluetooth выключен!", Toast.LENGTH_SHORT).show();
                return;
            case KEY_NOT_ENABLED:
                Toast.makeText(this, "Bluetooth не был включен!", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "The method 'finisher' has stopped bt activity.", Toast.LENGTH_SHORT).show();
                break;
        }
        this.mBtIsEnabled = false;
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(LOG_TAG, "onActivityResult()");
        switch (requestCode) {
            case BluetoothManager.REQUEST_ENABLE_BT:
                if (resultCode != RESULT_OK) {
                    finisher(KEY_NOT_ENABLED);
                } else {
                    this.mBtIsEnabled = true;
                }
                break;
        }
    }
}
