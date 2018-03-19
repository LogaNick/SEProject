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

import java.util.concurrent.TimeUnit;

public class CreateTeamActivity extends AppCompatActivity {
    private User user;
    /**
     * Initializes the activity when created
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        ((Button)findViewById(R.id.submitTeamButton)).setEnabled(false);

        // get the active user
        user = (User) getIntent().getExtras().getSerializable("user");

        findViewById(R.id.submitTeamButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the Team data from the fields
                String name = ((TextView) findViewById(R.id.newTeamName)).getText().toString();
                String motto = ((TextView) findViewById(R.id.newTeamMotto)).getText().toString();
                String owner = user.getUsername();

                // Validate fields and submit data
                if (Team.validateAll(name, motto, owner)) {
                    // Add Team to user
                    user.addUserTeam(new Team(name, motto, owner));
                    AccountManager.updateUser(user);

                    // Switch back to exercise activity
                    Intent intent = new Intent(CreateTeamActivity.this, TeamActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
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
                String name = ((TextView)findViewById(R.id.newTeamName)).getText().toString();
                String motto = ((TextView)findViewById(R.id.newTeamMotto)).getText().toString();

                // Enable submit button if fields are valid
                ((Button)findViewById(R.id.submitTeamButton)).setEnabled(Team.validateAll(name, motto));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        // Add listeners to team data fields
        ((EditText)findViewById(R.id.newTeamName)).addTextChangedListener(textChangeListener);
        ((EditText)findViewById(R.id.newTeamMotto)).addTextChangedListener(textChangeListener);
    }
}
