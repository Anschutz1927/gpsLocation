package by.black_pearl.cheloc.location;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Random;

import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.activity.ConsoleLinearLayout;
import by.black_pearl.cheloc.activity.MainActivity;

public class ManagerOfTestLocation {

    public static void changeLocation(final MainActivity mainActivity) {
        ManagerOfTestLocation.stopChangeLocationService(mainActivity);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MockLocationProvider mockLocationProvider = new MockLocationProvider(mainActivity);
                String latitude = ((EditText) (mainActivity.findViewById(R.id.latitudeEditText))).getText().toString();
                String longtitude = ((EditText) (mainActivity.findViewById(R.id.longtitudeEditText))).getText().toString();
                String altitude = ((EditText) (mainActivity.findViewById(R.id.altitudeEditText))).getText().toString();
                double speed = getRandomSpeed(((RadioGroup)
                        mainActivity.findViewById(R.id.speedModeRadioGroup)).getCheckedRadioButtonId());//3.235;
                ConsoleLinearLayout.addLineToConsole(mainActivity, "\n -added new mock provider.");

                mockLocationProvider.setLocation(Double.valueOf(latitude), Double.valueOf(longtitude),
                        Double.valueOf(altitude), speed);
            }
        }, 1000);
    }

    public static void startChangeLocationService(MainActivity mainActivity) {
        Intent intent = new Intent(mainActivity.getBaseContext(), SetterMockLocationService.class);
        String latitude = ((EditText) (mainActivity.findViewById(R.id.latitudeEditText))).getText().toString();
        String longtitude = ((EditText) (mainActivity.findViewById(R.id.longtitudeEditText))).getText().toString();
        String altitude = ((EditText) (mainActivity.findViewById(R.id.altitudeEditText))).getText().toString();
        int speedMode = getSpeedMode(((RadioGroup)
                mainActivity.findViewById(R.id.speedModeRadioGroup)).getCheckedRadioButtonId());
        boolean randPos = ((CheckBox) mainActivity.findViewById(R.id.randomPositionCheckBox)).isChecked();

        intent.putExtra("lat", Double.valueOf(latitude));
        intent.putExtra("lon", Double.valueOf(longtitude));
        intent.putExtra("alt", Double.valueOf(altitude));
        intent.putExtra("speedMode", speedMode);
        intent.putExtra("randPos", randPos);
        if(getLocation(mainActivity) != null) {
            intent.putExtra("bearing", getLocation(mainActivity).getBearing());
        }
        else {
            intent.putExtra("bearing", 0);
        }

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
        String strLocation = "Широта: " + location.getLatitude() + ";\n" +
                "Долгота: " + location.getLongitude() + ";\n" +
                "Высота: " + location.getAltitude() + ";\n" +
                "Скорость: " + location.getSpeed() + ";\n" +
                "Курс: " + location.getBearing();
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

    private static double getRandomSpeed(int mode) {
        Random random = new Random();
        switch (mode) {
            case R.id.walkModeRadioButton:
                return random.nextInt(999)/1000. + random.nextInt(5);
            case R.id.driveModeRadioButton:
                return random.nextInt(999)/1000. + random.nextInt(20) + 40;
        }
        return 0.0;
    }

    private static int getSpeedMode(int mode) {
        switch (mode) {
            case R.id.walkModeRadioButton:
                return 1;
            case R.id.driveModeRadioButton:
                return 2;
        }
        return 0;
    }

}
