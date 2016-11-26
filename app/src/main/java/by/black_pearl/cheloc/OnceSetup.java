package by.black_pearl.cheloc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

/**
 * Created by BLACK_Pearl.
 */

public class OnceSetup extends LinearLayout implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Context mContext;
    private Button mCancelOnceButton;
    private Switch mTurnOffEmulatorSwitch;
    private Switch mTurnOnEmulatorSwitch;
    private EditText mSettedLatitudeEditText;
    private EditText mSettedLongtitudeEditText;
    private EditText mSettedAltitudeEditText;
    private EditText mTimeEditText;

    public OnceSetup(Context context) {
        super(context);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.activity_main_setup_once, this, true);
        this.mCancelOnceButton = (Button) findViewById(R.id.cancelOneButton);
        this.mTurnOffEmulatorSwitch = (Switch) findViewById(R.id.turnOffEmulatorSwitch);
        this.mTurnOnEmulatorSwitch = (Switch) findViewById(R.id.turnOnEmulatorSwitch);
        this.mSettedLatitudeEditText = (EditText) findViewById(R.id.settedLatitudeEditText);
        this.mSettedLongtitudeEditText = (EditText) findViewById(R.id.settedLongtitudeEditText);
        this.mSettedAltitudeEditText = (EditText) findViewById(R.id.settedAltitudeEditText);
        this.mTimeEditText = (EditText) findViewById(R.id.timeEditText);
        setListeners();
    }

    private void setListeners() {
        this.mCancelOnceButton.setOnClickListener(this);
        this.mTurnOffEmulatorSwitch.setOnCheckedChangeListener(this);
        this.mTurnOnEmulatorSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        LinearLayout parentLayout = (LinearLayout) this.getParent();
        parentLayout.removeView(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.turnOffEmulatorSwitch:
                if (isChecked) {
                    mTurnOnEmulatorSwitch.setEnabled(false);
                    mSettedLatitudeEditText.setEnabled(false);
                    mSettedLongtitudeEditText.setEnabled(false);
                    mSettedAltitudeEditText.setEnabled(false);
                } else {
                    mTurnOnEmulatorSwitch.setEnabled(true);
                    mSettedLatitudeEditText.setEnabled(true);
                    mSettedLongtitudeEditText.setEnabled(true);
                    mSettedAltitudeEditText.setEnabled(true);
                }
                break;
            case R.id.turnOnEmulatorSwitch:
                if (isChecked) {
                    mTurnOffEmulatorSwitch.setEnabled(false);
                    mSettedLatitudeEditText.setEnabled(false);
                    mSettedLongtitudeEditText.setEnabled(false);
                    mSettedAltitudeEditText.setEnabled(false);
                } else {
                    mTurnOffEmulatorSwitch.setEnabled(true);
                    mSettedLatitudeEditText.setEnabled(true);
                    mSettedLongtitudeEditText.setEnabled(true);
                    mSettedAltitudeEditText.setEnabled(true);
                }
                break;
        }
    }
}
