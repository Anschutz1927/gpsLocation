package by.black_pearl.cheloc.location;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.activity.ConsoleLinearLayout;
import by.black_pearl.cheloc.activity.MainActivity;

public class ManagerOfTestLocation {

    public static void changeLocation(MainActivity mainActivity) {
        MockLocationProvider mockLocationProvider = new MockLocationProvider(mainActivity);
        String latitude = ((EditText) (mainActivity.findViewById(R.id.latitudeEditText))).getText().toString();
        String longtitude = ((EditText) (mainActivity.findViewById(R.id.longtitudeEditText))).getText().toString();
        String altitude = ((EditText) (mainActivity.findViewById(R.id.altitudeEditText))).getText().toString();
        ConsoleLinearLayout.addLineToConsole(mainActivity, "\n -added new mock provider.");

        mockLocationProvider.setLocation(Double.valueOf(latitude), Double.valueOf(longtitude),
                Integer.valueOf(altitude));
    }

    public static void startChangeLocationService(MainActivity mainActivity) {
        Intent intent = new Intent(mainActivity.getBaseContext(), SetterMockLocationService.class);
        String latitude = ((EditText) (mainActivity.findViewById(R.id.latitudeEditText))).getText().toString();
        String longtitude = ((EditText) (mainActivity.findViewById(R.id.longtitudeEditText))).getText().toString();
        String altitude = ((EditText) (mainActivity.findViewById(R.id.altitudeEditText))).getText().toString();
        intent.putExtra("lat", Double.valueOf(latitude));
        intent.putExtra("lon", Double.valueOf(longtitude));
        intent.putExtra("alt", Integer.valueOf(altitude));
        try {
            mainActivity.stopService(intent);
        }
        catch (Exception e) {
            Log.i("<<<_MOTL_>>>", e.getMessage());
        }
        mainActivity.startService(intent);
    }

    public static void cancelChangeLocation(MainActivity mainActivity) {
        MockLocationProvider mockLocationProvider = new MockLocationProvider(mainActivity);
        mockLocationProvider.cancelSetLocation();
    }

    public static void stopChangeLocationService(MainActivity mainActivity) {
        mainActivity.stopService(new Intent(mainActivity, SetterMockLocationService.class));
    }

    public static void showLocation(MainActivity mainActivity, Location location) {
        if(location == null){
            ConsoleLinearLayout.addLineToConsole(mainActivity, "location is null.");
            return;
        }
        String strLocation = location.getLatitude() + "; " +
                location.getLongitude() + "; " + location.getAltitude() + ";";
        if(location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            ((TextView)mainActivity.findViewById(R.id.curGpsLocationTextView)).setText(strLocation);
        }
        else {
            ((TextView)mainActivity.findViewById(R.id.curNtwrkLocationTextView)).setText(strLocation);
        }
    }

    public static void checkEnabled(MainActivity mainActivity, LocationManager locationManager) {
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            ((TextView)mainActivity.findViewById(R.id.endisGpsTextView)).setText("Enable: ");
            ConsoleLinearLayout.addLineToConsole(mainActivity, "Gps enable.");
        }
        else {
            ((TextView)mainActivity.findViewById(R.id.endisGpsTextView)).setText("Disable.");
            ConsoleLinearLayout.addLineToConsole(mainActivity, "Gps disable.");
        }
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            ((TextView)mainActivity.findViewById(R.id.endisNtwrkTextView)).setText("Enable: ");
            ConsoleLinearLayout.addLineToConsole(mainActivity, "network enable.");
        }
        else {
            ((TextView)mainActivity.findViewById(R.id.endisNtwrkTextView)).setText("Disable.");
            ConsoleLinearLayout.addLineToConsole(mainActivity, "network disable.");
        }
    }
}
