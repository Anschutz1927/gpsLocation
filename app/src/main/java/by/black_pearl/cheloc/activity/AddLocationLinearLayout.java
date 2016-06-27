package by.black_pearl.cheloc.activity;

import android.view.View;

import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.location.ManagerOfTestLocation;

public class AddLocationLinearLayout {

    public static void onClickSetLocationButton(MainActivity mainActivity) {
        setVisibleGoneCurLayout(mainActivity);
        ManagerOfTestLocation.changeLocation(mainActivity);
    }

    public static void onClickSetLocationServiceButton(MainActivity mainActivity) {
        setVisibleGoneCurLayout(mainActivity);
        ManagerOfTestLocation.startChangeLocationService(mainActivity);
    }

    public static void onClickCancelSetLocationServiceButton(MainActivity mainActivity) {
        setVisibleGoneCurLayout(mainActivity);
    }

    private static void setVisibleGoneCurLayout(MainActivity mainActivity) {
        mainActivity.findViewById(R.id.addLocationScrollView).setVisibility(View.GONE);
    }
}
