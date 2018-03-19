package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CreateExerciseActivity extends AppCompatActivity {
    private User user;

    private static final Map<String, TimeUnit> timeUnitNames = new HashMap<String, TimeUnit>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercise);

        // get the active user
        user = (User) getIntent().getExtras().getSerializable("user");

        // Initialize timeUnitNames map
        timeUnitNames.put("Seconds", TimeUnit.SECONDS);
        timeUnitNames.put("Minutes", TimeUnit.MINUTES);
        timeUnitNames.put("Hours", TimeUnit.HOURS);

        ((Button)findViewById(R.id.newExerciseSubmitButton)).setEnabled(false);

        // Create on click listener for submit button
        findViewById(R.id.newExerciseSubmitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    throw new IllegalStateException("Not logged in");
                }

                // Get the exercise data from the fields
                String name = ((TextView) findViewById(R.id.newExerciseName)).getText().toString();
                String description = ((TextView) findViewById(R.id.newExerciseDescription)).getText().toString();
                String timeString = ((TextView) findViewById(R.id.newExerciseTime)).getText().toString();
                int time = 0;

                if (!timeString.equals("")) {
                    time = Integer.parseInt(timeString);
                }

                TimeUnit unit = timeUnitNames.get(((Spinner) findViewById(R.id.newExerciseTimeUnits)).getSelectedItem().toString());

                // Validate fields and submit data
                if (Exercise.validateAll(name, description, time)) {
                    // Add exercise to user
                    user.addUserExercise(new Exercise(name, description, time, unit));
                    AccountManager.updateUser(user);
                    // Switch back to exercise activity
                    Intent exerciseActivityIntent = new Intent(CreateExerciseActivity.this, ExerciseActivity.class);
                    exerciseActivityIntent.putExtra("user", user);
                    startActivity(exerciseActivityIntent);
                }
            }
        });

        // Text change listener for the text fields, to validate as the user types
        TextWatcher textChangeListener = new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String name = ((TextView)findViewById(R.id.newExerciseName)).getText().toString();
                String description = ((TextView)findViewById(R.id.newExerciseDescription)).getText().toString();
                String timeString = ((TextView)findViewById(R.id.newExerciseTime)).getText().toString();
                int time = 0;

                if(!timeString.equals("")){
                    time = Integer.parseInt(timeString);
                }

                ((Button)findViewById(R.id.newExerciseSubmitButton)).setEnabled(Exercise.validateAll(name, description, time));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        ((EditText)findViewById(R.id.newExerciseName)).addTextChangedListener(textChangeListener);
        ((EditText)findViewById(R.id.newExerciseDescription)).addTextChangedListener(textChangeListener);
        ((EditText)findViewById(R.id.newExerciseTime)).addTextChangedListener(textChangeListener);
    }
}
