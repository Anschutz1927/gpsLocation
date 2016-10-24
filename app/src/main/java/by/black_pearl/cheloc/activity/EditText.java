package by.black_pearl.cheloc.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import by.black_pearl.cheloc.R;

/**
 * Created by BlackPearl.
 */

public class EditText extends FrameLayout implements TextInterface {
    private android.widget.EditText mEditText;

    public EditText(Context context) {
        super(context);
        mEditText = (android.widget.EditText)
                LayoutInflater.from(context).inflate(R.layout.edit_text, this, false);
        this.addView(mEditText);
    }

    @Override
    public void setText(CharSequence text) {
        this.mEditText.setText(text);
    }

    @Override
    public CharSequence getText() {
        return this.mEditText.getText();
    }

    @Override
    public void setGravity(int gravity) {
        this.mEditText.setGravity(gravity);
    }

    @Override
    public void setEms(int ems) {
        this.mEditText.setEms(ems);
    }

    @Override
    public void setInputType(int type) {
        this.mEditText.setInputType(type);
    }

    @Override
    public void setTextColor(int color) {
        this.mEditText.setTextColor(color);
    }

    @Override
    public void setTextLayoutParams(ViewGroup.LayoutParams params) {
        this.mEditText.setLayoutParams(params);
    }

    public void requestTextFocus() {
        this.mEditText.requestFocus();
    }

    public void setTextHint(CharSequence hint) {
        this.mEditText.setHint(hint);
    }
}