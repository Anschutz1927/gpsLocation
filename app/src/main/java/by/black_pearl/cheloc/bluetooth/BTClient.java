package by.black_pearl.cheloc.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Client class to accept file from client to server.
 */
public class BTClient {
    private static final String LOG_TAG = "BTClient";
    private BluetoothAdapter mBluetoothAdapter;
    private Context mContext;
    public static final String STOP_MARKER = "***stop***";
    private ProgressBar mProgressBar;

    /**
     * Create new BTClient to send data to server.
     *
     * @param bluetoothAdapter
     * @param context
     */
    public BTClient(BluetoothAdapter bluetoothAdapter, Context context, ProgressBar progressBar) {
        Log.i(LOG_TAG, "BTClient()");
        this.mBluetoothAdapter = bluetoothAdapter;
        this.mContext = context;
        this.mProgressBar = progressBar;
    }

    /**
     * Create connect to remote device.
     *
     * @param address - bt mac address remote device.
     */
    public void connectAndSend(String address, ArrayList<String> stringArrayList) {
        Log.i(LOG_TAG, "connectToDevice()");
        try {
            BluetoothDevice bluetoothDevice = mBluetoothAdapter.getRemoteDevice(address);
            BluetoothSocket bluetoothSocket = bluetoothDevice
                    .createRfcommSocketToServiceRecord(BluetoothManager.UUID_APP);
            bluetoothSocket.connect();
            Toast.makeText(mContext, "Connected successfuly.", Toast.LENGTH_SHORT).show();
            this.sendStrings(bluetoothSocket, stringArrayList);
        }
        catch (Exception e) {
            Log.i(LOG_TAG, e.getMessage());
            Toast.makeText(mContext, "Connect was wrong. Canceled.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method-sender. Send strings to remopte device.
     *
     * @param bluetoothSocket
     * @param strings         - ArrayList<String> with data.
     */
    public void sendStrings(BluetoothSocket bluetoothSocket, ArrayList<String> strings) {
        Log.i(LOG_TAG, "sendStrings()");
        try {
            Toast.makeText(mContext, "Sending...", Toast.LENGTH_SHORT).show();
            OutputStream outputStream = bluetoothSocket.getOutputStream();
            InputStream inputStream = bluetoothSocket.getInputStream();
            byte[] bytes;
            for (int i = 0; i < strings.size(); i++) {
                bytes = strings.get(i).getBytes("windows-1251");
                outputStream.write(bytes);
                updateProgressBar(i);
                if (inputStream.read() == 0) {
                    inputStream.close();
                    outputStream.close();
                    bluetoothSocket.close();
                    return;
                }
            }
            bytes = BTClient.STOP_MARKER.getBytes();
            outputStream.write(bytes);
            inputStream.close();
            //outputStream.close();
            bluetoothSocket.close();
        } catch (Exception e) {
            Log.i(LOG_TAG, e.getMessage());
        }
    }

    private void updateProgressBar(int i) {
        //mProgressBar.setProgress(i);
    }
}
