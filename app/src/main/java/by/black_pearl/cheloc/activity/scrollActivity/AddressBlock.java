package by.black_pearl.cheloc.activity.scrollActivity;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.activity.EditText;
import by.black_pearl.cheloc.activity.TextInterface;
import by.black_pearl.cheloc.activity.TextView;
import by.black_pearl.cheloc.activity.mainActivity.Dialogs;

import static android.support.v7.appcompat.R.attr.editTextColor;

/**
 * .
 */
public class AddressBlock extends LinearLayout {
    private Context mContext;
    private TextInterface addressTextView;
    private TextInterface latitudeTextView;
    private TextInterface longtitudeTextView;
    private TextInterface altitudeTextView;
    private CheckBox checkBox;
    public static final boolean EDITABLE_ON = true;
    public static final boolean EDITABLE_OFF = false;
    private final int FLAG_MAIN = 0;
    private final int FLAG_INFO = 1;
    private final int FLAG_CHECKBOX = 2;

    public AddressBlock(Context context, boolean isEditable) {
        super(context);
        this.mContext = context;
        this.setOrientation(HORIZONTAL);
        generateAddressBlock(isEditable);
        this.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.scrollBlockColor));
    }

    private void generateAddressBlock(boolean isEditable) {
        TextView textAddressTextView = new TextView(mContext);
        TextView textLatitudeTextView = new TextView(mContext);
        TextView textLongtitudeTextView = new TextView(mContext);
        TextView textAltitudeTextView = new TextView(mContext);
        if(isEditable) {
            int EMS = 7;
            addressTextView = new EditText(mContext);
            ((android.widget.EditText)(this.addressTextView)).setHint("Введите адрес...");
            ((android.widget.EditText) (this.addressTextView)).requestFocus();
            latitudeTextView = new EditText(mContext);
            latitudeTextView.setEms(EMS);
            latitudeTextView.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            longtitudeTextView = new EditText(mContext);
            longtitudeTextView.setEms(EMS);
            longtitudeTextView.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            altitudeTextView = new EditText(mContext);
            altitudeTextView.setEms(EMS);
            altitudeTextView.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        }
        else {
            addressTextView = new TextView(mContext);
            latitudeTextView = new TextView(mContext);
            longtitudeTextView = new TextView(mContext);
            altitudeTextView = new TextView(mContext);
        }
        textAddressTextView.setText(R.string.address);
        textLatitudeTextView.setText(R.string.Latitude);
        textLongtitudeTextView.setText(R.string.Longitude);
        textAltitudeTextView.setText(R.string.Altitude);
        textAddressTextView.setGravity(Gravity.CENTER);
        textLatitudeTextView.setGravity(Gravity.LEFT);
        textLongtitudeTextView.setGravity(Gravity.LEFT);
        textAltitudeTextView.setGravity(Gravity.LEFT);
        addressTextView.setTextColor(editTextColor);
        latitudeTextView.setTextColor(editTextColor);
        longtitudeTextView.setTextColor(editTextColor);
        altitudeTextView.setTextColor(editTextColor);
        addressTextView.setGravity(Gravity.CENTER);
        LinearLayout infoLayout = new LinearLayout(mContext);
        infoLayout.setOrientation(VERTICAL);
        infoLayout.setLayoutParams(setLayoutParams(FLAG_INFO));
        infoLayout.addView(textAddressTextView);
        infoLayout.addView((View) addressTextView);
        infoLayout.addView(textLatitudeTextView);
        infoLayout.addView((View) latitudeTextView);
        infoLayout.addView(textLongtitudeTextView);
        infoLayout.addView((View) longtitudeTextView);
        infoLayout.addView(textAltitudeTextView);
        infoLayout.addView((View) altitudeTextView);
        this.checkBox = new CheckBox(mContext);
        this.checkBox.setChecked(false);
        this.checkBox.setVisibility(GONE);
        //this.checkBox.setBackgroundColor(Color.GRAY);
        LinearLayout checkLayout = new LinearLayout(mContext);
        checkLayout.setOrientation(VERTICAL);
        checkLayout.setLayoutParams(setLayoutParams(FLAG_CHECKBOX));
        checkLayout.setGravity(Gravity.CENTER_VERTICAL);
        checkLayout.addView(this.checkBox);
        this.addView(infoLayout);
        this.addView(checkLayout);
        this.setLayoutParams(setLayoutParams(FLAG_MAIN));
    }

    private LayoutParams setLayoutParams(int flag) {
        LayoutParams linearParams;
        switch (flag) {
            case FLAG_MAIN:
                linearParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                linearParams.setMargins(15, 15, 15, 15);
                return linearParams;
            case FLAG_INFO:
                linearParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                linearParams.weight = 1;
                return linearParams;
            case FLAG_CHECKBOX:
                linearParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                linearParams.setMargins(15, 15, 15, 15);
                linearParams.weight = 0;
                return linearParams;
        }
        return null;
    }

    public void setupLongClick(final int id, final LoadActivity loadActivity) {
        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("AddressBlock", "OnLongClickListener\n");
                Dialogs.showDeleteAddressDialog(mContext, id, loadActivity);
                return true;
            }
        });
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }

    public void setTextAddressTextView(String address) {
        this.addressTextView.setText(address);
    }

    public void setTextLatitudeTextView(String latitude) {
        this.latitudeTextView.setText(latitude);
    }

    public void setTextLongtitudeTextView(String longtitude) {
        this.longtitudeTextView.setText(longtitude);
    }

    public void setTextAltitudeTextView(String altitude) {
        this.altitudeTextView.setText(altitude);
    }

    public void setCheckBoxVisibility(int visibility) {
        this.checkBox.setVisibility(visibility);
    }

    public void changeCheckBoxCheck() {
        if (this.checkBox.isChecked()) {
            this.checkBox.setChecked(false);
        } else {
            this.checkBox.setChecked(true);
        }
    }

    public boolean isCheckBoxCheked() {
        return this.checkBox.isChecked();
    }

    public int getCheckBoxVisibility() {
        return this.checkBox.getVisibility();
    }

    public String getAddress() {
        return this.addressTextView.getText().toString();
    }

    public String getLatitude() {
        return this.latitudeTextView.getText().toString();
    }

    public String getLongtitude() {
        return this.longtitudeTextView.getText().toString();
    }

    public String getAltitude() {
        return this.altitudeTextView.getText().toString();
    }
}
