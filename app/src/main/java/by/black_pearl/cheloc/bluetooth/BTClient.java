package by.black_pearl.cheloc.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.OutputStream;

/**
 * Client class to accept file from client to server.
 */
public class BTClient {
    private static final String LOG_TAG = "BTClient";
    private BluetoothAdapter bluetoothAdapter;
    private Context mContext;

    public BTClient(BluetoothAdapter bluetoothAdapter, Context context) {
        this.bluetoothAdapter = bluetoothAdapter;
        this.mContext = context;
    }

    public void connectToDevice(String address) {
        try {
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
            BluetoothSocket bluetoothSocket = bluetoothDevice
                    .createRfcommSocketToServiceRecord(BluetoothManager.UUID_APP);
            bluetoothSocket.connect();
            Toast.makeText(mContext, "Connected successfuly.", Toast.LENGTH_SHORT).show();
            this.sender(bluetoothSocket);
        }
        catch (Exception e) {
            Log.i(LOG_TAG, e.getMessage());
            Toast.makeText(mContext, "Connect was wrong. Canceled.", Toast.LENGTH_SHORT).show();
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
