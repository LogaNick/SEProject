package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class RecordActivity extends AppCompatActivity implements OnMapReadyCallback {

    /** Minimum time between location refresh, in ms */
    private static final long REFRESH_TIME = 5000;
    /** Minimum dist between location refresh, in metres */
    private static final float MIN_DISTANCE = 3;
    private static final int ACCESS_FINE_LOCATION = 0;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15f));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        ((MapFragment) getFragmentManager().findFragmentById(R.id.record_map))
                .getMapAsync(this);

        setupLocationFields();
        if (checkForLocPermission()) {
            requestLocationUpdates();
        }
    }

    /**
     *
     * @return true, iff permission has already been granted, else false
     */
    private boolean checkForLocPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION);
            return false;
        }
        else
            return true;
    }

    private void setupLocationFields() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    private void requestLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, REFRESH_TIME, MIN_DISTANCE, locationListener);
        } catch (SecurityException e) {
            //TODO Ask user for permission or something???
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocationUpdates();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

}
