package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

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
                startActivity(createExerciseActivityIntent);
            }
        });

        // Get the layout to add exercises to
        LinearLayout layout = findViewById(R.id.exerciseLinearLayout);

        boolean alternateColor = false;

        // Get the user's list of exercises
        List<Exercise> exercises = UserManager.getUserInstance().getUserExercises();
        // Iterate and add exercises to screen
        for(Exercise exercise : exercises){
            // Build a new TextView for this exercise
            TextView exerciseText = new TextView(this);
            exerciseText.setText(exercise.getName());
            exerciseText.setTextSize(28);
            exerciseText.setPadding(0,30,0,30);
            if(alternateColor) exerciseText.setBackgroundColor(Color.LTGRAY);
            exerciseText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 4,0,0);
            exerciseText.setLayoutParams(params);

            // Add the text view to the screen
            layout.addView(exerciseText);

            alternateColor = !alternateColor;
        }
    }
}
