package by.black_pearl.cheloc.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.activity.mainActivity.MainActivity;
import by.black_pearl.cheloc.location.CoordinatesForExtra;
import by.black_pearl.cheloc.location.LocationListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartFragment extends Fragment implements View.OnClickListener {
    private TextView mLatGpsTextView;
    private TextView mLonGpsTextView;
    private TextView mAltGpsTextView;
    private TextView mBerGpsTextView;
    private TextView mSpdGpsTextView;
    private TextView mSatGpsTextView;
    private LinearLayout mGpsLayout;
    private long mLastLocationTime = 0;

    public StartFragment() {
    }

    public static StartFragment newInstance() {
        return new StartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_start, container, false);
        initialize(v);
        printVersion((TextView) v.findViewById(R.id.verTextView));
        printLastKnownLocation();
        ((MainActivity) getActivity()).getLocationListener().registerOnLocationChangedListener(getListener());
        return v;
    }

    private LocationListener.OnLocationChanged getListener() {
        return new LocationListener.OnLocationChanged() {
            @Override
            public void onLocationChanged(Location location) {
                if (location == null) {
                    return;
                }
                setTextViewsBackgroundColor(
                        ContextCompat.getColor(getContext(), R.color.fragment_start_updated_location)
                );
                showLocation(location);
            }
        };
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).getLocationListener().removeOnLocationChangedListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exitButton:
                getActivity().finish();
                break;
            case R.id.toSetPosButton:
                ((MainActivity) getActivity()).fragmentChanger(
                        MainActivity.CHANGE_TO_SETPOSFRAGMENT,
                        getCoordinatesForExtra()
                );
                break;
            case R.id.stopMockLocationButton:
                stopMockLocation();
                break;
        }
    }

    private void initialize(View v) {
        v.findViewById(R.id.exitButton).setOnClickListener(this);
        v.findViewById(R.id.toSetPosButton).setOnClickListener(this);
        v.findViewById(R.id.stopMockLocationButton).setOnClickListener(this);
        this.mLatGpsTextView = (TextView) v.findViewById(R.id.latGpsLocationTextView);
        this.mLonGpsTextView = (TextView) v.findViewById(R.id.longGpsLocationTextView);
        this.mAltGpsTextView = (TextView) v.findViewById(R.id.altGpsLocationTextView);
        this.mBerGpsTextView = (TextView) v.findViewById(R.id.brnGpsLocationTextView);
        this.mSpdGpsTextView = (TextView) v.findViewById(R.id.spdGpsLocationTextView);
        this.mSatGpsTextView = (TextView) v.findViewById(R.id.stlGpsLocationTextView);
        this.mGpsLayout = (LinearLayout) v.findViewById(R.id.gpsLocationLayout);
    }

    private void printVersion(TextView textView) {
        try {
            textView.setText(
                    getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName
            );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            textView.setText("Unknown version.");
        }
    }

    private void printLastKnownLocation() {
        Location location = ((MainActivity) getActivity()).getLocationListener().getLastKnownLocation();
        if (location == null) {
            return;
        }
        mLastLocationTime = location.getTime();
        setTextViewsBackgroundColor(
                ContextCompat.getColor(getContext(), R.color.fragment_start_outdated_location)
        );
        showLocation(location);
    }

    private void setTextViewsBackgroundColor(int color) {
        this.mGpsLayout.setBackgroundColor(color);
    }

    private void showLocation(Location location) {
        mLatGpsTextView.setText(String.valueOf(location.getLatitude()));
        mLonGpsTextView.setText(String.valueOf(location.getLongitude()));
        mAltGpsTextView.setText(String.valueOf(location.getAltitude()));
        mBerGpsTextView.setText(String.valueOf(location.getBearing()));
        mSpdGpsTextView.setText(String.valueOf(location.getSpeed()));
        startTimer();
    }

    private void startTimer() {

    }

    private CoordinatesForExtra getCoordinatesForExtra() {
        long diffTime = System.currentTimeMillis() - this.mLastLocationTime;
        if (diffTime <= 5 * 60 * 1000 && diffTime >= 0) {
            return new CoordinatesForExtra(
                    mLatGpsTextView.getText().toString(),
                    mLonGpsTextView.getText().toString(),
                    mAltGpsTextView.getText().toString()
            );
        }
        return null;
    }

    private void stopMockLocation() {

        AlertDialog.Builder stopMockDialog = new AlertDialog.Builder(getContext());
        stopMockDialog.setTitle("Включить реальное местоположение?");
        stopMockDialog.setPositiveButton("Да, я не ошибся.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (((MainActivity) getActivity()).getBound()) {
                    ((MainActivity) getActivity()).getChelocService().stopMockLocation();
                } else {
                    Toast.makeText(getContext(), "Не удалось подключиться к сервису ;(",
                            Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(), "Нужно перезапустить приложение",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        stopMockDialog.setNegativeButton("Нет, случайно.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Реальное положение НЕ включено.",
                        Toast.LENGTH_LONG).show();
            }
        });
        stopMockDialog.show();
    }

}
