package by.black_pearl.cheloc.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.OutputStream;

/**
 * Client class to accept file from client to server.
 */
public class BTClient {
    private static final String LOG_TAG = "BTClient";
    private BluetoothAdapter bluetoothAdapter;

    public BTClient(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public void connectToDevice(String address) {
        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
        try {
            BluetoothSocket bluetoothSocket = bluetoothDevice
                    .createRfcommSocketToServiceRecord(BluetoothManager.UUID_APP);
            bluetoothSocket.connect();
            this.sender(bluetoothSocket);
        }
        catch (Exception e) {
            Log.i(LOG_TAG, e.getMessage());
        }
    }

    private void sender(BluetoothSocket bluetoothSocket) {
        try {
        OutputStream outputStream = bluetoothSocket.getOutputStream();
        byte[] bytes = ("asdfg").getBytes();
            outputStream.write(bytes);
        } catch (Exception e) {
            Log.i(LOG_TAG, e.getMessage());
        }
    }
}
