package by.black_pearl.cheloc;

import android.Manifest;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener{

    private MockLocationProvider mockLocationProvider;
    private LocationManager locationManager;
    private TextView console;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.openLocationLinearLayoutButton).setOnClickListener(this);
        findViewById(R.id.openDeleteLocationLinearLayout).setOnClickListener(this);
        findViewById(R.id.setLocationButton).setOnClickListener(this);
        findViewById(R.id.deleteLocationButton).setOnClickListener(this);
        console = (TextView)findViewById(R.id.console);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        console.append("\n-getted System Service to locationManager.");

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
            console.append("\n -no permissions to MOCK... canceled.");
            return;
        }
        try{
            showLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
            console.append("\n -showed last location from gps provider.");
        }
        catch (Exception e) {
            console.append("\n -EXEPTION last location: " + e.getMessage());
        }

    }

    private void showLocation(Location location) {
        if(location == null){
            console.append("\n -location null.");
            return;
        }
        String strLocation = location.getLatitude() + "; " +
                location.getLongitude() + "; " + location.getAltitude() + ";";
        if(location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            ((TextView)findViewById(R.id.curGpsLocationTextView)).setText(strLocation);
        }
        else {
            ((TextView)findViewById(R.id.curNtwrkLocationTextView)).setText(strLocation);
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        console.append("\n -clicked on " + ((Button)v).getText());
        switch (v.getId()) {
            case R.id.openLocationLinearLayoutButton:
                findViewById(R.id.voteLinearLayuot).setVisibility(View.GONE);
                findViewById(R.id.addLocationLinearLayout).setVisibility(View.VISIBLE);
                break;
            case R.id.openDeleteLocationLinearLayout:
                findViewById(R.id.voteLinearLayuot).setVisibility(View.GONE);
                findViewById(R.id.deleteLocationLinearLayout).setVisibility(View.VISIBLE);
                break;
            case R.id.setLocationButton:
                findViewById(R.id.addLocationLinearLayout).setVisibility(View.GONE);
                findViewById(R.id.voteLinearLayuot).setVisibility(View.VISIBLE);
                this.changeLocation();
                break;
            case R.id.deleteLocationButton:
                findViewById(R.id.deleteLocationLinearLayout).setVisibility(View.GONE);
                findViewById(R.id.voteLinearLayuot).setVisibility(View.VISIBLE);
                if (mockLocationProvider == null) {
                    mockLocationProvider = new MockLocationProvider(this);
                }
                mockLocationProvider.cancelSetLocation();
                break;
        }
    }

    private void changeLocation() {
        mockLocationProvider = new MockLocationProvider(this);
        String latitude = ((EditText) (findViewById(R.id.latitudeEditText))).getText().toString();
        String longtitude = ((EditText) (findViewById(R.id.longtitudeEditText))).getText().toString();
        String altitude = ((EditText) (findViewById(R.id.altitudeEditText))).getText().toString();
        console.append("\n -added new mock provider.");

        mockLocationProvider.setLocation(Double.valueOf(latitude), Double.valueOf(longtitude),
                Integer.valueOf(altitude));
    }

    @Override
    protected void onResume() {
        super.onResume();

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
        console.append("\n -setting requestLocationUpdates to 1000...");
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        }
        catch (Exception e) {
            console.append("\n -...set gps EXEPTION: " + e.getMessage());
            Log.i("GPS EXEPTION", "unfortunely: " + "locationManager.requestLocationUpdates" +
                    "(LocationManager.GPS_PROVIDER, 1000, 0, this);");
            Log.i("=========TAG=========", e.getMessage());
        }
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
        }
        catch (Exception e) {
            console.append("\n -...set network EXEPTION: " + e.getMessage());
            Log.i("NETWORK EXEPTION", "unfortunely: " + "locationManager.requestLocationUpdates" +
                            "(LocationManager.NETWORK_PROVIDER, 1000, 0, this);");
            Log.i("=========TAG=========", e.getMessage());
        }

        checkEnabled();
        console.append("\n -checked enable providers.");
    }

    private void checkEnabled() {
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            ((TextView)findViewById(R.id.endisGpsTextView)).setText("Enable: ");
            console.append("\n -gps enable.");
        }
        else {
            ((TextView)findViewById(R.id.endisGpsTextView)).setText("Disable.");
            console.append("\n -gps disable.");
        }
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            ((TextView)findViewById(R.id.endisNtwrkTextView)).setText("Enable: ");
            console.append("\n -network enable.");
        }
        else {
            ((TextView)findViewById(R.id.endisNtwrkTextView)).setText("Disable.");
            console.append("\n -network disable.");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

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
        console.append("\n -requestUpdates removed.");
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
        showLocation(location);
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
        console.append("\n -status changed: provider = " + provider +
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
        checkEnabled();
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
        showLocation(locationManager.getLastKnownLocation(provider));
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
        checkEnabled();
        console.append("\n -provider disabled: " + provider + ".");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Show console...");
        menu.add(1, 1, 1, "Close console");
        menu.add(2, 2, 2, "Close");
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

    public void addLogToConsole(String logText) {
        console.append("\n -" + logText);
    }
}
