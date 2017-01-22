package by.black_pearl.cheloc.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;

import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.ViewSetupOnceModel;
import by.black_pearl.cheloc.activity.mainActivity.MainActivity;
import by.black_pearl.cheloc.activity.scrollActivity.ScrollActivity;
import by.black_pearl.cheloc.location.Coordinates;
import by.black_pearl.cheloc.location.CoordinatesForExtra;
import by.black_pearl.cheloc.location.service.ChelocService;
import by.black_pearl.cheloc.views.ManagerView;
import by.black_pearl.cheloc.views.SetupperView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SetPosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetPosFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = "SetPosFragment";
    private SetupperView mSetupperView;
    private ManagerView mManagerView;
    private Button mSetupperButton;
    private Button mManagerButton;
    private SetupperView.ButtonsOnCliclListener listener;

    public SetPosFragment() {
    }

    public static SetPosFragment newInstance(CoordinatesForExtra coordinatesForExtra) {
        SetPosFragment fragment = new SetPosFragment();
        try {
            Bundle args = new Bundle();
            args.putString(CoordinatesForExtra.EXTRA_LATITUDE, coordinatesForExtra.getLatitude());
            args.putString(CoordinatesForExtra.EXTRA_LONGTITUDE, coordinatesForExtra.getLongtitude());
            args.putString(CoordinatesForExtra.EXTRA_ALTITUDE, coordinatesForExtra.getAltitude());
            fragment.setArguments(args);
        } catch (Exception ignored) {
            Log.i(LOG_TAG, "setPos without extras.");
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mSetupperView = new SetupperView(getContext(), getArguments(), getSetupperListener());
        this.mManagerView = new ManagerView(getContext(), getManagerListener());
        this.mManagerView.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_set_pos, container, false);
        v.findViewById(R.id.setupperButton).setOnClickListener(this);
        v.findViewById(R.id.managerButton).setOnClickListener(this);
        FrameLayout fLayout = (FrameLayout) v.findViewById(R.id.setPosLayout);
        fLayout.addView(this.mSetupperView);
        fLayout.addView(this.mManagerView);
        this.mSetupperButton = (Button) v.findViewById(R.id.setupperButton);
        this.mSetupperButton.setEnabled(false);
        this.mManagerButton = (Button) v.findViewById(R.id.managerButton);
        this.mManagerButton.setEnabled(true);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setupperButton:
                mSetupperButton.setEnabled(false);
                mManagerButton.setEnabled(true);
                this.mSetupperView.setVisibility(View.VISIBLE);
                this.mManagerView.setVisibility(View.GONE);
                break;
            case R.id.managerButton:
                mSetupperButton.setEnabled(true);
                mManagerButton.setEnabled(false);
                this.mSetupperView.setVisibility(View.GONE);
                this.mManagerView.setVisibility(View.VISIBLE);
                break;
        }
    }

    public SetupperView.ButtonsOnCliclListener getSetupperListener() {
        return new SetupperView.ButtonsOnCliclListener() {
            @Override
            public void onClickSaveLocationButton(SetupperView setupperView) {
                Intent scrollIntent = new Intent(getContext(), ScrollActivity.class);
                CoordinatesForExtra forExtra = setupperView.getCoordinatesForExtra();
                scrollIntent.putExtra(ScrollActivity.ACTIVITY_MODE, ScrollActivity.ACTIVITY_MODE_SAVE)
                        .putExtra(ScrollActivity.EXTRA_LATITUDE, forExtra.getLatitude())
                        .putExtra(ScrollActivity.EXTRA_LONGTITUDE, forExtra.getLongtitude())
                        .putExtra(ScrollActivity.EXTRA_ALTITUDE, forExtra.getAltitude());
                startActivity(scrollIntent);
            }

            @Override
            public void onClickLoadLocationButton() {
                startActivityForResult(new Intent(getContext(), ScrollActivity.class)
                                .putExtra(ScrollActivity.ACTIVITY_MODE, ScrollActivity.ACTIVITY_MODE_LOAD),
                        ScrollActivity.ACTIVITY_MODE_LOAD);
            }

            @Override
            public void onClickSetLoacationButton(Coordinates coordinates) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity.getBound()) {
                    ChelocService chelocService = mainActivity.getChelocService();
                    if (!chelocService.isWork()) {
                        chelocService.setMockLocation(coordinates, false);
                    } else {
                        chelocService.changeMockLocation(coordinates, false);
                    }
                }
                getActivity().onBackPressed();
            }

            @Override
            public void onClickSetUpdatedLocationButton(Coordinates coordinates) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity.getBound()) {
                    ChelocService chelocService = mainActivity.getChelocService();
                    if (!chelocService.isWork()) {
                        chelocService.setMockLocation(coordinates, true);
                    } else {
                        chelocService.changeMockLocation(coordinates, true);
                    }
                }
                getActivity().onBackPressed();
            }

            @Override
            public void onClickCancelButton() {
                getActivity().onBackPressed();
            }
        };
    }

    private ManagerView.ButtonsOnClickListener getManagerListener() {
        return new ManagerView.ButtonsOnClickListener() {
            @Override
            public void onClickCancelButton() {
                getActivity().onBackPressed();
            }

            @Override
            public void onClickRunButton(ArrayList<ViewSetupOnceModel> listModels) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity.getBound()) {
                    ChelocService chelocService = mainActivity.getChelocService();
                    if (!chelocService.isWork()) {
                        Toast.makeText(getContext(), "Loading to start manager.", Toast.LENGTH_SHORT).show();
                        chelocService.setManagerMockLocation(listModels);
                    } else {
                        Toast.makeText(getContext(), "Need to stop service at first!", Toast.LENGTH_LONG).show();
                    }
                }
                getActivity().onBackPressed();
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(LOG_TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle args = new Bundle();
            args.putString(CoordinatesForExtra.EXTRA_LATITUDE, data.getStringExtra(ScrollActivity.EXTRA_LATITUDE));
            args.putString(CoordinatesForExtra.EXTRA_LONGTITUDE, data.getStringExtra(ScrollActivity.EXTRA_LONGTITUDE));
            args.putString(CoordinatesForExtra.EXTRA_ALTITUDE, data.getStringExtra(ScrollActivity.EXTRA_ALTITUDE));
            mSetupperView.printExtraToView(args);
        }
    }
}
