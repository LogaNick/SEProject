package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WorkoutActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    // TODO Refactor these private variables to make code safer, i.e. either make variables
    // local to methods or check for null where necessary

    // Needed private variables
    private Spinner spinner;
    private Button submitButton;
    private Workout currentWorkout;
    private WorkoutExercise currentExercise;
    private List<Workout> workouts;
    private LinearLayout layout;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        AccountManager.getUser(getIntent().getExtras().getString("username"), new AccountManager.UserObjectListener() {
            @Override
            public void onUserPopulated(User user) {
                // Get the layout to add exercises to
                layout = findViewById(R.id.workoutLinearLayout);

                // Get the user's list of exercises
                workouts = user.getUserWorkouts();

                // Spinner for workout selection, and its adapter
                spinner = (Spinner) findViewById (R.id.spinner);
                spinner.setOnItemSelectedListener(WorkoutActivity.this);
                ArrayAdapter<Workout> adapter = new ArrayAdapter<Workout>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, workouts);
                adapter.setDropDownViewResource
                        (android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                submitButton = findViewById(R.id.submitDataButton);
                submitButton.setClickable(false);
            }
        });

        findViewById(R.id.createWorkoutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createWorkoutActivityIntent = new Intent(WorkoutActivity.this, CreateWorkoutActivity.class);
                createWorkoutActivityIntent.putExtras(getIntent().getExtras());
                startActivity(createWorkoutActivityIntent);
            }
        });
    }

    // When a workout has been selected from the spinner
    public void onItemSelected (AdapterView<?> parent, View view, int pos, long id) {
        // Get current workout, its position, whether it is completed and the exercises
        currentWorkout = workouts.get(pos);
        position = pos;
        boolean isCompleted = currentWorkout.isCompleted();
        final ArrayList<WorkoutExercise> exerciseList = currentWorkout.getExercises();

        // For each exercise in the workout, add the required elements to the layout
        for (int i = 0; i< exerciseList.size(); i++) {
            currentExercise = exerciseList.get(i);
            // Build a new TextView for this exercise
            TextView exerciseText = new TextView(WorkoutActivity.this);
            exerciseText.setText(currentExercise.getName());
            exerciseText.setTextSize(28);
            exerciseText.setPadding(0, 10, 0, 10);

            exerciseText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 4, 0, 0);

            exerciseText.setLayoutParams(params);

            // Add the text view to the screen
            layout.addView(exerciseText);

            EditText inputData = new EditText(WorkoutActivity.this);
            TextView outOfField = new TextView(WorkoutActivity.this);

            inputData.setTextSize(28);
            inputData.setPadding(0, 10, 0, 10);
            inputData.setTag("workoutExercise" + i);

            outOfField.setTextSize(28);
            outOfField.setPadding(0, 10, 0, 10);

            outOfField.setText("/ " + currentExercise.getTime() + " " + currentExercise.getTimeUnit());

            if (isCompleted) {
                inputData.setText(currentExercise.getData() +"");
                inputData.setBackgroundColor(Color.LTGRAY);
                inputData.setEnabled(false);
            }

            inputData.setLayoutParams(params);
            outOfField.setLayoutParams(params);

            layout.addView(inputData);
            layout.addView(outOfField);
        }

        // Only allow submission if the workout was not already complete
        if (isCompleted) {
            submitButton.setClickable(false);
        }
        // Otherwise check to see if exercises are completed, and update the UI respectively
        else
        {
            // Create on click listener for submit button
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the logged in user instance
                    AccountManager.getUser(getIntent().getExtras().getString("username"), new AccountManager.UserObjectListener() {
                        @Override
                        public void onUserPopulated(User user) {
                            if (user == null) {
                                throw new IllegalStateException("Not logged in");
                            }

                            for (int i=0; i<exerciseList.size(); i++) {
                                // Get the exercise data from the fields
                                currentExercise = exerciseList.get(i);
                                TextView exerciseView = (TextView) layout.findViewWithTag("workoutExercise" + i);
                                String data = exerciseView.getText().toString();
                                int dataValue = 0;
                                try {
                                    dataValue = Integer.parseInt(data);
                                    if (dataValue >= currentExercise.getTime())
                                    {
                                        exerciseView.setBackgroundColor(Color.GREEN);
                                    }
                                    else
                                    {
                                        exerciseView.setBackgroundColor(Color.RED);
                                    }
                                    exerciseView.setEnabled(false);
                                    currentExercise.setData(dataValue);
                                }
                                catch(Exception e) {

                                }
                            }
                            currentWorkout.setCompleted(true);
                            AccountManager.updateUser(user);
                        }
                    });
                }
            });
        }
    }

    public void onNothingSelected (AdapterView<?> parent)
    {

    }
}