package by.black_pearl.cheloc.activity;

import android.content.Context;
import android.os.Build;

/**
 * Created by netbook on 05.09.2016.
 */
public class EditText extends android.widget.EditText implements TextInterface {

    public EditText(Context context) {
        super(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            this.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        }
    }
}
