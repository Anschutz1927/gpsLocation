package by.black_pearl.cheloc.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

import by.black_pearl.cheloc.R;

/**
 * Created by BLACK_Pearl.
 */

public class SetupOnce extends LinearLayout implements CompoundButton.OnCheckedChangeListener {

    private Switch mTurnOffEmulatorSwitch;
    private Switch mTurnOnEmulatorSwitch;
    private EditText mLatitudeEditText;
    private EditText mLongtitudeEditText;
    private EditText mAltitudeEditText;
    private EditText mTimeEditText;

    public SetupOnce(Context context) {
        super(context);
        initialize();
        setListeners();
    }

    private void initialize() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_setup_once, this, true);
        this.mTurnOffEmulatorSwitch = (Switch) findViewById(R.id.turnOffEmulatorSwitch);
        this.mTurnOnEmulatorSwitch = (Switch) findViewById(R.id.turnOnEmulatorSwitch);
        this.mLatitudeEditText = (EditText) findViewById(R.id.settedLatitudeEditText);
        this.mLongtitudeEditText = (EditText) findViewById(R.id.settedLongtitudeEditText);
        this.mAltitudeEditText = (EditText) findViewById(R.id.settedAltitudeEditText);
        this.mTimeEditText = (EditText) findViewById(R.id.timeEditText);
    }

    private void setListeners() {
        findViewById(R.id.deleteOneButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout) getParent()).removeView(SetupOnce.this);
            }
        });
        mTurnOffEmulatorSwitch.setOnCheckedChangeListener(this);
        mTurnOnEmulatorSwitch.setOnCheckedChangeListener(this);
    }

    public boolean getTurnOffEmulator() {
        return this.mTurnOffEmulatorSwitch.isChecked();
    }

    public boolean getTurnOnEmulator() {
        return this.mTurnOnEmulatorSwitch.isChecked();
    }

    public Double getSettedLatitude() {
        return Double.valueOf(this.mLatitudeEditText.getText().toString());
    }

    public Double getSettedLongtitude() {
        return Double.valueOf(this.mLongtitudeEditText.getText().toString());
    }

    public Double getSettedAltitude() {
        return Double.valueOf(this.mAltitudeEditText.getText().toString());
    }

    public int getSettedTime() {
        return Integer.valueOf(this.mTimeEditText.getText().toString());
    }

    public boolean isCorrectBlock() {
        if (mTimeEditText.getText().toString().equals("")) {
            return false;
        }
        boolean switchEnable = mTurnOnEmulatorSwitch.isChecked() || mTurnOffEmulatorSwitch.isChecked();
        return !(!switchEnable && (mLatitudeEditText.getText().toString().equals("")
                || mLongtitudeEditText.getText().toString().equals("")
                || mAltitudeEditText.getText().toString().equals("")));
    }

    public SetupOnce(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SetupOnce(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SetupOnce(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            this.mLatitudeEditText.setEnabled(false);
            this.mLongtitudeEditText.setEnabled(false);
            this.mAltitudeEditText.setEnabled(false);
            switch (buttonView.getId()) {
                case R.id.turnOffEmulatorSwitch:
                    mTurnOnEmulatorSwitch.setChecked(false);
                    break;
                case R.id.turnOnEmulatorSwitch:
                    mTurnOffEmulatorSwitch.setChecked(false);
                    break;
            }
        } else {
            if (!mTurnOffEmulatorSwitch.isChecked() && !mTurnOnEmulatorSwitch.isChecked()) {
                this.mLatitudeEditText.setEnabled(true);
                this.mLongtitudeEditText.setEnabled(true);
                this.mAltitudeEditText.setEnabled(true);
            }
        }
    }

    public interface ButtonsOnClickListener {
        void onDeleteOneButtonClick();
    }
}
