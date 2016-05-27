package by.black_pearl.cheloc.activity;

import android.view.View;

import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.location.ManagerOfTestLocation;

public class DeleteLocationLinearLayout {

    public static void onClickdDeleteLocationButton(MainActivity mainActivity) {
        mainActivity.findViewById(R.id.deleteLocationLinearLayout).setVisibility(View.GONE);
        ManagerOfTestLocation.stopChangeLocationService(mainActivity);
        ManagerOfTestLocation.cancelChangeLocation(mainActivity);
    }
}
