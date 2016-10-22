package by.black_pearl.cheloc;

import android.os.Handler;

/**
 * Created by BlackPearl-net.
 */

public class TimerHandler implements Runnable{
    private Handler mTimerHsndler;
    private int mTtimeExtend;
    private boolean mIsStop = true;
    private TimerHandlerListener mListener;
    public static final int DEFAULT_EXTEND_TIME_3 = 3000;

    public TimerHandler(TimerHandlerListener timerHandlerListener) {
        this.mTimerHsndler = new Handler();
        this.mTtimeExtend = 0;
        this.mListener = timerHandlerListener;
    }

    public void startTimer(int timeExtend) {
        this.mTtimeExtend = timeExtend;
        this.mIsStop = false;
        this.mTimerHsndler.post(this);
    }

    public void stopTimer() {
        this.mIsStop = true;
    }

    private void looper() {
        this.mTimerHsndler.postDelayed(this, 1000);
    }

    @Override
    public void run() {
        if(mTtimeExtend > 0) {
            mTtimeExtend--;
            if(!mIsStop) {
                looper();
            }
        }
        else {
            mListener.timerStopped();
        }
    }

    public boolean isTimerStop() {
        return mIsStop;
    }
}
