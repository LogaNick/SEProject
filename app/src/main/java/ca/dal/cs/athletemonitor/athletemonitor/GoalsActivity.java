package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

public class GoalsActivity extends AppCompatActivity {

    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        // Click listener for create new goal button
        findViewById(R.id.createGoalButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectWorkoutActivityIntent = new Intent(GoalsActivity.this, CreateGoalActivity.class);
                selectWorkoutActivityIntent.putExtra("user", user);
                startActivityForResult(selectWorkoutActivityIntent, 1);
            }
        });

        user = (User) getIntent().getExtras().getSerializable("user");

        populateGoals();
    }

    private void populateGoals() {
        LinearLayout layout = findViewById(R.id.goalLinearLayout);
        layout.removeAllViews();

        boolean alternateColor = false;

        //get the user's list of goals
        List<Goal> goals = user.getUserGoals();

        //iterate and add goals to screen
        for (Goal goal : goals) {
            // Build a new for this goal
            TextView goalText = new TextView(GoalsActivity.this);
            goalText.setText(goal.getName());
            goalText.setTextSize(28);
            goalText.setPadding(0, 30, 0, 30);

            if (alternateColor) goalText.setBackgroundColor(Color.LTGRAY);

            goalText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 4, 0, 0);

            goalText.setLayoutParams(params);

            // Add a click listener to show more information
            goalText.setOnClickListener(new DialogOnClickListener(goal));
            // Add the text view to the screen
            layout.addView(goalText);

            alternateColor = !alternateColor;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            user = (User) data.getSerializableExtra("user");
            populateGoals();
        }
    }

    // Custom click listener implementation, so that we can access the goal data.
    class DialogOnClickListener implements View.OnClickListener{
        Goal goal;

        public DialogOnClickListener(Goal goal){
            this.goal = goal;
        }

        @Override
        public void onClick(View v){
            AlertDialog.Builder builder = new AlertDialog.Builder(GoalsActivity.this);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            })
                    .setTitle(goal.getName())
                    .setMessage("\n" + goal.getDescription() + "\n\n")
                    .show();
        }
    }
}
