package by.black_pearl.cheloc.activity.scrollActivity;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import by.black_pearl.cheloc.DataBaser;

/**
 * There is part of scroll activity. This block provide views to save data from sePosLayout to DB.
 * It will create if save button has clicked from main activity on setPosition layout.
 * It need ScrollActivity at parameters.
 */
public class SaveActivity extends LinearLayout implements View.OnClickListener {
    private final static String LOG_TAG = "SaveActivity";
    private Context mContext;
    private ScrollActivity scrollActivity;
    private AddressBlock addressBlock;

    public SaveActivity(ScrollActivity scrollActivity) {
        super(scrollActivity.getContext());
        Log.i(LOG_TAG, "SaveActivity");
        this.mContext = scrollActivity.getContext();
        this.scrollActivity = scrollActivity;
        this.setOrientation(VERTICAL);
        generateSaveActivity();
        setupButton();
    }

    /**
     * Setup button "OK" to layot to confirm input data is right .
     */
    private void setupButton() {
        Log.i(LOG_TAG, "setupButton");
        Button buttonOk = new Button(mContext);
        buttonOk.setText("OK");
        buttonOk.setOnClickListener(this);
        this.addView(buttonOk);
    }

    /**
     * Insert AddressBlock to this layout vith edit text views and text views.
     */
    private void generateSaveActivity() {
        Log.i(LOG_TAG, "generateSaveActivity");
        try {
            Log.i(LOG_TAG, "coordinates getted:" +
                    "\n" + scrollActivity.getIntent().getStringExtra(ScrollActivity.EXTRA_LATITUDE) +
                    "\n" + scrollActivity.getIntent().getStringExtra(ScrollActivity.EXTRA_LONGTITUDE) +
                    "\n" + scrollActivity.getIntent().getStringExtra(ScrollActivity.EXTRA_ALTITUDE));
        }
        catch (Exception e) {
            Log.i(LOG_TAG, e.getMessage());
        }
        addressBlock = new AddressBlock(mContext, AddressBlock.EDITABLE_ON);
        addressBlock.setTextAddressTextView("");
        addressBlock.setTextLatitudeTextView(scrollActivity.getIntent()
                .getStringExtra(ScrollActivity.EXTRA_LATITUDE));
        addressBlock.setTextLongtitudeTextView(scrollActivity.getIntent()
                .getStringExtra(ScrollActivity.EXTRA_LONGTITUDE));
        addressBlock.setTextAltitudeTextView(scrollActivity.getIntent()
                .getStringExtra(ScrollActivity.EXTRA_ALTITUDE));
        this.addView(addressBlock);
    }

    @Override
    public void onClick(View v) {
        DataBaser dataBaser = new DataBaser(mContext);
        dataBaser.insertNewAddress(
                addressBlock.getAddress(),
                addressBlock.getLatitude(),
                addressBlock.getLongtitude(),
                addressBlock.getAltitude()
        );
        scrollActivity.finish();
    }
}
