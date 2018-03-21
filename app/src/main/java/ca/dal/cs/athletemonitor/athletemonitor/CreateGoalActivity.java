package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class CreateGoalActivity extends AppCompatActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);

        ((Button)findViewById(R.id.newGoalSubmitButton)).setEnabled(true);

        // Create on click listener for submit button
        findViewById(R.id.newGoalSubmitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the logged in user instance
                AccountManager.getUser(getIntent().getExtras().getString("username"), new AccountManager.UserObjectListener() {
                    @Override
                    public void onUserPopulated(User user) {
                        if (user == null) {
                            throw new IllegalStateException("Not logged in");
                        }

                        // Get the goal data from the fields
                        String name = ((TextView) findViewById(R.id.newGoalName)).getText().toString();
                        String description = ((TextView) findViewById(R.id.newGoalDescription)).getText().toString();
                        int time = 0;

                        ((Button)findViewById(R.id.newGoalSubmitButton)).setEnabled(false);
                        // Add goal to user
                        user.addUserGoal(new Goal(name, description));
                        AccountManager.updateUser(user);
                        // Switch back to goals activity
                        Intent goalsActivityIntent = new Intent(CreateGoalActivity.this, GoalsActivity.class);
                        goalsActivityIntent.putExtras(getIntent().getExtras());
                        startActivity(goalsActivityIntent);
                    }
                });
            }
        });

    }
}
