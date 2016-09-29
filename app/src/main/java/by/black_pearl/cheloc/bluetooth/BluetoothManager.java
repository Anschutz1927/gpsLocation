package by.black_pearl.cheloc.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

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
    private BluetoothAdapter bluetoothAdapter;
    private BtClientsListAdapter btClientsListAdapter;

    public BluetoothManager(Activity activity) {
        this.activity = activity;
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
                    Log.i(LOG_TAG, " \nmReceiver:onRecive");
                    Log.i(LOG_TAG, "bt device address = " + device.getAddress());
                    Log.i(LOG_TAG, "bt device name = " + device.getName());
                    btClientsListAdapter.add(device.getName(), device.getAddress());
                }
            }
        };
    }

    public boolean checkBtAccess() {
        if(bluetoothAdapter == null) {
            Toast.makeText(activity, "Не удалось обнаружть bluetooth!", Toast.LENGTH_SHORT).show();
            activity.finish();
        }
        if(!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            Toast.makeText(activity, "bluetooth выключен!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void printKnownBtDevicesToLog() {
        Log.i(LOG_TAG, "bluetooth is enabled\n");
        Log.i(LOG_TAG, "bt device address = " + bluetoothAdapter.getAddress());
        Log.i(LOG_TAG, "bt device name = " + bluetoothAdapter.getName());
        Log.i(LOG_TAG, "bt device state = " + bluetoothAdapter.getState());
        Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
        if(pairedDevice.size() > 0) {
            for (BluetoothDevice device : pairedDevice) {
                Log.i(LOG_TAG, "pairedDevice " + device.getName());
            }
        }
    }

    public void fillDevicesToListView(ListView listView) {
        btClientsListAdapter = new BtClientsListAdapter(activity.getBaseContext(),
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
        this.activity.registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    public void unregisterReceiver() {
        this.activity.unregisterReceiver(receiver);
    }

    public void startSearchBtDevices() {
        this.bluetoothAdapter.startDiscovery();
    }

    public void stopSearchBtDevices() {
        if(bluetoothAdapter.isDiscovering()) {
            this.bluetoothAdapter.cancelDiscovery();
        }
    }

    public void startServerToAccept() {

    }
}
