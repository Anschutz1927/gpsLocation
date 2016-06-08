package by.black_pearl.cheloc.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import by.black_pearl.cheloc.PropertiesCheloc;
import by.black_pearl.cheloc.R;
import by.black_pearl.cheloc.location.ManagerOfTestLocation;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener{

    private LocationManager locationManager;
    private PropertiesCheloc propertiesCheloc;
    private final String logTag = "<<<_MA_>>>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(logTag, "onCreate(...) top");
        findViewById(R.id.openLocationLinearLayoutButton).setOnClickListener(this);
        findViewById(R.id.openDeleteLocationLinearLayout).setOnClickListener(this);
        findViewById(R.id.setLocationButton).setOnClickListener(this);
        findViewById(R.id.setLocationServiceButton).setOnClickListener(this);
        findViewById(R.id.deleteLocationButton).setOnClickListener(this);
        findViewById(R.id.cancelSetLocationButton).setOnClickListener(this);
        findViewById(R.id.buttonCancelProps).setOnClickListener(this);
        findViewById(R.id.buttonSaveProps).setOnClickListener(this);

        ((NumberPicker) findViewById(R.id.serviceRunTimeNumberPicker)).setMinValue(0);
        ((NumberPicker) findViewById(R.id.serviceRunTimeNumberPicker)).setMaxValue(720);
        ((NumberPicker) findViewById(R.id.setSpeedNumberPicker)).setMinValue(0);
        ((NumberPicker) findViewById(R.id.setSpeedNumberPicker)).setMaxValue(250);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        ConsoleLinearLayout.addLineToConsole(this, "Getted System Service to locationManager.");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ConsoleLinearLayout.addLineToConsole(this, "No permissions to MOCK... canceled.");
            return;
        }
        try{
            ManagerOfTestLocation.showLocation(this,
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
            ConsoleLinearLayout.addLineToConsole(this, "Showed last location from gps provider.");
        }
        catch (Exception e) {
            ConsoleLinearLayout.addLineToConsole(this, "EXEPTION last location: " + e.getMessage());
        }
        Log.i(logTag, "onCreate(...) bottom");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(logTag, "onResume() top");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        ConsoleLinearLayout.addLineToConsole(this, "Setted requestLocationUpdates to 1000...");
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        }
        catch (Exception e) {
            ConsoleLinearLayout.addLineToConsole(this, "...set gps EXEPTION: " + e.getMessage());
            Log.i("GPS EXEPTION", "unfortunely: " + "locationManager.requestLocationUpdates" +
                    "(LocationManager.GPS_PROVIDER, 1000, 0, this);");
            Log.i("=========TAG=========", e.getMessage());
        }
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
        }
        catch (Exception e) {
            ConsoleLinearLayout.addLineToConsole(this, "...set network EXEPTION: " + e.getMessage());
            Log.i("NETWORK EXEPTION", "unfortunely: " + "locationManager.requestLocationUpdates" +
                    "(LocationManager.NETWORK_PROVIDER, 1000, 0, this);");
            Log.i("=========TAG=========", e.getMessage());
        }

        ManagerOfTestLocation.checkEnabled(this, locationManager);
        ConsoleLinearLayout.addLineToConsole(this, "checked enable providers.");
        Log.i(logTag, "onResume() bottom");
    }

    @Override
    public void onClick(View v) {
        ConsoleLinearLayout.addLineToConsole(this, "Clicked on " + ((Button)v).getText());
        switch (v.getId()) {
            case R.id.openLocationLinearLayoutButton:
                VoteLinearLayout.onClickOpenLocationLinearLayout(this);
                break;
            case R.id.openDeleteLocationLinearLayout:
                VoteLinearLayout.onClickDeleteOpenLocationLinearLayout(this);
                break;
            case R.id.setLocationButton:
                AddLocationLinearLayout.onClickSetLocationButton(this);
                break;
            case R.id.setLocationServiceButton:
                AddLocationLinearLayout.onClickSetLocationServiceButton(this);
                break;
            case R.id.deleteLocationButton:
                DeleteLocationLinearLayout.onClickdDeleteLocationButton(this);
                break;
            case R.id.buttonCancelProps:
                PropertiesLinearLayout.onClickButtonCancelProps(this);
                break;
            case R.id.buttonSaveProps:
                propertiesCheloc = PropertiesLinearLayout.onClickButtonSaveProps(this);
                break;
            case R.id.cancelSetLocationButton:
                AddLocationLinearLayout.onClickCancelSetLocationServiceButton(this);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i(logTag, "onPause() top");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);
        ConsoleLinearLayout.addLineToConsole(this, "RequestUpdates removed.");
        Log.i(logTag, "onPause() bottom");
    }

    @Override
    public void onDestroy() {
        Log.i(logTag, "onDestroy() top");
        Log.i(logTag, "onDestroy() bottom");
        super.onDestroy();
    }

    /**
     * Called when the location has changed.
     * <p>
     * <p> There are no restrictions on the use of the supplied Location object.
     *
     * @param location The new location, as a Location object.
     */
    @Override
    public void onLocationChanged(Location location) {
        ManagerOfTestLocation.showLocation(this, location);
    }

    /**
     * Called when the provider status changes. This method is called when
     * a provider is unable to fetch a location or if the provider has recently
     * become available after a period of unavailability.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     * @param status   {@link LocationProvider#OUT_OF_SERVICE} if the
     *                 provider is out of service, and this is not expected to change in the
     *                 near future; {@link LocationProvider#TEMPORARILY_UNAVAILABLE} if
     *                 the provider is temporarily unavailable but is expected to be available
     *                 shortly; and {@link LocationProvider#AVAILABLE} if the
     *                 provider is currently available.
     * @param extras   an optional Bundle which will contain provider specific
     *                 status variables.
     *                 <p>
     *                 <p> A number of common key/value pairs for the extras Bundle are listed
     *                 below. Providers that use any of the keys on this list must
     *                 provide the corresponding value as described below.
     *                 <p>
     *                 <ul>
     *                 <li> satellites - the number of satellites used to derive the fix
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        ConsoleLinearLayout.addLineToConsole(this, "Status changed: provider = " + provider +
                ", status: " + String.valueOf(status) + ".");
    }

    /**
     * Called when the provider is enabled by the user.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */
    @Override
    public void onProviderEnabled(String provider) {
        ManagerOfTestLocation.checkEnabled(this, locationManager);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        ManagerOfTestLocation.showLocation(this, locationManager.getLastKnownLocation(provider));
    }

    /**
     * Called when the provider is disabled by the user. If requestLocationUpdates
     * is called on an already disabled provider, this method is called
     * immediately.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */
    @Override
    public void onProviderDisabled(String provider) {
        ManagerOfTestLocation.checkEnabled(this, locationManager);
        ConsoleLinearLayout.addLineToConsole(this, "Provider disabled: " + provider + ".");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Show console...");
        menu.add(1, 1, 1, "Close console");
        menu.add(2, 2, 2, "Open properties");
        menu.add(3, 3, 3, "Close");
        menu.setGroupVisible(0, false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                (findViewById(R.id.consoleLinearLayout)).setVisibility(View.VISIBLE);
                break;
            case 1:
                (findViewById(R.id.consoleLinearLayout)).setVisibility(View.GONE);
                break;
            case 2:
                (findViewById(R.id.propsScrollView)).setVisibility(View.VISIBLE);
                break;
            case 3:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(menu.getItem(0).isVisible()) {
            menu.setGroupVisible(0, false);
            menu.setGroupVisible(1, true);
        }
        else {
            menu.setGroupVisible(0, true);
            menu.setGroupVisible(1, false);
        }
        return super.onPrepareOptionsMenu(menu);
    }
}
