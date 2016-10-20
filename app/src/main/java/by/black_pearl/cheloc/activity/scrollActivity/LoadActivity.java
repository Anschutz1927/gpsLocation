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
import android.widget.Toast;

import java.util.ArrayList;

import by.black_pearl.cheloc.DataBaser;
import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.activity.BtActivity;
import by.black_pearl.cheloc.activity.mainActivity.Dialogs;

/**
 * There is part of scroll activity. This block provide views to load from custom list data from DB.
 * It will create if load button has clicked from main activity on setPosition layout.
 * It need ScrollActivity at parameters.
 */
public class LoadActivity extends LinearLayout implements View.OnClickListener {
    private final static String LOG_TAG  = "LoadActivity";
    private Context mContext;
    private DataBaser mDataBaser;
    public static final String SEND_DIVIDER = "***";

    public LoadActivity(ScrollActivity scrollActivity) {
        super(scrollActivity.getContext());
        Log.i(LOG_TAG, "LoadActivity");
        this.mContext = scrollActivity.getContext();
        this.setOrientation(VERTICAL);
        loadFromDB(DataBaser.TABLE_METRO);
        loadFromDB(DataBaser.TABLE_NAME);
        setVisibleProperties(scrollActivity);
        setOnClickListeners(scrollActivity);
    }

    public void reloadLoadActivity() {
        this.removeAllViews();
        loadFromDB(DataBaser.TABLE_METRO);
        loadFromDB(DataBaser.TABLE_NAME);
    }

    private void setOnClickListeners(final ScrollActivity scrollActivity) {
        scrollActivity.findViewById(R.id.sendWithBtButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCheckedElements(scrollActivity);
            }
        });
        scrollActivity.findViewById(R.id.getFromBtButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollActivity.startActivity((new Intent(scrollActivity, BtActivity.class))
                        .putExtra(BtActivity.INTENT_SERVER_MODE, true));
            }
        });
        scrollActivity.findViewById(R.id.bluetoothButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibleProperties(scrollActivity);
            }
        });
    }

    private void sendCheckedElements(ScrollActivity scrollActivity) {
        int childCount = this.getChildCount();
        int prearrayCount = (getResources().getStringArray(R.array.metroAddresses)).length;
        ArrayList<String> listCheckedElements = new ArrayList<>();
        for (; prearrayCount < childCount; prearrayCount++) {
            if (((AddressBlock) this.getChildAt(prearrayCount)).isCheckBoxCheked()) {
                listCheckedElements.add(((AddressBlock) this.getChildAt(prearrayCount)).getAddress() +
                        SEND_DIVIDER + ((AddressBlock) this.getChildAt(prearrayCount)).getLatitude() +
                        SEND_DIVIDER + ((AddressBlock) this.getChildAt(prearrayCount)).getLongtitude() +
                        SEND_DIVIDER + ((AddressBlock) this.getChildAt(prearrayCount)).getAltitude());
            }
        }
        if (listCheckedElements.size() == 0) {
            Toast.makeText(mContext, "Ничего не выбрано!", Toast.LENGTH_SHORT).show();
            return;
        }
        scrollActivity.startActivity((new Intent(scrollActivity, BtActivity.class))
                .putExtra(BtActivity.INTENT_SERVER_MODE, false)
                .putStringArrayListExtra(BtActivity.INTENT_EXTRA_STRING_ARRAYLIST,
                        listCheckedElements));
        setVisibleProperties(scrollActivity);
    }

    private void setVisibleProperties(ScrollActivity scrollActivity) {
        int childCount = this.getChildCount();
        int prearrayCount = (getResources().getStringArray(R.array.metroAddresses)).length;
        if (scrollActivity.findViewById(R.id.sendWithBtButton).getVisibility() == View.INVISIBLE) {
            if (childCount <= prearrayCount) {
                Toast.makeText(mContext, "Нет сохранений для передачи!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Выделите для передачи.", Toast.LENGTH_SHORT).show();
                for (; prearrayCount < childCount; prearrayCount++) {
                    ((AddressBlock) this.getChildAt(prearrayCount)).setCheckBoxVisibility(View.VISIBLE);
                }
                scrollActivity.findViewById(R.id.sendWithBtButton).setVisibility(View.VISIBLE);
                scrollActivity.findViewById(R.id.getFromBtButton).setVisibility(View.VISIBLE);
            }
        } else {
            for (; prearrayCount < childCount; prearrayCount++) {
                ((AddressBlock) this.getChildAt(prearrayCount)).setCheckBoxVisibility(View.GONE);
            }
            scrollActivity.findViewById(R.id.sendWithBtButton).setVisibility(View.INVISIBLE);
            scrollActivity.findViewById(R.id.getFromBtButton).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Private method load data from database.
     * @param tableName
     */
    private void loadFromDB(String tableName) {
        Log.i(LOG_TAG, "loadFromDB");
        mDataBaser = new DataBaser(mContext);
        SQLiteDatabase db = mDataBaser.getReadableDatabase();
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                AddressBlock addressBlock = new AddressBlock(mContext, AddressBlock.EDITABLE_OFF);
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
        if (((AddressBlock) v).getCheckBoxVisibility() == GONE) {
            v.setBackgroundColor(Color.YELLOW);
            Intent data = new Intent();
            data.putExtra(ScrollActivity.EXTRA_LATITUDE, ((AddressBlock) v).getLatitude());
            data.putExtra(ScrollActivity.EXTRA_LONGTITUDE, ((AddressBlock) v).getLongtitude());
            data.putExtra(ScrollActivity.EXTRA_ALTITUDE, ((AddressBlock) v).getAltitude());
            ((ScrollActivity) mContext).setResult(ScrollActivity.RESULT_OK, data);
            ((ScrollActivity) mContext).finish();
        } else {
            ((AddressBlock) v).changeCheckBoxCheck();
        }
    }
}
