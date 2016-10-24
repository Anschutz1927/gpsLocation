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
    public static final int DEFAULT_EXTEND_TIME_60 = 60;

    /**
     * This is timer. The value of the measurement - 1 second.
     *
     * @param timerHandlerListener the listener that reacts at start, terminate and stop timer.
     */
    public TimerHandler(TimerHandlerListener timerHandlerListener) {
        this.mTimerHsndler = new Handler();
        this.mTtimeExtend = 0;
        this.mListener = timerHandlerListener;
    }

    /**
     * Run this method  to start timer.
     *
     * @param timeExtend_sec time, sec.
     */
    public void startTimer(int timeExtend_sec) {
        this.mTtimeExtend = timeExtend_sec;
        this.mIsStop = false;
        this.mListener.timerSterted();
        this.mTimerHsndler.post(this);
    }

    /**
     * Stop timer.
     */
    public void stopTimer() {
        this.mIsStop = true;
    }

    /**
     * Make loop for timer.
     */
    private void looper() {
        this.mTimerHsndler.postDelayed(this, 1000);
    }

    @Override
    public void run() {
        if(mTtimeExtend > 0) {
            mTtimeExtend--;
            if(!mIsStop) {
                looper();
            } else {
                mListener.timerTerminated();
            }
        }
        else {
            mListener.timerStopped();
        }
    }

    /**
     * Check stop the timer.
     *
     * @return true when timer is stop.
     */
    public boolean isTimerStop() {
        return mIsStop;
    }
}
