package by.black_pearl.cheloc;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.Set;

/**
 * Manager to work with BT to sharing data;
 */
public class BluetoothManager {
    private static final String LOG_TAG = "BluetoothManager";
    private Activity activity;
    private final BroadcastReceiver receiver;
    private BluetoothAdapter bluetoothAdapter;

    public BluetoothManager(Activity activity) {
        this.activity = activity;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        printKnownBtDevicesToLog();
        this.receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.i(LOG_TAG, " \nmReceiver:onRecive");
                    Log.i(LOG_TAG, "bt device address = " + device.getAddress());
                    Log.i(LOG_TAG, "bt device name = " + device.getName());
                }
            }
        };
        registerReceiver();
    }

    private void printKnownBtDevicesToLog() {
        if(bluetoothAdapter != null) {
            Log.i(LOG_TAG, "bluetooth workaet...\n");
            return;
        }
        else {
            Log.i(LOG_TAG, "bluetooth NE workaet...\n");
        }
        if(bluetoothAdapter.isEnabled()) {
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
        else {
            Log.i(LOG_TAG, "bluetooth is NOT enabled");
            return;
        }
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.activity.registerReceiver(receiver, filter);
    }

    public void unregisterReceiver() {
        this.activity.unregisterReceiver(receiver);
    }

    public void startSearchBtDevices() {
        this.bluetoothAdapter.startDiscovery();
    }

    public void stopSearchBtDevices() {
        this.bluetoothAdapter.cancelDiscovery();
    }
}
