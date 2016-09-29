package by.black_pearl.cheloc.activity.bluetoothActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.bluetooth.BluetoothManager;

public class BluetoothActivity extends Activity {
    private final String LOG_TAG ="BluetoothActivity";
    private BluetoothManager bluetoothManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate");
        setContentView(R.layout.activity_bluetooth);
        bluetoothManager = new BluetoothManager(this);
        this.setListeners();
    }

    private void setListeners() {
        findViewById(R.id.searchBtButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothManager.startSearchBtDevices();
            }
        });
        findViewById(R.id.acceptButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothManager.startServerToAccept();
                findViewById(R.id.btListView).setEnabled(false);
            }
        });
        ((ListView)findViewById(R.id.btListView)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String address = ((TextView)view).getText().toString();
                Log.i(LOG_TAG, "textFromClickItem = ");
                Log.i(LOG_TAG, "\n" + address + "\n");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart");
        if(bluetoothManager.checkBtAccess()) {
            bluetoothManager.printKnownBtDevicesToLog();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
        bluetoothManager.registerReceiver();
        bluetoothManager.fillDevicesToListView((ListView)findViewById(R.id.btListView));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause");
        bluetoothManager.stopSearchBtDevices();
        bluetoothManager.unregisterReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
                    bluetoothManager.printKnownBtDevicesToLog();
                }
                else {
                    Toast.makeText(this, "bluetooth не был включен!", Toast.LENGTH_SHORT).show();
                    this.finish();
                }
                break;
        }
    }
}
