package by.black_pearl.cheloc.location.service;

import android.os.Binder;
import android.util.Log;

/**
 * class for bind service.
 */
public class ServiceBinder extends Binder {
    private final String LOG_TAG = "ServiceBinder";
    private ChelocService service;

    public ServiceBinder(ChelocService service) {
        Log.i(LOG_TAG, "ServiceBinder");
        this.service = service;
    }

    public ChelocService getService() {
        Log.i(LOG_TAG, "getService");
        return this.service;
    }
}
