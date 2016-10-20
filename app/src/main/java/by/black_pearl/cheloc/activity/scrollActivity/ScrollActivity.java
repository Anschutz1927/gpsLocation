package by.black_pearl.cheloc.activity.scrollActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import by.black_pearl.cheloc.R;

/**
 * By BLACK_Pearl.
 * This main class of scroll activity.
 * It be called that button save(load) data to insert (get from) SQLite database.
 */
public class ScrollActivity extends Activity {
    public final static String ACTIVITY_MODE = "ActivityMode";
    public final static String EXTRA_LATITUDE = "latitude";
    public final static String EXTRA_LONGTITUDE = "longtitude";
    public final static String EXTRA_ALTITUDE = "altitude";
    public final static int ACTIVITY_MODE_SAVE = 1;
    public final static int ACTIVITY_MODE_LOAD = 2;
    private final static String LOG_TAG = "ScrollActivity";
    private LinearLayout mBodyLinearLayout;
    private int mMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        Log.i(LOG_TAG, "onCreate");
        mBodyLinearLayout = ((LinearLayout) findViewById(R.id.bodyScrollActivityLayout));
        mMode = getIntent().getIntExtra(ACTIVITY_MODE, 0);
        switch (mMode) {
            case ACTIVITY_MODE_SAVE:
                ((TextView)findViewById(R.id.textTitleActivityTextView)).setText("Сохранение...");
                findViewById(R.id.bluetoothLayout).setVisibility(View.INVISIBLE);
                mBodyLinearLayout.addView(new SaveActivity(this));
                break;
            case ACTIVITY_MODE_LOAD:
                ((TextView)findViewById(R.id.textTitleActivityTextView)).setText("Загрузка...");
                findViewById(R.id.bluetoothLayout).setVisibility(View.VISIBLE);
                mBodyLinearLayout.addView(new LoadActivity(this));
                break;
            default:
                Log.i(LOG_TAG, "default");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
        if (mMode == ACTIVITY_MODE_LOAD) {
            ((LoadActivity) mBodyLinearLayout.getChildAt(0)).reloadLoadActivity();
        }
    }

    protected Context getContext() {
        Log.i(LOG_TAG, "getContext");
        return this;
    }
}