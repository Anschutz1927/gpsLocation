package by.black_pearl.cheloc.activity.scrollActivity;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import by.black_pearl.cheloc.activity.EditText;
import by.black_pearl.cheloc.activity.TextInterface;
import by.black_pearl.cheloc.activity.TextView;

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
    }

    private void generateAddressBlock(boolean isEditable) {
        TextView textAddressTextView = new TextView(mContext);
        TextView textLatitudeTextView = new TextView(mContext);
        TextView textLongtitudeTextView = new TextView(mContext);
        TextView textAltitudeTextView = new TextView(mContext);
        if(isEditable) {
            addressTextView = new EditText(mContext);
            latitudeTextView = new EditText(mContext);
            longtitudeTextView = new EditText(mContext);
            altitudeTextView = new EditText(mContext);
        }
        else {
            addressTextView = new TextView(mContext);
            latitudeTextView = new TextView(mContext);
            longtitudeTextView = new TextView(mContext);
            altitudeTextView = new TextView(mContext);
        }
        textAddressTextView.setText("address");
        textLatitudeTextView.setText("latitude");
        textLongtitudeTextView.setText("longtitude");
        textAltitudeTextView.setText("altitude");
        this.addView(textAddressTextView);
        this.addView((View) addressTextView);
        this.addView(textLatitudeTextView);
        this.addView((View) latitudeTextView);
        this.addView(textLongtitudeTextView);
        this.addView((View) longtitudeTextView);
        this.addView(textAltitudeTextView);
        this.addView((View) altitudeTextView);
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
