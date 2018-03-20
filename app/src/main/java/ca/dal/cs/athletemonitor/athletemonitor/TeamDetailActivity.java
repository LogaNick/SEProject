package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.opengl.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.dal.cs.athletemonitor.athletemonitor.listeners.BooleanResultListener;

public class TeamDetailActivity extends AppCompatActivity {
    private Team team;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);

        user = (User) getIntent().getExtras().getSerializable("user");
        team = (Team) getIntent().getExtras().getSerializable("team");

        configureViews();
    }

    /**
     * Configures the transfer ownership button
     */
    private void setupTransferOwnershipButton() {
        final Button transferOwnershipButton = findViewById(R.id.transferOwnerButton);

        transferOwnershipButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Set the components on this activity to editable if not currently editing, otherwise
             * update the database with the new information and return to the previous activity
             *
             * @param v View that received the click event
             */
            @Override
            public void onClick(View v) {
                AccountManager.transferOwnership(team, ((EditText) findViewById(R.id.teamOwner)).getText().toString(), new BooleanResultListener() {
                    @Override
                    public void onResult(boolean result) {
                        if (result) {
                            ((Button) findViewById(R.id.editTeamButton)).setVisibility(View.INVISIBLE);
                            ((Button) findViewById(R.id.transferOwnerButton)).setVisibility(View.INVISIBLE);
                            ((TextView) findViewById(R.id.lblMessage)).setText(R.string.ownershipTransferred);
                        } else {
                            ((TextView) findViewById(R.id.lblMessage)).setText(R.string.ownershipTransferredFailed);
                        }
                    }
                });
            }
        });
    }

    /**
     * Configures the edit team button
     */
    private void setupEditTeamButton() {
        final Button editTeamButton = findViewById(R.id.editTeamButton);

        editTeamButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Set the components on this activity to editable if not currently editing, otherwise
             * update the database with the new information and return to the previous activity
             *
             * @param v View that received the click event
             */
            @Override
            public void onClick(View v) {
                if (editTeamButton.getText().toString().equals(getString(R.string.editTeam))) {
                    findViewById(R.id.teamName).setEnabled(true);
                    findViewById(R.id.teamMotto).setEnabled(true);
                    findViewById(R.id.teamOwner).setEnabled(true);
                    findViewById(R.id.transferOwnerButton).setVisibility(View.VISIBLE);
                    findViewById(R.id.lblMessage).setVisibility(View.VISIBLE);
                    editTeamButton.setText(R.string.submitChanges);
                } else {
                    findViewById(R.id.teamName).setEnabled(false);
                    findViewById(R.id.teamMotto).setEnabled(false);
                    findViewById(R.id.teamOwner).setEnabled(false);
                    findViewById(R.id.transferOwnerButton).setVisibility(View.INVISIBLE);
                    findViewById(R.id.lblMessage).setVisibility(View.INVISIBLE);
                    editTeamButton.setEnabled(true);

                    String name = ((TextView) findViewById(R.id.teamName)).getText().toString();
                    String motto = ((TextView) findViewById(R.id.teamMotto)).getText().toString();

                    //find the team in the users team list
                    Team teamToUpdate = user.getUserTeams().get(user.getUserTeams().indexOf(team));
                    teamToUpdate.setName(name);
                    teamToUpdate.setMotto(motto);
                    AccountManager.updateUser(user);

                    Intent result = new Intent();
                    result.putExtra("user", user);
                    setResult(1, result);

                    finish();
                }
            }
        });
    }

    /**
     * Populates the views on this activity with relevant information
     */
    private void configureViews() {
        ((EditText) findViewById(R.id.teamName)).setText(team.getName());
        ((EditText) findViewById(R.id.teamMotto)).setText(team.getMotto());
        ((TextView) findViewById(R.id.teamOwner)).setText(team.getOwner());

        findViewById(R.id.teamName).setEnabled(false);
        findViewById(R.id.teamMotto).setEnabled(false);
        findViewById(R.id.teamOwner).setEnabled(false);

        if (team.getOwner().equals(user.getUsername())) {
            setupTransferOwnershipButton();
            setupEditTeamButton();
        } else {
            findViewById(R.id.editTeamButton).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent result = new Intent();
        result.putExtra("user", user);
        setResult(0, result);

        finish();
    }
}
