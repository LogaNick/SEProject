package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TeamDetailActivity extends AppCompatActivity {
    private EditText teamName, teamMotto;
    private Button editTeamButton, updateTeamButton;
    private Team team, newTeam;
    private List<Team> userTeams;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);

        // get the active user
        user = (User) getIntent().getExtras().getSerializable("user");
        team = (Team) getIntent().getExtras().getSerializable("team");

        editTeamButton = findViewById(R.id.editTeamButton);
        teamName = findViewById(R.id.teamName);
        teamMotto = findViewById(R.id.teamMotto);

        teamName.setText(team.getName());
        teamMotto.setText(team.getMotto());

        teamName.setEnabled(false);
        teamMotto.setEnabled(false);

        editTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTeamButton.getText().toString().equals(getString(R.string.editTeam))) {
                    teamName.setEnabled(true);
                    teamMotto.setEnabled(true);
                    editTeamButton.setText(R.string.submitChanges);
                } else {
                    teamName.setEnabled(false);
                    teamMotto.setEnabled(false);
                    editTeamButton.setEnabled(true);

                    String name = ((TextView) findViewById(R.id.teamName)).getText().toString();
                    String motto = ((TextView) findViewById(R.id.teamMotto)).getText().toString();
                    String owner = user.getUsername();

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
}
