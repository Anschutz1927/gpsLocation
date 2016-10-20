package by.black_pearl.cheloc.activity.bluetoothActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.activity.BtActivity;
import by.black_pearl.cheloc.bluetooth.BluetoothManager;

public class BluetoothActivity extends Activity {
    private final static String LOG_TAG = "BluetoothActivity";
    public final static String BLUETOOTH_EXTRA_STRING_ARRAYLIST = "extrastrings";
    public final static String ActivityMode = "ActivityMode";
    public final static int ACTIVITY_MODE_SENDER = 0;
    public final static int ACTIVITY_MODE_SERVER = 1;
    private int currentActivityMode;
    private BluetoothManager bluetoothManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate");
        setContentView(R.layout.activity_bluetooth);
        bluetoothManager = new BluetoothManager(this);
        currentActivityMode = getIntent().getIntExtra(BluetoothActivity.ActivityMode, 0);
        initialize();
        startActivity(new Intent(this, BtActivity.class));
    }

    private void initialize() {
        switch (currentActivityMode) {
            case BluetoothActivity.ACTIVITY_MODE_SENDER:
                findViewById(R.id.btListView).setVisibility(View.VISIBLE);
                ArrayList<String> arrayList = getIntent().getStringArrayListExtra(BluetoothActivity
                        .BLUETOOTH_EXTRA_STRING_ARRAYLIST);
                setListeners(arrayList);
                ((ProgressBar) findViewById(R.id.btProgressBar)).setMax(arrayList.size() - 1);
                ((ProgressBar) findViewById(R.id.btProgressBar)).setIndeterminate(false);
                break;
            case BluetoothActivity.ACTIVITY_MODE_SERVER:
                findViewById(R.id.btListView).setVisibility(View.GONE);
                break;
        }
    }

    private void setListeners(final ArrayList<String> arrayList) {
        ((ListView)findViewById(R.id.btListView)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String address = ((TextView)view.findViewById(R.id.addressListLayoutTextView)).getText().toString();
                Log.i(LOG_TAG, "textFromClickedItem = ");
                Log.i(LOG_TAG, "\n" +
                        ((TextView)view.findViewById(R.id.nameListLayoutTextView)).getText().toString() +
                        ", " + address + "\n");
                bluetoothManager.connectAndSend(address, arrayList, (ProgressBar) findViewById(R.id.btProgressBar));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart");
        //if(bluetoothManager.checkBtAccess() &&
        //        currentActivityMode == BluetoothActivity.ACTIVITY_MODE_SENDER) {
        //    bluetoothManager.fillDevicesToListView((ListView) findViewById(R.id.btListView));
        //}
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
        switch (currentActivityMode) {
            case BluetoothActivity.ACTIVITY_MODE_SENDER:
                bluetoothManager.registerReceiver();
                bluetoothManager.startSearchBtDevices();
                break;
            case BluetoothActivity.ACTIVITY_MODE_SERVER:
                //bluetoothManager.startServerToAccept(findViewById(R.id.content_bt));
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause");
        switch (currentActivityMode) {
            case BluetoothActivity.ACTIVITY_MODE_SENDER:
                bluetoothManager.stopSearchBtDevices();
                bluetoothManager.unregisterReceiver();
                break;
            case BluetoothActivity.ACTIVITY_MODE_SERVER:
                //bluetoothManager.startServerToAccept();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        bluetoothManager.stopServer();
        Log.i(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(LOG_TAG, "onActivityResult");
        switch (requestCode) {
            case BluetoothManager.REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK) {
                    // bluetoothManager.printKnownBtDevicesToLog();
                }
                else {
                    Toast.makeText(this, "bluetooth не был включен!", Toast.LENGTH_SHORT).show();
                    this.finish();
                }
                break;
        }
    }
}
