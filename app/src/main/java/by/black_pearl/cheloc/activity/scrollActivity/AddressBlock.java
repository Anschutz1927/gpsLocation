package by.black_pearl.cheloc.activity.scrollActivity;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.activity.EditText;
import by.black_pearl.cheloc.activity.TextInterface;
import by.black_pearl.cheloc.activity.TextView;
import by.black_pearl.cheloc.activity.mainActivity.Dialogs;

/**
 * .
 */
public class AddressBlock extends LinearLayout implements View.OnClickListener{
    private Context mContext;
    private TextInterface addressTextView;
    private TextInterface latitudeTextView;
    private TextInterface longtitudeTextView;
    private TextInterface altitudeTextView;

    public AddressBlock(Context context, boolean isEditable) {
        super(context);
        this.mContext = context;
        this.setOrientation(VERTICAL);
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
            ((android.widget.EditText)(this.addressTextView)).requestFocus();
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
        addressTextView.setGravity(Gravity.CENTER);
        this.addView(textAddressTextView);
        this.addView((View) addressTextView);
        this.addView(textLatitudeTextView);
        this.addView((View) latitudeTextView);
        this.addView(textLongtitudeTextView);
        this.addView((View) longtitudeTextView);
        this.addView(textAltitudeTextView);
        this.addView((View) altitudeTextView);
        setLayoutParams();
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

    private void setLayoutParams() {
        LayoutParams linearParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearParams.setMargins(15, 15, 15, 15);
        this.setLayoutParams(linearParams);
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

    public String getAddressTextView() {
        return this.addressTextView.getText().toString();
    }

    public String getLatitudeTextView() {
        return this.latitudeTextView.getText().toString();
    }

    public String getLongtitudeTextView() {
        return this.longtitudeTextView.getText().toString();
    }

    public String getAltitude() {
        return this.altitudeTextView.getText().toString();
    }

    @Override
    public void onClick(View v) {

    }
}
