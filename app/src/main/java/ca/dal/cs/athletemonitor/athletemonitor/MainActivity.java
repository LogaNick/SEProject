package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //retrieve the extras passed by the intent, if there is a username then the user is logged
        // in.  If username doesn't exist, go to sign in.
        //TODO: implement User as Parcelable
        //TODO: initialize a User object with user data to pass around to other activities
        final Bundle extras = getIntent().getExtras();
        if (extras == null || !extras.containsKey("username")) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        } else {
            ((Button)this.findViewById(R.id.btnSignOut)).setText("Signout " + extras.getString("username"));
        }

        // Add the exercise button click listener
        findViewById(R.id.goToExerciseActivityButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exerciseActivityIntent = new Intent(MainActivity.this, ExerciseActivity.class);
                exerciseActivityIntent.putExtra("username", extras.getString("username"));
                startActivity(exerciseActivityIntent);
            }
        });

        // Add the team button click listener
        findViewById(R.id.createNewTeamButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createTeamActivityIntent = new Intent(MainActivity.this, CreateTeamActivity.class);
                createTeamActivityIntent.putExtra("username", extras.getString("username"));
                startActivity(createTeamActivityIntent);
            }
        });
    }

    public void logOutButtonHandler(View view){
        //Take user to the log in page and update Firebase's online_users node

        //Start by taking the user offline in Firebase
        final Bundle extras = getIntent().getExtras();
        AccountManager.setUserLoginState(extras.getString("username"), false);

        //Start the sign in activity
        Intent signInActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(signInActivityIntent);
    }
}
