package by.black_pearl.cheloc;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.util.Set;

/**
 * Manager to work with BT to sharing data;
 */
public class BluetoothManager {
    private static final String LOG_TAG = "BluetoothManager";

    public BluetoothManager() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter != null) {
            Log.i(LOG_TAG, "bluetooth workaet.\n");
            return;
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
}
