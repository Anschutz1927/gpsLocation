package by.black_pearl.cheloc.activity.scrollActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import by.black_pearl.cheloc.DataBaser;
import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.activity.mainActivity.Dialogs;

/**
 * There is part of scroll activity. This block provide views to load from custom list data from DB.
 * It will create if load button has clicked from main activity on setPosition layout.
 * It need ScrollActivity at parameters.
 */
public class LoadActivity extends LinearLayout implements View.OnClickListener {
    private final static String LOG_TAG  = "LoadActivity";
    private ScrollActivity scrollActivity;
    private Context mContext;
    private DataBaser dataBaser;

    public LoadActivity(ScrollActivity scrollActivity) {
        super(scrollActivity.getContext());
        Log.i(LOG_TAG, "LoadActivity");
        this.scrollActivity = scrollActivity;
        this.mContext = scrollActivity.getContext();
        this.setOrientation(VERTICAL);
        loadFromDB(DataBaser.TABLE_METRO);
        loadFromDB(DataBaser.TABLE_NAME);
    }

    public void reloadLoadActivity() {
        this.removeAllViews();
        loadFromDB(DataBaser.TABLE_METRO);
        loadFromDB(DataBaser.TABLE_NAME);
    }

    /**
     * Private method it load data from database.
     * @param tableName
     */
    private void loadFromDB(String tableName) {
        Log.i(LOG_TAG, "loadFromDB");
        dataBaser = new DataBaser(mContext);
        SQLiteDatabase db = dataBaser.getReadableDatabase();
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                AddressBlock addressBlock = new AddressBlock(mContext, false);
                if(tableName == DataBaser.TABLE_METRO) {
                    addressBlock.setBackgroundColor(ContextCompat.getColor(this.getContext(),
                            R.color.metroBlock));
                }
                else {
                    addressBlock.setupLongClick(
                            cursor.getInt(cursor.getColumnIndex(DataBaser.ID_COLUMN)), this);
                    Button button = new Button(mContext);
                    button.setText("del");
                    final int id = cursor.getInt(cursor.getColumnIndex(DataBaser.ID_COLUMN));
                    final LoadActivity l = this;
                    button.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Dialogs.showDeleteAddressDialog(mContext, id, l);
                        }
                    });
                }
                addressBlock.setTextAddressTextView(
                        cursor.getString(cursor.getColumnIndex(DataBaser.ADDRESS_COLUMN))
                );
                addressBlock.setTextLatitudeTextView(
                        cursor.getString(cursor.getColumnIndex(DataBaser.LATITUDE_COLUMN))
                );
                addressBlock.setTextLongtitudeTextView(
                        cursor.getString(cursor.getColumnIndex(DataBaser.LONGTITUDE_COLUMN))
                );
                addressBlock.setTextAltitudeTextView(
                        cursor.getString(cursor.getColumnIndex(DataBaser.ALTITUDE_COLUMN))
                );
                addressBlock.setOnClickListener(this);
                this.addView(addressBlock);
            }
            while(cursor.moveToNext());
        }
        Log.i(LOG_TAG, "DB loaded.");
    }

    @Override
    public void onClick(View v) {
        Log.i(LOG_TAG, "onClick");
        v.setBackgroundColor(Color.YELLOW);
        Log.i("LoadActivity", "===========================\n" +
                "\n" + ((AddressBlock)v).getAddressTextView() +
                "\n" + ((AddressBlock)v).getLatitudeTextView() +
                "\n" + ((AddressBlock)v).getLongtitudeTextView() +
                "\n" + ((AddressBlock)v).getAltitude());
        Intent data = new Intent();
        data.putExtra(ScrollActivity.EXTRA_LATITUDE, ((AddressBlock)v).getLatitudeTextView());
        data.putExtra(ScrollActivity.EXTRA_LONGTITUDE, ((AddressBlock)v).getLongtitudeTextView());
        data.putExtra(ScrollActivity.EXTRA_ALTITUDE, ((AddressBlock)v).getAltitude());
        scrollActivity.setResult(ScrollActivity.RESULT_OK, data);
        scrollActivity.finish();
    }
}
