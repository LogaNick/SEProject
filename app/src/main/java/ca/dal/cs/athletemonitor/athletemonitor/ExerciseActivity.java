package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ExerciseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        findViewById(R.id.createExerciseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createExerciseActivityIntent = new Intent(ExerciseActivity.this, CreateExerciseActivity.class);
                startActivity(createExerciseActivityIntent);
            }
        });
    }
}
