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
        Bundle extras = getIntent().getExtras();
        if (extras == null || !extras.containsKey("username")) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        } else {
            ((Button)this.findViewById(R.id.btnSignin)).setText("Signout " + extras.getString("username"));
        }

        // Add the exercise button click listener
        findViewById(R.id.goToExerciseActivityButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exerciseActivityIntent = new Intent(MainActivity.this, ExerciseActivity.class);
                startActivity(exerciseActivityIntent);
            }
        });
    }

    public void startLogin(View view) {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }
}
