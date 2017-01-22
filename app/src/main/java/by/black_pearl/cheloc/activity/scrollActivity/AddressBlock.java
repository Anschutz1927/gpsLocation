package by.black_pearl.cheloc.activity.scrollActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.activity.EditText;
import by.black_pearl.cheloc.activity.TextInterface;
import by.black_pearl.cheloc.activity.TextView;
import by.black_pearl.cheloc.activity.mainActivity.Dialogs;

/**
 * By BlackPearl.
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

    /**
     * It provides block: address - position.
     *
     * @param context    base context.
     * @param isEditable true when block created for save data to have access to rewrite data.
     */
    public AddressBlock(Context context, boolean isEditable) {
        super(context);
        this.mContext = context;
        generateAddressBlock(isEditable);
        this.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.scrollBlockColor));
    }

    public AddressBlock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AddressBlock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AddressBlock(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void generateAddressBlock(boolean isEditable) {
        LayoutInflater.from(mContext).inflate(R.layout.view_address_block, this, true);
        if(isEditable) {
            addressTextView = new EditText(mContext);
            ((EditText) addressTextView).requestTextFocus();
            ((EditText) addressTextView).setTextHint("Введите адрес...");
            latitudeTextView = new EditText(mContext);
            ((EditText) latitudeTextView).setTextHint("53.xxxxxxxx");
            latitudeTextView.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            longtitudeTextView = new EditText(mContext);
            ((EditText) longtitudeTextView).setTextHint("27.xxxxxxxx");
            longtitudeTextView.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            altitudeTextView = new EditText(mContext);
            ((EditText) altitudeTextView).setTextHint("80 - 340");
            altitudeTextView.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        }
        else {
            addressTextView = new TextView(mContext);
            latitudeTextView = new TextView(mContext);
            longtitudeTextView = new TextView(mContext);
            altitudeTextView = new TextView(mContext);
        }
        this.addressTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER_HORIZONTAL;
        this.addressTextView.setTextLayoutParams(params);
        this.checkBox = (CheckBox) findViewById(R.id.addressBlockCheckBox);
        this.checkBox.setChecked(false);
        this.checkBox.setVisibility(GONE);
        ((LinearLayout) this.findViewById(R.id.addressLayout)).addView((View) addressTextView);
        ((LinearLayout) this.findViewById(R.id.latitudeLayout)).addView((View) latitudeTextView);
        ((LinearLayout) this.findViewById(R.id.longtitudeLayout)).addView((View) longtitudeTextView);
        ((LinearLayout) this.findViewById(R.id.altitudeLayout)).addView((View) altitudeTextView);
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
