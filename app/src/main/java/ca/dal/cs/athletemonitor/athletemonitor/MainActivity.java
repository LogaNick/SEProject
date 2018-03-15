package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import static ca.dal.cs.athletemonitor.athletemonitor.UserInformationActivity.USER_ID;

public class MainActivity extends AppCompatActivity {
    private User activeUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // retrieve the extras passed by the intent, if there is a username then the user is logged
        // in.  If username doesn't exist, go to sign in.
        final Bundle extras = getIntent().getExtras();
        if (extras == null || !extras.containsKey("user")) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        } else {
            activeUser = (User) extras.getSerializable("user");
            ((Button)this.findViewById(R.id.btnSignOut)).setText("Signout " + activeUser.getUsername());
        }

        // Add the exercise button click listener
        findViewById(R.id.goToExerciseActivityButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exerciseActivityIntent = new Intent(MainActivity.this, ExerciseActivity.class);
                exerciseActivityIntent.putExtra("user", activeUser);
                startActivity(exerciseActivityIntent);
            }
        });

        findViewById(R.id.goToUserInfo).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent userInfoIntent = new Intent(MainActivity.this, UserInformationActivity.class);
                   userInfoIntent.putExtra("user", activeUser);
                   startActivity(userInfoIntent);
               }
		});

        // Add the team button click listener
        findViewById(R.id.teamButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent teamActivityIntent = new Intent(MainActivity.this, TeamActivity.class);
                teamActivityIntent.putExtra("user", activeUser);
                startActivity(teamActivityIntent);
            }
        });

        // Add the workout button click listener
        findViewById(R.id.goToWorkoutActivityButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent workoutActivityIntent = new Intent(MainActivity.this, WorkoutActivity.class);
                workoutActivityIntent.putExtra("user", activeUser);
                startActivityForResult(workoutActivityIntent, 1);
            }
        });

        ((Switch)findViewById(R.id.onlineToggleSwitch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AccountManager.setOnline(isChecked);
            }
        });
    }

    /**
     * Take user to the log in page and update Firebase's online_users node
     * @param view
     */
    public void logOutButtonHandler(View view){
        if(!AccountManager.isOnline()){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setMessage(R.string.logout_while_offline_warning)
                    .setTitle("Warning")
                    .setPositiveButton(R.string.logout_while_offline_warning_save, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AccountManager.setOnline(true);
                            logout();
                        }
                    })
                    .setNegativeButton(R.string.logout_while_offline_warning_quit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            logout();
                        }
                    })
                    .create()
                    .show();
        } else {
            logout();
        }
    }

    private void logout() {
        //Start by taking the user offline in Firebase
        AccountManager.setUserLoginState(activeUser.getUsername(), false);

        // Return to login activity
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1){
            /* Returning from workout activity */
        }
    }
}
