package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.opengl.Visibility;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

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
                final Team teamToUpdate = user.getUserTeams().get(user.getUserTeams().indexOf(team));

                AccountManager.transferOwnership(team, ((EditText) findViewById(R.id.teamOwner)).getText().toString(), new BooleanResultListener() {
                    @Override
                    public void onResult(boolean result) {
                        if (result) {
                            teamToUpdate.setOwner(((EditText) findViewById(R.id.teamOwner)).getText().toString());
                            AccountManager.updateUser(user);

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
                    findViewById(R.id.inviteUserButton).setVisibility(View.VISIBLE);
                    findViewById(R.id.lblMessage).setVisibility(View.VISIBLE);
                    editTeamButton.setText(R.string.submitChanges);
                } else {
                    findViewById(R.id.teamName).setEnabled(false);
                    findViewById(R.id.teamMotto).setEnabled(false);
                    findViewById(R.id.teamOwner).setEnabled(false);
                    findViewById(R.id.transferOwnerButton).setVisibility(View.INVISIBLE);
                    findViewById(R.id.inviteUserButton).setVisibility(View.INVISIBLE);
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
    private void setupInviteUser(){
        final Button inviteUserButton = findViewById(R.id.inviteUserButton);

        inviteUserButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if (inviteUserButton.getText().toString().equals(getString(R.string.inviteUser))) {
                      findViewById(R.id.inviteUsername).setVisibility(View.VISIBLE);
                      findViewById(R.id.inviteUsername).setEnabled(true);
                      inviteUserButton.setText(R.string.sendInvite);
                  }
                  else {
                      findViewById(R.id.inviteUsername).setVisibility(View.GONE);
                      findViewById(R.id.inviteUsername).setEnabled(false);
                      String inviteUser = ((TextView) findViewById(R.id.inviteUsername)).getText().toString();
                      inviteUserButton.setText(R.string.inviteUser);
                      //Write some firebase stuff.
                      TeamManager.inviteUser(inviteUser,team);
                      ((TextView)findViewById(R.id.lblMessage)).setText("Invitation sent!");
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


        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                team.getTeamMembers());
        ((ListView)findViewById(R.id.teamMembersListView)).setAdapter(adapter);
        ((ListView)findViewById(R.id.teamMembersListView)).setVisibility(View.VISIBLE);

        findViewById(R.id.teamName).setEnabled(false);
        findViewById(R.id.teamMotto).setEnabled(false);
        findViewById(R.id.teamOwner).setEnabled(false);
        findViewById(R.id.inviteUsername).setVisibility(View.GONE);
        findViewById(R.id.inviteUserButton).setVisibility(View.GONE);

        if (team.getOwner().equals(user.getUsername())) {
            setupTransferOwnershipButton();
            setupInviteUser();
            setupEditTeamButton();

            //TODO Make a setup for the invite button.

        } else {
            findViewById(R.id.editTeamButton).setVisibility(View.INVISIBLE);
        }

        populateMemberList();
    }

    @Override
    public void onBackPressed() {
        Intent result = new Intent();
        result.putExtra("user", user);
        setResult(0, result);

        finish();
    }

    /**
     * Populates the list of teams associated with the user
     */
    private void populateMemberList() {
        // Get the layout to add exercises to
        final LinearLayout layout = findViewById(R.id.membersLinearLayout);
        layout.removeAllViewsInLayout();



        final ArrayList<String> teamMembers = new ArrayList<>();

        TeamManager.getTeamMembers(team, new TeamManager.TeamPopulatedListener() {
            @Override
            public void onTeamPopulated(Team team) {
                List<String> members = team.getTeamMembers();
                boolean alternateColor = false;

                for (int i = 0; i < team.getTeamMembers().size(); i++) {
                    teamMembers.add(team.getTeamMembers().get(i));
                }
                // Iterate and add exercises to screen
                for (String user : teamMembers) {
                    // Build a new TextView for this team
                    TextView teamText = new TextView(TeamDetailActivity.this);

                    teamText.setText(user);
                    teamText.setTextSize(28);
                    teamText.setPadding(0, 30, 0, 30);

                    if (alternateColor) teamText.setBackgroundColor(Color.LTGRAY);

                    teamText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 4, 0, 0);

                    teamText.setLayoutParams(params);

                    // Add a click listener to show more information
                    teamText.setOnClickListener(new TeamDetailActivity.TeamMemberClickListener(user));
                    // Add the text view to the screen
                    layout.addView(teamText);

                    alternateColor = !alternateColor;
                }
            }
        });


        // Get the user's list of exercises
        //List<String> users = team.getTeamMembers();


    }

    class TeamMemberClickListener implements View.OnClickListener {
        private String user;

        public TeamMemberClickListener(String user) {
            this.user = user;
        }

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(TeamDetailActivity.this);

            builder
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }})
                .setTitle("Team Member Info")
                .setMessage("\nUsername: " + user)
                .show();
        }
    }
}
