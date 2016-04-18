package by.black_pearl.cheloc;

import android.Manifest;
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
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {

    MockLocationProvider mockLocationProvider;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.openLocationLinearLayoutButton).setOnClickListener(this);
        findViewById(R.id.openDeleteLocationLinearLayout).setOnClickListener(this);
        findViewById(R.id.setLocationButton).setOnClickListener(this);
        findViewById(R.id.deleteLocationButton).setOnClickListener(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        showLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));

    }

    private void changeLocation() {
        mockLocationProvider = new MockLocationProvider(this);
        String latitude = ((EditText) (findViewById(R.id.latitudeEditText))).getText().toString();
        String longtitude = ((EditText) (findViewById(R.id.longtitudeEditText))).getText().toString();
        String altitude = ((EditText) (findViewById(R.id.altitudeEditText))).getText().toString();

        mockLocationProvider.setLocation(Double.valueOf(latitude), Double.valueOf(longtitude),
                Integer.valueOf(altitude));
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
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

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);

        checkEnabeled();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    }

    /**
     * Called when the provider is enabled by the user.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */
    @Override
    public void onProviderEnabled(String provider) {
        checkEnabeled();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        checkEnabeled();
    }

    private void showLocation(Location location) {
        if(location == null){
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

    private void checkEnabeled() {
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            ((TextView)findViewById(R.id.endisGpsTextView)).setText("Enable: ");
        }
        else {
            ((TextView)findViewById(R.id.endisGpsTextView)).setText("Disable.");
        }
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            ((TextView)findViewById(R.id.endisNtwrkTextView)).setText("Enable: ");
        }
        else {
            ((TextView)findViewById(R.id.endisNtwrkTextView)).setText("Disable.");
        }
    }
}
