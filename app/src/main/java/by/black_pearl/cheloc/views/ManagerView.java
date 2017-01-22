package by.black_pearl.cheloc.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.ViewSetupOnceModel;

/**
 * Created by BLACK_Pearl.
 */

public class ManagerView extends LinearLayout implements View.OnClickListener {
    private ManagerView.ButtonsOnClickListener mListener;
    private LinearLayout mContentManagerLayout;

    public ManagerView(Context context, ButtonsOnClickListener listener) {
        this(context);
        this.mListener = listener;
        initialize();
    }

    public ManagerView(Context context) {
        super(context);
    }

    public ManagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ManagerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ManagerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initialize() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_manager_view, this, true);
        findViewById(R.id.cancelButton).setOnClickListener(this);
        findViewById(R.id.startButton).setOnClickListener(this);
        findViewById(R.id.addFab).setOnClickListener(this);
        this.mContentManagerLayout = (LinearLayout) findViewById(R.id.content_manager);
        this.mContentManagerLayout.addView(new SetupOnce(getContext()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelButton:
                mListener.onClickCancelButton();
                break;
            case R.id.startButton:
                if (!correct()) {
                    Toast.makeText(getContext(), "Please, put correct data to blocks!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<ViewSetupOnceModel> listModels = parceView();
                mListener.onClickRunButton(listModels);
                break;
            case R.id.addFab:
                this.mContentManagerLayout.addView(new SetupOnce(getContext()));
                break;
        }
    }

    private ArrayList<ViewSetupOnceModel> parceView() {
        ArrayList<ViewSetupOnceModel> models = new ArrayList<>();
        for (int i = 0; i < this.mContentManagerLayout.getChildCount(); i++) {
            models.add(new ViewSetupOnceModel(
                    ((Switch) this.mContentManagerLayout.getChildAt(i).findViewById(R.id.turnOffEmulatorSwitch)).isChecked(),
                    ((Switch) this.mContentManagerLayout.getChildAt(i).findViewById(R.id.turnOnEmulatorSwitch)).isChecked(),
                    Double.valueOf(((EditText) this.mContentManagerLayout.getChildAt(i)
                            .findViewById(R.id.settedLatitudeEditText)).getText().toString()),
                    Double.valueOf(((EditText) this.mContentManagerLayout.getChildAt(i)
                            .findViewById(R.id.settedLongtitudeEditText)).getText().toString()),
                    Double.valueOf(((EditText) this.mContentManagerLayout.getChildAt(i)
                            .findViewById(R.id.settedAltitudeEditText)).getText().toString()),
                    Integer.valueOf(((EditText) this.mContentManagerLayout.getChildAt(i)
                            .findViewById(R.id.timeEditText)).getText().toString())
            ));
        }
        return models;
    }

    private boolean correct() {
        for (int i = 0; i < this.mContentManagerLayout.getChildCount(); i++) {
            if (!((SetupOnce) this.mContentManagerLayout.getChildAt(i)).isCorrectBlock()) {
                return false;
            }
        }
        return true;
    }

    public interface ButtonsOnClickListener {
        void onClickCancelButton();

        void onClickRunButton(ArrayList<ViewSetupOnceModel> listModels);
    }
}
