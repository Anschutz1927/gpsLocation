package by.black_pearl.cheloc.activity;

import android.widget.TextView;

import by.black_pearl.cheloc.R;

public class ConsoleLinearLayout {

    public static void addLineToConsole(MainActivity mainActivity, String text) {
        ((TextView)mainActivity.findViewById(R.id.console)).append("\n - " + text);
    }
}
