package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class CreateGoalActivity extends AppCompatActivity{

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);

        ((Button)findViewById(R.id.newGoalSubmitButton)).setEnabled(true);

        user = (User) getIntent().getSerializableExtra("user");

        // Create on click listener for submit button
        findViewById(R.id.newGoalSubmitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the goal data from the fields
                String name = ((TextView) findViewById(R.id.newGoalName)).getText().toString();
                String description = ((TextView) findViewById(R.id.newGoalDescription)).getText().toString();

                findViewById(R.id.newGoalSubmitButton).setEnabled(false);
                // Add goal to user
                user.addUserGoal(new Goal(name, description));
                AccountManager.updateUser(user);
                // Switch back to goals activity
                Intent goalsActivityIntent = new Intent();
                goalsActivityIntent.putExtra("user", user);
                setResult(RESULT_OK, goalsActivityIntent);
                finish();
            }
        });

    }
}
