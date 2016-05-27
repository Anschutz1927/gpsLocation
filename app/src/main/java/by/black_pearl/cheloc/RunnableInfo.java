package by.black_pearl.cheloc;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import by.black_pearl.cheloc.location.RunnableGPS;

public class RunnableInfo implements Runnable {
    private Context context;
    private RunnableGPS runnableGPS;
    private Handler handlerViewer;

    public RunnableInfo(Handler handlerViewer, Context context, RunnableGPS runnableGPS) {
        this.handlerViewer = handlerViewer;
        this.runnableGPS = runnableGPS;
        this.context = context;
    }
    @Override
    public void run() {
        Toast.makeText(context, "Location setted "
                + runnableGPS.getSettsServiceLocationsCounter().getCount() +
                " times.", Toast.LENGTH_SHORT).show();
        runnableGPS.getSettsServiceLocationsCounter().setCountToZero();
        handlerViewer.postDelayed(this, 60000);
    }
}
