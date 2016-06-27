package by.black_pearl.cheloc.activity;

import android.view.View;
import android.widget.EditText;

import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.location.ManagerOfTestLocation;

public class VoteLinearLayout {

    public static void onClickOpenLocationLinearLayout(MainActivity mainActivity) {
        if(ManagerOfTestLocation.getLocation(mainActivity) != null) {
            ((EditText) mainActivity.findViewById(R.id.latitudeEditText)).setText(
                    String.valueOf(ManagerOfTestLocation.getLocation(mainActivity).getLatitude())
            );
            ((EditText) mainActivity.findViewById(R.id.longtitudeEditText)).setText(
                    String.valueOf(ManagerOfTestLocation.getLocation(mainActivity).getLongitude())
            );
            ((EditText) mainActivity.findViewById(R.id.altitudeEditText)).setText(
                    String.valueOf(ManagerOfTestLocation.getLocation(mainActivity).getAltitude())
            );
        }
        mainActivity.findViewById(R.id.addLocationScrollView).setVisibility(View.VISIBLE);
    }

    public static void onClickDeleteOpenLocationLinearLayout(MainActivity mainActivity) {
        mainActivity.findViewById(R.id.deleteLocationLinearLayout).setVisibility(View.VISIBLE);
    }
}
