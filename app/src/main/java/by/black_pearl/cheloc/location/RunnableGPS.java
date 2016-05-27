package by.black_pearl.cheloc.location;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class RunnableGPS implements Runnable {
    private MockLocationServiceProvider mockLocationServiceProvider;
    private SettsServiceLocationsCounter settsServiceLocationsCounter =
            new SettsServiceLocationsCounter();
    private Handler handlerSetter;
    private Coordinates coordinates;
    private final String logTag = "<<<_RGPS_>>>";
    private int time;

    @Override
    public void run() {
        try {
            mockLocationServiceProvider.setMockLocation(coordinates);
        }
        catch (Exception e) {
            Log.i(logTag, e.getMessage());
        }
        this.settsServiceLocationsCounter.addCount();
        this.handlerSetter.postDelayed(this, time);
    }

    public RunnableGPS(Handler handlerSetter, Context context, Coordinates coordinates) {
        this.handlerSetter = handlerSetter;
        this.mockLocationServiceProvider =
                new MockLocationServiceProvider(context);
        this.coordinates = coordinates;
        this.time = 399;
    }

    public SettsServiceLocationsCounter getSettsServiceLocationsCounter() {
        return this.settsServiceLocationsCounter;
    }

    public void stopMockLocation() {
        mockLocationServiceProvider.removeTestProviders();
    }

}
