package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import static android.app.PendingIntent.getActivity;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class ExerciseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        // Click listener for create new exercise button
        findViewById(R.id.createExerciseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createExerciseActivityIntent = new Intent(ExerciseActivity.this, CreateExerciseActivity.class);
                createExerciseActivityIntent.putExtras(getIntent().getExtras());
                startActivity(createExerciseActivityIntent);
            }
        });

        AccountManager.getUser(getIntent().getExtras().getString("username"), new AccountManager.UserObjectListener() {
            @Override
            public void onUserPopulated(User user) {
                // Get the layout to add exercises to
                LinearLayout layout = findViewById(R.id.exerciseLinearLayout);

                boolean alternateColor = false;

                // Get the user's list of exercises
                List<Exercise> exercises = user.getUserExercises();
                // Iterate and add exercises to screen
                for (Exercise exercise : exercises) {
                    // Build a new TextView for this exercise
                    TextView exerciseText = new TextView(ExerciseActivity.this);
                    exerciseText.setText(exercise.getName());
                    exerciseText.setTextSize(28);
                    exerciseText.setPadding(0, 30, 0, 30);

                    if (alternateColor) exerciseText.setBackgroundColor(Color.LTGRAY);

                    exerciseText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 4, 0, 0);

                    exerciseText.setLayoutParams(params);

                    // Add a click listener to show more information
                    exerciseText.setOnClickListener(new DialogOnClickListener(exercise));
                    // Add the text view to the screen
                    layout.addView(exerciseText);

                    alternateColor = !alternateColor;
                }
            }
        });
    }

    // Custom click listener implementation, so that we can access the exercise data.
    class DialogOnClickListener implements View.OnClickListener{
        Exercise exercise;

        public DialogOnClickListener(Exercise exercise){
            this.exercise = exercise;
        }

        @Override
        public void onClick(View v){
            AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseActivity.this);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    })
                    .setTitle(exercise.getName())
                    .setMessage("\n" + exercise.getDescription() + "\n\n" + exercise.getTime() + " " + exercise.getTimeUnit().toString().toLowerCase())
                    .show();
        }
    }
}
