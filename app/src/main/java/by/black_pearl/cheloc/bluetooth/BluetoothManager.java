package by.black_pearl.cheloc.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**
 * Manager to work with BT to sharing data;
 */
public class BluetoothManager {
    private static final String LOG_TAG = "BluetoothManager";
    public static final int REQUEST_ENABLE_BT = 1;
    public static final String BT_NAME = "Cheloc";
    public static UUID UUID_APP = UUID.fromString("5e12e650-80bd-11e6-bdf4-0800200c9a66");
    private final Activity activity;
    private final BroadcastReceiver receiver;
    private final Context mContext;
    private BluetoothAdapter bluetoothAdapter;
    private BtClientsListAdapter btClientsListAdapter;
    private BTServer btServer;
    private BTClient btClient;

    public BluetoothManager(Activity activity) {
        Log.i(LOG_TAG, "BluetoothManager()");
        this.activity = activity;
        this.mContext = activity.getBaseContext();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.receiver = initializeReciver();
    }

    private BroadcastReceiver initializeReciver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    btClientsListAdapter.add(device.getName(), device.getAddress());
                }
            }
        };
    }

    /**
     * Return false when device is not have bt.
     */
    public boolean isBtExist() {
        Log.i(LOG_TAG, "isBtExist()");
        return bluetoothAdapter != null;

    }

    /**
     * Return true if bt is enabled or is sending request to turn on bt.
     */
    public boolean isBtAccess() {
        Log.i(LOG_TAG, "checkBtAccess()");
        if(!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return false;
        }
        return true;
    }

    public void fillDevicesToListView(ListView listView) {
        Log.i(LOG_TAG, "fillDevicesToListView()");
        btClientsListAdapter = new BtClientsListAdapter(mContext,
                new ArrayList<HashMap<String, String>>());
        Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
        if(pairedDevice.size() > 0) {
            for (BluetoothDevice device : pairedDevice) {
                btClientsListAdapter.add(device.getName(), device.getAddress());
            }
        }
        listView.setAdapter(btClientsListAdapter);
    }

    public void registerReceiver() {
        Log.i(LOG_TAG, "registerReceiver()");
        this.activity.registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    public void unregisterReceiver() {
        Log.i(LOG_TAG, "unregisterReceiver()");
        this.activity.unregisterReceiver(receiver);
    }

    public void startSearchBtDevices() {
        Log.i(LOG_TAG, "startSearchBtDevices()");
        this.bluetoothAdapter.startDiscovery();
    }

    public void stopSearchBtDevices() {
        Log.i(LOG_TAG, "stopSearchBtDevices()");
        if(bluetoothAdapter.isDiscovering()) {
            this.bluetoothAdapter.cancelDiscovery();
        }
    }

    public void startServerToAccept(LinearLayout layout, BtProcessListener btProcessListener) {
        Log.i(LOG_TAG, "startServerToAccept():");
        if (btServer == null) {
            Log.i(LOG_TAG, "new btServer");
            btServer = new BTServer(bluetoothAdapter, layout, btProcessListener, mContext);
        }
        btServer.startServer();
    }

    public void stopServer() {
        Log.i(LOG_TAG, "stopServer()");
        if (btServer != null) {
            btServer.stopServer();
        }
    }

    public void connectAndSend(String address, ArrayList<String> stringArrayListExtra, ProgressBar progressBar) {
        Log.i(LOG_TAG, "connectToDevice");
        btClient = new BTClient(bluetoothAdapter, mContext, progressBar);
        btClient.connectAndSend(address, stringArrayListExtra);
    }

    public void setDataSaveButton(FloatingActionButton fab) {
        if (btServer != null) {
            btServer.setDataSaveButton(fab);
        }
    }
}
