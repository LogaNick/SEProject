package ca.dal.cs.athletemonitor.athletemonitor;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;

import com.google.android.gms.location.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class contains functionality to allow a user to record their workout
 * while it is in progress.
 */
public class RecordActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int ACCESS_FINE_LOCATION = 0;
    private static final String DATABASE_REFERENCE = "DATABASE_REFERENCE";
    private static final String ELAPSED_TIME = "ELAPSED_TIME";
    private static final String LOCATION_LIST = "LOCATION_LIST";

    private Chronometer timer;
    private ImageButton recordButton;
    private ImageButton pauseButton;

    private long timeWhenStopped = 0L;
    private User user;
    private boolean isRecording = false;
    private boolean isPaused = false;
    private ArrayList<Location> locationList = new ArrayList<>();
    private Polyline currentRoute = null;
    private GoogleMap mMap;
    private FusedLocationProviderClient locationProviderClient;
    private LocationRequest locationRequest;
    private SettingsClient settingsClient;
    private LocationSettingsRequest locationSettingsRequest;
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            Location location = locationResult.getLastLocation();
            LatLng current = new LatLng(location.getLatitude(), location.getLongitude());

            if (isRecording && !isPaused) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
                locationList.add(location);

                if (currentRoute == null) {
                    currentRoute = mMap.addPolyline(new PolylineOptions().color(Color.BLUE).clickable(false));
                    currentRoute.setStartCap(new RoundCap());
                    currentRoute.setEndCap(new RoundCap());
                }
                currentRoute.setPoints(convertListToLatLng());
            }
        }
    };

    /**
     * This class is used to allow a user to save their workout. It appears
     * as a pop-up dialog when recording is complete.
     */
    public static class RecordSaveFragment extends DialogFragment {

        /**
         * This method is called to create the dialog.
         * @param savedInstanceState not used
         * @return the dialog
         */
        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.activity_record_save_dialog);
            builder.setPositiveButton(
                    R.string.activity_record_yes,
                    new DialogInterface.OnClickListener() {
                        /**
                         * This method saves the user's workout to Firebase.
                         * @param dialog not used
                         * @param which not used
                         */
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Bundle bundle = getArguments();
                            String dbRef = bundle.getString(DATABASE_REFERENCE);
                            long time = bundle.getLong(ELAPSED_TIME);
                            ArrayList<Location> locationList = bundle.getParcelableArrayList(LOCATION_LIST);
                            saveToFirebase(dbRef, time, locationList);
                        }
                    }
            );
            builder.setNegativeButton(
                    R.string.activity_record_no,
                    new DialogInterface.OnClickListener() {
                        /**
                         * This method does not do anything, but must be here as per the interface.
                         * @param dialog not used
                         * @param which not used
                         */
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
            });
            return builder.create();
        }

    }

    /**
     * This method sets up the fields and handles GPS permissions.
     * @param savedInstanceState not used
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        ((MapFragment) getFragmentManager().findFragmentById(R.id.record_map))
                .getMapAsync(this);

        timer = findViewById(R.id.record_chrono);
        recordButton = findViewById(R.id.record_button);
        pauseButton = findViewById(R.id.pause_button);

        if (checkForLocPermission()) {
            createLocationParameters();
            requestLocationUpdates();
        }
        else {
            requestLocPermissions();
        }

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
    }

    private void requestLocPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                ACCESS_FINE_LOCATION);
    }

    private void createLocationParameters() {
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(TimeUnit.SECONDS.toMillis(10));
        locationRequest.setFastestInterval(TimeUnit.SECONDS.toMillis(1));
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        settingsClient = LocationServices.getSettingsClient(this);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();
    }

    /**
     * This method is used when this RecordActivity is paused to possibly
     * disable location updates.
     */
    @Override
    public void onPause() {
        super.onPause();

        if (locationProviderClient != null && !isRecording)
            locationProviderClient.removeLocationUpdates(locationCallback);
    }

    /**
     * This method is used when this RecordActivity is resumed to possibly
     * enable location updates.
     */
    @Override
    public void onResume() {
        super.onResume();

        if (!isRecording) {
            if (checkForLocPermission()) {
                if (locationProviderClient == null) {
                    createLocationParameters();
                }
                requestLocationUpdates();
            } else {
                requestLocPermissions();
            }
        }
    }

    private List<LatLng> convertListToLatLng() {

        List<LatLng> latLngs = new LinkedList<>();

        for (Location loc : locationList)
            latLngs.add(new LatLng(loc.getLatitude(), loc.getLongitude()));

        return latLngs;
    }

    /**
     * This method causes the timer to be paused/resumed, but recording is still enabled.
     * @param v not used
     */
    public void togglePauseStatus(View v) {
        if (isRecording) {

            isPaused = !isPaused;
            pauseButton.setImageResource(
                    isPaused ? R.drawable.ic_record_play : R.drawable.ic_record_pause
            );

            if (isPaused) {
                timeWhenStopped = timer.getBase() - SystemClock.elapsedRealtime();
                timer.stop();
            }
            else {
                timer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                timeWhenStopped = 0L;
                timer.start();
            }

        }
    }

    /**
     * This method toggles between recording statuses, and triggers the save
     * dialog on completion.
     * @param v not used
     */
    public void toggleRecordStatus(View v) {
        isRecording = !this.isRecording;
        recordButton.setImageResource(
                isRecording ? R.drawable.ic_record_stop : R.drawable.ic_record_rec
        );

        if (!isRecording) {
            timer.stop();
            long time = SystemClock.elapsedRealtime() - timer.getBase();
            isPaused = false;
            pauseButton.setVisibility(View.INVISIBLE);

            RecordSaveFragment fragment = new RecordSaveFragment();
            Bundle bundle = new Bundle();
            bundle.putString(DATABASE_REFERENCE, getString(R.string.activity_record_firebase, user.getUsername()));
            bundle.putLong(ELAPSED_TIME, time);
            bundle.putParcelableArrayList(LOCATION_LIST, locationList);
            fragment.setArguments(bundle);
            fragment.show(getSupportFragmentManager(), "SAVE_TO_FIREBASE_FRAGMENT");
        }
        else {
            locationList = new ArrayList<>();
            currentRoute = null;
            timer.setBase(SystemClock.elapsedRealtime());
            timer.start();
            pauseButton.setVisibility(View.VISIBLE);
        }
    }

    private static void saveToFirebase(String dbRef, long time, List<Location> locationList) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef
                = db.getReference(dbRef);

        RecordedWorkout workout = new RecordedWorkout(locationList, time);
        String key = myRef.push().getKey();
        myRef.child(key).setValue(workout);
    }

    private boolean checkForLocPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        else
            return true;
    }

    private void requestLocationUpdates() {
        try {
            locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is a callback when location permissions are requested.
     * @param requestCode the code of the permission requested
     * @param permissions the permissions requested
     * @param grantResults the result of the request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        createLocationParameters();
                        requestLocationUpdates();
                        moveToInitialLocation();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * This method is a callback when the Map object is instantiated.
     * @param googleMap the map that has been created
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (checkForLocPermission()) {
            try {
                moveToInitialLocation();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    private void moveToInitialLocation() throws SecurityException {
        Task<Location> locationTask = locationProviderClient.getLastLocation();
        mMap.setMyLocationEnabled(true);
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mMap.moveCamera(CameraUpdateFactory.zoomTo(16f));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(
                        new LatLng(location.getLatitude(), location.getLongitude())
                ));
            }
        });
    }

    /**
     * This method gets the isRecording field
     * @return the isRecording field
     */
    protected boolean getIsRecording() {
        return isRecording;
    }

    /**
     * This method gets the isPaused field
     * @return the isPaused field
     */
    protected boolean getIsPaused() {
        return isPaused;
    }

}
