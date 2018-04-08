package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * This class describes a screen where the user can view their
 * personal information, and begin an activity to edit it (if they so choose).
 */
public class UserInformationActivity extends AppCompatActivity {

    private User user;
    private UserInformation info = new UserInformation();

    protected static final String USER_INFORMATION = "USER_INFORMATION";
    protected static final String USER = "user";

    private static final String TAG = "UserInformationActivity";
    private static final int EDIT_INFO_REQUEST = 0;

    /**
     * This method connects to Firebase, retrieving the user information
     * stored for a given userId.
     *
     * @param userId the ID for the user to display
     * @return the user info corresponding with the ID if successful, null otherwise
     */
    private void retrieveInfo(final String userId) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef
                = db.getReference(getString(R.string.activity_user_information_firebase, userId));

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInformation information = dataSnapshot.getValue(UserInformation.class);
                Log.d(TAG, "Retrieved " + userId + " from Firebase.");
                if (information != null) {
                    info = information;
                    changeDisplayedInfo(info);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to retrieve " + userId + " from Firebase.");
            }
        });

    }

    /**
     * This method changes the information displayed on the personal info screen,
     * based on the passed object.
     *
     * @param info a representation of the the info to be displayed
     */
    private void changeDisplayedInfo(UserInformation info) {
        TextView nameView = findViewById(R.id.nameTextView);
        TextView statementTextView = findViewById(R.id.statementTextView);
        TextView ageTextView = findViewById(R.id.ageDisplayView);
        TextView heightDisplayView = findViewById(R.id.heightDisplayView);
        TextView weightDisplayView = findViewById(R.id.weightDisplayView);
        TextView athleteTypeDisplayView = findViewById(R.id.athleteTypeDisplayView);
        TextView imageIdDisplayView = findViewById(R.id.imageIdDisplayView);

        nameView.setText(getString(
                R.string.activity_user_information_format_name,
                info.firstName,
                info.lastName)
        );
        statementTextView.setText(getString(
                R.string.activity_user_information_format_statement,
                info.personalStatement)
        );
        ageTextView.setText(getString(R.string.activity_user_information_format_age, info.age));
        heightDisplayView.setText(
                getString(R.string.activity_user_information_format_height, info.height)
        );
        weightDisplayView.setText(
                getString(R.string.activity_user_information_format_weight, info.weight)
        );
        athleteTypeDisplayView.setText(
                getString(R.string.activity_user_information_format_athlete_type, info.athleteType)
        );
        String[] imageArr = getResources().getStringArray(R.array.ImageIds);
        imageIdDisplayView.setText(
                getString(R.string.activity_user_information_format_user_image, imageArr[info.imageId])
        );
    }

    /**
     * This method starts the edit information activity.
     */
    private void startEditInformation(View view) {
        Intent intent = new Intent(this, UserInformationEditActivity.class);
        intent.putExtra(USER, user);
        intent.putExtra(USER_INFORMATION, info);
        startActivityForResult(intent, EDIT_INFO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_INFO_REQUEST) {
            if (resultCode == RESULT_OK) {
                info = data.getParcelableExtra(USER_INFORMATION);
                changeDisplayedInfo(info);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        retrieveInfo(user.getUsername());

        FloatingActionButton fab = findViewById(R.id.editInfo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEditInformation(view);
            }
        });
    }

}
