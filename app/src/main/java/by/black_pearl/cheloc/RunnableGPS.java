package by.black_pearl.cheloc;

import android.util.Log;

public class RunnableGPS implements Runnable {
    private MockLocationServiceProvider mockLocationServiceProvider;
    private final String logTag = "RGPS";

    @Override
    public void run() {
        try {
            mockLocationServiceProvider.setMockLocation(54.92, 28.59, 201);
        }
        catch (Exception e) {
            Log.i(logTag, e.getMessage());
        }
    }

    public RunnableGPS(MockLocationServiceProvider mockLocationServiceProvider) {
        this.mockLocationServiceProvider = mockLocationServiceProvider;
    }
}
