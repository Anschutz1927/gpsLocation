package by.black_pearl.cheloc.activity;

import android.view.ViewGroup;

/**
 * Interface to access to some methods for EditText and TextView.
 */
public interface TextInterface {
    void setText(CharSequence text);

    CharSequence getText();

    void setGravity(int gravity);

    void setEms (int ems);

    void setInputType (int type);

    void setTextColor(int color);

    void setTextLayoutParams(ViewGroup.LayoutParams params);
}
