package by.black_pearl.cheloc.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import by.black_pearl.cheloc.R;

/**
 * Created by BlackPearl.
 */

public class TextView extends FrameLayout implements TextInterface {
    private android.widget.TextView mTextView;

    public TextView(Context context) {
        super(context);
        mTextView = (android.widget.TextView)
                LayoutInflater.from(context).inflate(R.layout.text_view, this, false);
        this.addView(mTextView);
    }

    @Override
    public void setText(CharSequence text) {
        this.mTextView.setText(text);
    }

    @Override
    public CharSequence getText() {
        return this.mTextView.getText();
    }

    @Override
    public void setGravity(int gravity) {
        this.mTextView.setGravity(gravity);
    }

    public void setTextLayoutParams(ViewGroup.LayoutParams params) {
        this.mTextView.setLayoutParams(params);
    }

    @Override
    public void setEms(int ems) {
        this.mTextView.setEms(ems);
    }

    @Override
    public void setInputType(int type) {
        this.mTextView.setInputType(type);
    }

    @Override
    public void setTextColor(int color) {
        this.mTextView.setTextColor(color);
    }
}
