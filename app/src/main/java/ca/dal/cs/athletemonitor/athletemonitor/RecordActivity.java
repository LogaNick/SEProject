package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;

import static ca.dal.cs.athletemonitor.athletemonitor.UserInformationActivity.USER_ID;

public class RecordActivity extends AppCompatActivity implements OnMapReadyCallback {

    /** Minimum time between location refresh, in ms */
    private static final long REFRESH_TIME = 500;
    /** Minimum dist between location refresh, in metres */
    private static final float MIN_DISTANCE = 3;
    private static final int ACCESS_FINE_LOCATION = 0;

    private String userId;
    private boolean isRecording = false;
    private boolean isPaused = false;
    private List<Location> locationList = new LinkedList<>();
    private Polyline currentRoute = null;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.zoomTo(16f));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(current));

            if (isRecording && !isPaused) {
                locationList.add(location);

                if (currentRoute == null) {
                    currentRoute = mMap.addPolyline(new PolylineOptions().color(Color.BLUE).clickable(false));
                    currentRoute.setStartCap(new RoundCap());
                    currentRoute.setEndCap(new RoundCap());
                }
                currentRoute.setPoints(convertListToLatLng());
            }
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

        //TODO uncomment
//        Intent intent = getIntent();
//        String userId = intent.getStringExtra(USER_ID);
//        this.userId = userId;
        this.userId = "zachary";

    }

    private List<LatLng> convertListToLatLng() {

        List<LatLng> latLngs = new LinkedList<>();

        for (Location loc : locationList)
            latLngs.add(new LatLng(loc.getLatitude(), loc.getLongitude()));

        return latLngs;
    }

    //TODO change text while paused
    public void togglePauseStatus(View v) {
        isPaused = !isPaused;
    }

    //TODO make the button change text when recording
    public void toggleRecordStatus(View v) {
        isRecording = !isRecording;

        if (!isRecording) {
            saveToFirebase();
        }
    }

    private void saveToFirebase() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef
                = db.getReference(getString(R.string.activity_record_firebase, userId));

        //TODO Add time
        RecordedWorkout workout = new RecordedWorkout(locationList, 0);
        String key = myRef.push().getKey();
        Task task = myRef.child(key).setValue(workout);
        task.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    //TODO GOOD
                }
                else {
                    //TODO BAD
                }
            }
        });
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
