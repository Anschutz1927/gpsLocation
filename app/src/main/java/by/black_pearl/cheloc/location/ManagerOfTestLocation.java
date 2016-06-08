package by.black_pearl.cheloc.location;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.CheckBox;
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
        double speed = 3.235;
        ConsoleLinearLayout.addLineToConsole(mainActivity, "\n -added new mock provider.");
        if(((CheckBox)mainActivity.findViewById(R.id.driveModeCheckBox)).isChecked()) {
            speed += 56;
        }

        mockLocationProvider.setLocation(Double.valueOf(latitude), Double.valueOf(longtitude),
                Double.valueOf(altitude), speed);
    }

    public static void startChangeLocationService(MainActivity mainActivity) {
        Intent intent = new Intent(mainActivity.getBaseContext(), SetterMockLocationService.class);
        String latitude = ((EditText) (mainActivity.findViewById(R.id.latitudeEditText))).getText().toString();
        String longtitude = ((EditText) (mainActivity.findViewById(R.id.longtitudeEditText))).getText().toString();
        String altitude = ((EditText) (mainActivity.findViewById(R.id.altitudeEditText))).getText().toString();
        Double speed = 1.1;
        if(((CheckBox)mainActivity.findViewById(R.id.driveModeCheckBox)).isChecked()) {
            speed += 15.8;
        }

        intent.putExtra("lat", Double.valueOf(latitude));
        intent.putExtra("lon", Double.valueOf(longtitude));
        intent.putExtra("alt", Double.valueOf(altitude));
        intent.putExtra("speed", speed);

        try {
            mainActivity.stopService(intent);
        } catch (Exception e) {
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

    public static Location getLocation(MainActivity mainActivity) {
        LocationManager locationManager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return new Location(LocationManager.GPS_PROVIDER);
        }
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    public static void showLocation(MainActivity mainActivity, Location location) {
        if(location == null){
            ConsoleLinearLayout.addLineToConsole(mainActivity, "location is null.");
            return;
        }
        String strLocation = location.getLatitude() + "; " +
                location.getLongitude() + "; " + location.getAltitude() + "; " +
                location.getSpeed() + ";";
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
