package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WorkoutActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    public Spinner spinner;
    private List<Workout> workouts;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);




        AccountManager.getUser(getIntent().getExtras().getString("username"), new AccountManager.UserObjectListener() {
            @Override
            public void onUserPopulated(User user) {
                // Get the layout to add exercises to
                layout = findViewById(R.id.workoutLinearLayout);

                //boolean alternateColor = false;

                // Get the user's list of exercises
                workouts = user.getUserWorkouts();


                spinner = (Spinner) findViewById (R.id.spinner);
                ArrayAdapter<Workout> adapter = new ArrayAdapter<Workout>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, workouts);
                adapter.setDropDownViewResource
                        (android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                // Iterate and add exercises to screen
                /*
                for (Workout workout : workouts) {
                    // Build a new TextView for this exercise
                    TextView workoutText = new TextView(WorkoutActivity.this);
                    workoutText.setText(workout.getName());
                    workoutText.setTextSize(28);
                    workoutText.setPadding(0, 30, 0, 30);

                    if (alternateColor) workoutText.setBackgroundColor(Color.LTGRAY);

                    workoutText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 4, 0, 0);

                    workoutText.setLayoutParams(params);

                    // Add a click listener to show more information
                    //workoutText.setOnClickListener(new WorkoutActivity.DialogOnClickListener(workout));
                    // Add the text view to the screen
                    layout.addView(workoutText);

                    alternateColor = !alternateColor;
                }
                */
            }
        });
    }
    //}

    public void onItemSelected (AdapterView<?> parent, View view, int pos, long id)
    {
        ArrayList<WorkoutExercise> exerciseList = workouts.get(pos).getExercises();

        for (WorkoutExercise workoutExercise : exerciseList) {
            // Build a new TextView for this exercise
            TextView exerciseText = new TextView(WorkoutActivity.this);
            exerciseText.setText(workoutExercise.getName());
            exerciseText.setTextSize(28);
            exerciseText.setPadding(0, 10, 0, 10);

            //if (alternateColor) workoutText.setBackgroundColor(Color.LTGRAY);

            exerciseText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 4, 0, 0);

            exerciseText.setLayoutParams(params);

            // Add a click listener to show more information
            //workoutText.setOnClickListener(new WorkoutActivity.DialogOnClickListener(workout));
            // Add the text view to the screen
            layout.addView(exerciseText);

            RelativeLayout exerciseDataLayout = new RelativeLayout(WorkoutActivity.this);
            EditText inputData = new EditText(WorkoutActivity.this);
            TextView outOfField = new TextView(WorkoutActivity.this);

            inputData.setTextSize(28);
            inputData.setPadding(0, 10, 0, 10);

            outOfField.setTextSize(28);
            outOfField.setPadding(0, 10, 0, 10);

            outOfField.setText("/ "+workoutExercise.getTime()+" "+workoutExercise.getTimeUnit());

            exerciseDataLayout.addView(inputData);
            exerciseDataLayout.addView(outOfField);


        }
    }

    public void onNothingSelected (AdapterView<?> parent)
    {

    }

}



