package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.support.v7.widget.Toolbar;



public class MainActivity extends AppCompatActivity {
    private User activeUser = null;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        //Initialize toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);





        // retrieve the extras passed by the intent, if there is a username then the user is logged
        // in.  If username doesn't exist, go to sign in.
        final Bundle extras = getIntent().getExtras();
        if (extras == null || !extras.containsKey("user")) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        } else {
            activeUser = (User) extras.getSerializable("user");
            //((Button)this.findViewById(R.id.btnSignOut)).setText("Signout " + activeUser.getUsername());
        }



        /*
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
                   userInfoIntent.putExtra("USER", activeUser);
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

        // Add the goals button click listener
        findViewById(R.id.goToGoalsActivityButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goalsActivityIntent = new Intent(MainActivity.this, GoalsActivity.class);
                goalsActivityIntent.putExtra("user", activeUser);
                startActivity(goalsActivityIntent);
            }
        });




        findViewById(R.id.goToRecordButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recordIntent = new Intent(MainActivity.this, RecordActivity.class);
                recordIntent.putExtra("user", activeUser);
                startActivity(recordIntent);
            }
        });

        ((Switch)findViewById(R.id.onlineToggleSwitch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AccountManager.setOnline(isChecked);
            }
        });


        */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                Log.d("Reached Here 2", "derpdederp");
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        Log.d("Reached Here 3", "derpdederp");
        return  super.onOptionsItemSelected(item);
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
