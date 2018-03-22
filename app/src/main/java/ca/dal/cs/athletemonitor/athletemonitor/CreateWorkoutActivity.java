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
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);

        // get the active user
        user = (User) getIntent().getExtras().getSerializable("user");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 4, 0, 0);

        ListIterator<Exercise> it = user.getUserExercises().listIterator();
        while(it.hasNext()) {
            int i = it.nextIndex();
            Exercise e = it.next();

            CheckBox checkBox = new CheckBox(CreateWorkoutActivity.this);
            checkBox.setText(e.getName() + " (" + e.getTime() + " " + e.getTimeUnit().toString().toLowerCase() + ")");
            checkBox.setLayoutParams(params);
            checkBox.setTag("exercise" + i);

            ((LinearLayout) findViewById(R.id.createWorkoutLinearLayout)).addView(checkBox);
        }

        findViewById(R.id.submitNewWorkoutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    throw new IllegalStateException("Not logged in");
                }

                LinearLayout createWorkoutLayout = findViewById(R.id.createWorkoutLinearLayout);

                Workout newWorkout = new Workout(((TextView)findViewById(R.id.newWorkoutName)).getText().toString());

                ListIterator<Exercise> it = user.getUserExercises().listIterator();
                while(it.hasNext()){
                    int i = it.nextIndex();
                    Exercise cur = it.next();
                    if(((CheckBox)createWorkoutLayout.findViewWithTag("exercise" + i)).isChecked()){
                        newWorkout.addWorkoutExercise(new WorkoutExercise(cur));
                    }
                }

                if (newWorkout.getExercises().size() > 0) {
                    user.addUserWorkout(newWorkout);
                    AccountManager.updateUser(user);
                    finish();
                }
            }
        });
    }
}