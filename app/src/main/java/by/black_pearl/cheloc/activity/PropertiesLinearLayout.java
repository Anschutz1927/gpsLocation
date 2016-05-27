package by.black_pearl.cheloc.activity;

import android.view.View;

import by.black_pearl.cheloc.R;

public class PropertiesLinearLayout {

    public static void onClickButtonCancelProps(MainActivity mainActivity) {
        (mainActivity.findViewById(R.id.propsScrollView)).setVisibility(View.GONE);
    }

    public static void onClickButtonSaveProps(MainActivity mainActivity) {
        (mainActivity.findViewById(R.id.propsScrollView)).setVisibility(View.GONE);
    }
}
