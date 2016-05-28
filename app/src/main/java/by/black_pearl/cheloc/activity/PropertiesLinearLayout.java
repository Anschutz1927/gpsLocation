package by.black_pearl.cheloc.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import by.black_pearl.cheloc.PropertiesCheloc;
import by.black_pearl.cheloc.R;

public class PropertiesLinearLayout {

    public static void onClickButtonCancelProps(MainActivity mainActivity) {
        (mainActivity.findViewById(R.id.propsScrollView)).setVisibility(View.GONE);
    }

    public static PropertiesCheloc onClickButtonSaveProps(MainActivity mainActivity) {
        (mainActivity.findViewById(R.id.propsScrollView)).setVisibility(View.GONE);
        return getProperties(mainActivity);
    }

    private static PropertiesCheloc getProperties(MainActivity mainActivity) {
        PropertiesCheloc propertiesCheloc = new PropertiesCheloc();
        propertiesCheloc.setServiceRunTime(((NumberPicker)
                mainActivity.findViewById(R.id.serviceRunTimeNumberPicker)).getValue());
        propertiesCheloc.setUpdateTime(Double.valueOf(((EditText)
                mainActivity.findViewById(R.id.updateTimePropEditText)).getText().toString()));
       return propertiesCheloc;
    }
}
