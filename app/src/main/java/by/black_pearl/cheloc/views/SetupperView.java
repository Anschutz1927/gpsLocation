package by.black_pearl.cheloc.views;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.location.Coordinates;
import by.black_pearl.cheloc.location.CoordinatesForExtra;

/**
 * Created by BLACK_Pearl.
 */

public class SetupperView extends LinearLayout implements View.OnClickListener {
    private static final String LOG_TAG = SetupperView.class.getName();
    private final Context mContext;
    private final ButtonsOnCliclListener mListener;

    public SetupperView(Context context, Bundle arguments, ButtonsOnCliclListener listener) {
        super(context);
        this.mContext = context;
        this.mListener = listener;
        LayoutInflater.from(context).inflate(R.layout.setupper_view, this, true);
        printExtraToView(arguments);
        setListeners();
    }

    private void setListeners() {
        findViewById(R.id.savePosButon).setOnClickListener(this);
        findViewById(R.id.loadPosButton).setOnClickListener(this);
        findViewById(R.id.setPosWithoutUpdatesButton).setOnClickListener(this);
        findViewById(R.id.setPosWithUpdatesButton).setOnClickListener(this);
        findViewById(R.id.cancelSetLocationButton).setOnClickListener(this);
    }

    public void printExtraToView(Bundle args) {
        if (args != null) {
            ((EditText) findViewById(R.id.latEditText)).setText(args.getString(CoordinatesForExtra.EXTRA_LATITUDE));
            ((EditText) findViewById(R.id.longEditText)).setText(args.getString(CoordinatesForExtra.EXTRA_LONGTITUDE));
            ((EditText) findViewById(R.id.altEditText)).setText(args.getString(CoordinatesForExtra.EXTRA_ALTITUDE));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.savePosButon:
                if (!isCorrect()) {
                    return;
                }
                mListener.onClickSaveLocationButton(SetupperView.this);
                break;
            case R.id.loadPosButton:
                mListener.onClickLoadLocationButton();
                break;
            case R.id.setPosWithoutUpdatesButton:
                if (!isCorrect()) {
                    return;
                }
                mListener.onClickSetLoacationButton(getCoordinates());
                break;
            case R.id.setPosWithUpdatesButton:
                if (!isCorrect()) {
                    return;
                }
                mListener.onClickSetUpdatedLocationButton(getCoordinates());
                break;
            case R.id.cancelSetLocationButton:
                mListener.onClickCancelButton();
                break;
        }
    }

    private Coordinates getCoordinates() {
        return new Coordinates(
                Double.valueOf(((EditText) (findViewById(R.id.latEditText)))
                        .getText().toString()),
                Double.valueOf(((EditText) (findViewById(R.id.longEditText)))
                        .getText().toString()),
                Double.valueOf(((EditText) (findViewById(R.id.altEditText)))
                        .getText().toString()),
                0.0f,
                getSpeedMode(),
                ((CheckBox) findViewById(R.id.randPosCheckBox)).isChecked()
        );
    }

    private int getSpeedMode() {
        switch (
                ((RadioGroup) (findViewById(R.id.setSpeedModeRadioGroup)))
                        .getCheckedRadioButtonId()
                ) {
            case R.id.walkSpeedModeRadioButton:
                return 1;
            case R.id.driveSpeedModeRadioButton:
                return 2;
        }
        return 0;
    }

    public CoordinatesForExtra getCoordinatesForExtra() {
        Log.i(LOG_TAG, "getCoordinates:CoordinatesForExtra");
        return new CoordinatesForExtra(
                ((EditText) (findViewById(R.id.latEditText))).getText().toString(),
                ((EditText) (findViewById(R.id.longEditText))).getText().toString(),
                ((EditText) (findViewById(R.id.altEditText))).getText().toString()
        );
    }

    public boolean isCorrect() {
        Log.i(LOG_TAG, "isCorrect");
        if ((((EditText) findViewById(R.id.latEditText)).getText().toString().equalsIgnoreCase("") ||
                ((EditText) findViewById(R.id.longEditText)).getText().toString().equalsIgnoreCase("") ||
                ((EditText) findViewById(R.id.altEditText)).getText().toString().equals(""))) {
            Toast.makeText(this.mContext, "No data... Check editboxes.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public interface ButtonsOnCliclListener {
        void onClickSaveLocationButton(SetupperView setupperView);

        void onClickLoadLocationButton();

        void onClickSetLoacationButton(Coordinates coordinates);

        void onClickSetUpdatedLocationButton(Coordinates coordinates);

        void onClickCancelButton();
    }
}
