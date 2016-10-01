package by.black_pearl.cheloc.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Scanner;

/**
 * This class to start bt server to get data from other devices.
 */
public class BTServer {
    private final static String LOG_TAG = "BTServer";
    private final Context mContext;
    private final BluetoothAdapter bluetoothAdapter;

    /**
     * Use this class to start bt server to get data from other devices.
     * @param bluetoothAdapter
     * @param mContext
     */
    public BTServer(BluetoothAdapter bluetoothAdapter, Context mContext) {
        this.bluetoothAdapter = bluetoothAdapter;
        this.mContext = mContext;
    }

    public void startServer() {
        try {
            BluetoothServerSocket bluetoothServerSocket = bluetoothAdapter
                    .listenUsingRfcommWithServiceRecord(BluetoothManager.BT_NAME, BluetoothManager.UUID_APP);
            BluetoothSocket bluetoothSocket = bluetoothServerSocket.accept();
            this.getData(bluetoothSocket);
        } catch (Exception e) {
            Log.i(LOG_TAG, e.getMessage());
            Toast.makeText(mContext, "Server failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getData(BluetoothSocket bluetoothSocket) {
        try {
            InputStream inputStream = bluetoothSocket.getInputStream();
            Scanner s = new Scanner(inputStream).useDelimiter("-1");
            String result = s.hasNext() ? s.next() : "";
            Log.i(LOG_TAG, "MESSAGE GETTED: " + result);
            Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.i(LOG_TAG, e.getMessage());
            Toast.makeText(mContext, "Mistake to get data!", Toast.LENGTH_SHORT).show();
        }
    }
}
