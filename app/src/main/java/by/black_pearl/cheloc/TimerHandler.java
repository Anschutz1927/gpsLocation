package by.black_pearl.cheloc;

import android.os.Handler;

/**
 * Created by BlackPearl-net.
 */

public class TimerHandler implements Runnable {
    public static final int DEFAULT_EXTEND_TIME_60 = 60;

    private Handler mTimerHandler;
    private int mTimeExtend;
    private boolean mIsStop = true;
    private TimerHandlerListener mListener;
    private State mCurrentState = State.STOPPED;

    /**
     * This is timer. The value of the measurement - 1 second.
     * @param timerHandlerListener the listener that reacts at start, terminate and stop timer.
     */
    public TimerHandler(TimerHandlerListener timerHandlerListener) {
        this.mTimerHandler = new Handler();
        this.mTimeExtend = 0;
        this.mListener = timerHandlerListener;
    }

    /**
     * Run this method  to start timer.
     * @param timeExtend_sec time, sec.
     */
    public void startTimer(int timeExtend_sec) {
        this.mTimeExtend = timeExtend_sec;
        this.mIsStop = false;
        this.mListener.timerStart();
        this.mCurrentState = State.STARTED;
        this.mTimerHandler.post(this);
    }

    /**
     * Run to pause timer.
     */
    public void pauseTimer() {
        this.mCurrentState = State.PAUSED;
        this.mListener.timerPause();
    }

    /**
     * Stop timer.
     */
    public void stopTimer() {
        this.mIsStop = true;
        this.mCurrentState = State.STOPPED;
        this.mListener.timerStop();
    }

    /**
     * Make loop for timer.
     */
    private void looper() {
        this.mTimerHandler.postDelayed(this, 1000);
    }

    @Override
    public void run() {
        if (mTimeExtend > 0) {
            mTimeExtend--;
            if (this.mCurrentState == State.STARTED) {
                looper();
            } else {
                mListener.timerPause();
            }
        }
        else {
            mListener.timerStop();
        }
    }

    /**
     * Check stop the timer.
     * @return true when timer is stop.
     */
    public boolean isTimerStop() {
        return mIsStop;
    }

    /**
     * Public interface to callback from TimerHandler.
     */
    public interface TimerHandlerListener {

        void timerStart();

        void timerPause();

        void timerStop();
    }

    public enum State {STARTED, PAUSED, STOPPED}
}
