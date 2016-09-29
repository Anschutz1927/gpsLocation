package by.black_pearl.cheloc.activity.scrollActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.activity.bluetoothActivity.BluetoothActivity;

/**
 * This main class of scroll activity.
 * It be called that button save(load) data to insert (get from) SQLite database.
 */
public class ScrollActivity extends Activity {
    public final static String ActivityMode = "ActivityMode";
    public final static String EXTRA_LATITUDE = "latitude";
    public final static String EXTRA_LONGTITUDE = "longtitude";
    public final static String EXTRA_ALTITUDE = "altitude";
    public final static int ACTIVITY_MODE_SAVE = 1;
    public final static int ACTIVITY_MODE_LOAD = 2;
    private final static String LOG_TAG = "ScrollActivity";
    private int MODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        Log.i(LOG_TAG, "onCreate");
        LinearLayout bodyLinearLayout = ((LinearLayout)findViewById(R.id.bodyScrollActivityLayout));
        MODE = getIntent().getIntExtra(ActivityMode, 0);
        switch (MODE) {
            case ACTIVITY_MODE_SAVE:
                Log.i(LOG_TAG, "ACTIVITY_MODE_SAVE");
                try {
                    Log.i(LOG_TAG, "coordinates getted:" +
                            "\n" + getIntent().getStringExtra(ScrollActivity.EXTRA_LATITUDE) +
                            "\n" + getIntent().getStringExtra(ScrollActivity.EXTRA_LONGTITUDE) +
                            "\n" + getIntent().getStringExtra(ScrollActivity.EXTRA_ALTITUDE));
                }
                catch (Exception e) {
                    Log.i(LOG_TAG, e.getMessage());
                }
                ((TextView)findViewById(R.id.textTitleActivityTextView)).setText("Сохранение...");
                bodyLinearLayout.addView(new SaveActivity(this));
                break;
            case ACTIVITY_MODE_LOAD:
                Log.i(LOG_TAG, "ACTIVITY_MODE_LOAD");
                ((TextView)findViewById(R.id.textTitleActivityTextView)).setText("Загрузка...");
                bodyLinearLayout.addView(new LoadActivity(this));
                break;
            default:
                Log.i(LOG_TAG, "default");
                break;
        }
        //bt button
        findViewById(R.id.bluetoothButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScrollActivity.this, BluetoothActivity.class));
            }
        });
        //bt button
    }
    protected Context getContext() {
        Log.i(LOG_TAG, "getContext");
        return this;
    }
}