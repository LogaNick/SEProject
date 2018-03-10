package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

public class CreateWorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);

        AccountManager.getUser(getIntent().getExtras().getString("username"), new AccountManager.UserObjectListener() {
            @Override
            public void onUserPopulated(User user) {
                List<Exercise> exercises = user.getUserExercises();

                ListIterator<Exercise> it = exercises.listIterator();
                while(it.hasNext()){
                    int i = it.nextIndex();
                    Exercise e = it.next();

                    CheckBox checkBox = new CheckBox(CreateWorkoutActivity.this);
                    checkBox.setText(e.getName() + " (" + e.getTime() + " " + e.getTimeUnit().toString().toLowerCase() + ")");

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 4, 0, 0);

                    checkBox.setLayoutParams(params);
                    checkBox.setTag("exercise" + i);

                    // Add the text view to the screen
                    ((LinearLayout)findViewById(R.id.createWorkoutLinearLayout)).addView(checkBox);
                }
            }
        });

        findViewById(R.id.submitNewWorkoutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the user object, add the new workout to it, and update it
                AccountManager.getUser(getIntent().getExtras().getString("username"), new AccountManager.UserObjectListener() {
                    @Override
                    public void onUserPopulated(User user) {
                        if (user == null) {
                            throw new IllegalStateException("Not logged in");
                        }

                        LinearLayout layout = findViewById(R.id.createWorkoutLinearLayout);

                        // Get the workout name
                        String name = ((TextView)findViewById(R.id.newWorkoutName)).getText().toString();

                        Workout newWorkout = new Workout(name);

                        ListIterator<Exercise> it = user.getUserExercises().listIterator();
                        while(it.hasNext()){
                            int i = it.nextIndex();
                            Exercise cur = it.next();
                            if(((CheckBox)layout.findViewWithTag("exercise" + i)).isChecked()){
                                newWorkout.addWorkoutExercise(new WorkoutExercise(cur));
                            }
                        }

                        // Validate data and submit
                        if (newWorkout.getExercises().size() > 0) {
                            // Add workout to user
                            user.addUserWorkout(newWorkout);
                            AccountManager.updateUser(user);
                            // Switch back to workout activity
                            Intent workoutActivityIntent = new Intent(CreateWorkoutActivity.this, WorkoutActivity.class);
                            workoutActivityIntent.putExtras(getIntent().getExtras());
                            startActivity(workoutActivityIntent);
                        }
                    }
                });
            }
        });
    }
}