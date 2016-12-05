package by.black_pearl.cheloc;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by BLACK_Pearl.
 */

public class MoreSetup extends LinearLayout implements View.OnClickListener {
    private Context mContext;
    private LinearLayout mContentOnceLayout;
    private FloatingActionButton mAddFAB;
    private Button mCancelMoreButton;
    private Button mOkMoreButton;

    public MoreSetup(Context context) {
        super(context);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.activity_main_setup_more, this, true);
        this.mContentOnceLayout = (LinearLayout) findViewById(R.id.contentOnceLayout);
        this.mAddFAB = (FloatingActionButton) findViewById(R.id.addFAB);
        this.mCancelMoreButton = (Button) findViewById(R.id.cancelMoreButton);
        this.mOkMoreButton = (Button) findViewById(R.id.okMoreButton);
        this.mContentOnceLayout.addView(new OnceSetup(context));
        setListeners();
    }

    private void setListeners() {
        this.mAddFAB.setOnClickListener(this);
        this.mCancelMoreButton.setOnClickListener(this);
        this.mOkMoreButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addFAB:
                mContentOnceLayout.addView(new OnceSetup(mContext));
                break;
            case R.id.cancelMoreButton:
                LinearLayout mainLinearLyaout = (LinearLayout) (getParent().getParent().getParent());
                break;
            case R.id.okMoreButton:
                break;
        }
    }
}
