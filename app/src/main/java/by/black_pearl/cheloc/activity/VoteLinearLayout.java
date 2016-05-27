package by.black_pearl.cheloc.activity;

import android.view.View;

import by.black_pearl.cheloc.R;

public class VoteLinearLayout {

    public static void onClickOpenLocationLinearLayout(MainActivity mainActivity) {
        mainActivity.findViewById(R.id.addLocationLinearLayout).setVisibility(View.VISIBLE);
    }

    public static void onClickDeleteOpenLocationLinearLayout(MainActivity mainActivity) {
        mainActivity.findViewById(R.id.deleteLocationLinearLayout).setVisibility(View.VISIBLE);
    }
}
