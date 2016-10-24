package by.black_pearl.cheloc.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import by.black_pearl.cheloc.DataBaser;
import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.TimerHandler;
import by.black_pearl.cheloc.TimerHandlerListener;
import by.black_pearl.cheloc.activity.scrollActivity.AddressBlock;

/**
 * By BLACK_Pearl.
 * This class to start bt server to get data from other devices.
 */
class BTServer {
    private final static String LOG_TAG = "BTServer";
    private final BluetoothAdapter bluetoothAdapter;
    private Context mContext;
    private LinearLayout linearLayout;
    private static final String SPLIT_TAG = "\\*\\*\\*";
    private BtProcessListener mListener;
    private boolean mIsRunning = true;
    private TimerHandler mTimerHandler;

    /**
     * Use this class to start bt server to get data from other devices.
     * @param bluetoothAdapter
     * @param btProcessListener
     */
    BTServer(BluetoothAdapter bluetoothAdapter, LinearLayout layout, BtProcessListener btProcessListener, Context context) {
        Log.i(LOG_TAG, "BTServer()");
        this.bluetoothAdapter = bluetoothAdapter;
        this.mContext = context;
        this.linearLayout = layout;
        this.mListener = btProcessListener;
        this.mTimerHandler = new TimerHandler(getTimerRunnable());
    }

    private TimerHandlerListener getTimerRunnable() {
        return new TimerHandlerListener() {
            @Override
            public void timerSterted() {

            }

            @Override
            public void timerTerminated() {

            }

            @Override
            public void timerStopped() {
                mIsRunning = false;
            }
        };
    }

    /**
     * Starts listen for incoming bt connection.
     */
    void startServer() {
        Log.i(LOG_TAG, "startServer()");
        Runnable serverRunnable = new Runnable() {
            @Override
            public void run() {
                Log.i(LOG_TAG, "serverRunnable::run()");
                try {
                    BluetoothServerSocket bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(
                            BluetoothManager.BT_NAME,
                            BluetoothManager.UUID_APP
                    );
                    BluetoothSocket bluetoothSocket = bluetoothServerSocket.accept();
                    bluetoothServerSocket.close();
                    getData(bluetoothSocket);
                } catch (Exception e) {
                    Log.i(LOG_TAG, e.getMessage());
                }
            }
        };
        Thread serverThread = new Thread(serverRunnable, "ChelocBtServer");
        serverThread.start();
    }

    /**
     * Prepare server to receive and save getted data.
     * Creates new TextView on layout view and starts new thread to process data.
     *
     * @param bluetoothSocket
     */
    private void getData(final BluetoothSocket bluetoothSocket) {
        Log.i(LOG_TAG, "getData()");
        Thread getDataThread = new Thread(getDataRunnable(bluetoothSocket));
        getDataThread.start();
    }

    /**
     * Returns Runnable to read byte to byte from bluetooth socket.
     *
     * @param bluetoothSocket
     * @return
     */
    private Runnable getDataRunnable(final BluetoothSocket bluetoothSocket) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = bluetoothSocket.getInputStream();
                    OutputStream outputStream = bluetoothSocket.getOutputStream();
                    String message = "";
                    while (mIsRunning) {
                        if (inputStream.available() > 0) {
                            message += new String(new byte[]{(byte) inputStream.read()}, "windows-1251");
                        } else {
                            if (!message.equals("")) {
                                if(!mTimerHandler.isTimerStop()) {
                                    mTimerHandler.stopTimer();
                                }
                                if (message.equals(BTClient.STOP_MARKER)) {
                                    mListener.sendComplete();
                                    bluetoothSocket.close();
                                    return;
                                }
                                outputStream.write(1);
                                Log.i(LOG_TAG, "MESSAGE GETTED: " + message);
                                linearLayout.post(getAddressBlockAdderRunnable(message));
                                message = "";
                            } else {
                                if(mTimerHandler.isTimerStop()) {
                                    mTimerHandler.startTimer(TimerHandler.DEFAULT_EXTEND_TIME_60);
                                }
                            }
                        }
                    }
                    bluetoothSocket.close();
                } catch (Exception e) {
                    Log.i(LOG_TAG, e.getMessage());
                }

            }
        };
    }

    /**
     * Return runnable what prints new AddressBlock from incoming resource.
     *
     * @param message
     * @return
     */
    private Runnable getAddressBlockAdderRunnable(final String message) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    String[] messages = message.split(SPLIT_TAG);
                    AddressBlock addressBlock = new AddressBlock(mContext, AddressBlock.EDITABLE_OFF);
                    addressBlock.setTextAddressTextView(messages[0]);
                    addressBlock.setTextLatitudeTextView(messages[1]);
                    addressBlock.setTextLongtitudeTextView(messages[2]);
                    addressBlock.setTextAltitudeTextView(messages[3]);
                    addressBlock.setCheckBoxVisibility(View.VISIBLE);
                    addressBlock.setBackgroundColor(ContextCompat.getColor(mContext, R.color.scrollBlockColor));
                    linearLayout.addView(addressBlock);
                } catch (Exception e) {
                    Log.i(LOG_TAG, e.getMessage());
                }
            }
        };
    }

    /**
     * Try to stop running bt server.
     */
    void stopServer() {
        Log.i(LOG_TAG, "stopServer()");
        this.mIsRunning = false;
    }

    /**
     * Set runnable to button to save data.
     *
     * @param fab - FloatingActionButton.
     */
    void setDataSaveButton(FloatingActionButton fab) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int childCount = linearLayout.getChildCount();
                ArrayList<AddressBlock> addressBlocks = new ArrayList<>();
                for (int i = 1; i < childCount; i++) {
                    if (((AddressBlock) linearLayout.getChildAt(i)).isCheckBoxCheked()) {
                        addressBlocks.add((AddressBlock) linearLayout.getChildAt(i));
                    }
                }
                if (addressBlocks.size() != 0) {
                    DataBaser dataBaser = new DataBaser(mContext);
                    for (int i = 0; i < addressBlocks.size(); i++) {
                        dataBaser.insertNewAddress(
                                addressBlocks.get(i).getAddress(),
                                addressBlocks.get(i).getLatitude(),
                                addressBlocks.get(i).getLongtitude(),
                                addressBlocks.get(i).getAltitude()
                        );
                    }
                }
                mListener.finishApp();
            }
        });
    }
}
