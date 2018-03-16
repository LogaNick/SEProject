package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TeamDetailActivity extends AppCompatActivity {
    private EditText teamName, teamMotto;
    private Button editTeamButton, updateTeamButton;
    private Team team;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);

        team = (Team) getIntent().getExtras().getSerializable("team");
        
        // get the active user
        user = (User) getIntent().getExtras().getSerializable("user");

        editTeamButton = findViewById(R.id.editTeamButton);
        updateTeamButton = findViewById(R.id.updateTeamButton);
        teamName = findViewById(R.id.teamName);
        teamMotto = findViewById(R.id.teamMotto);

        teamName.setText(team.getName());
        teamMotto.setText(team.getMotto());

        teamName.setEnabled(false);
        teamMotto.setEnabled(false);
        updateTeamButton.setVisibility(View.GONE);

        editTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamName.setEnabled(true);
                teamMotto.setEnabled(true);
                editTeamButton.setVisibility(View.GONE);
                updateTeamButton.setVisibility(View.VISIBLE);
            }
        });
        updateTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamName.setEnabled(true);
                teamMotto.setEnabled(true);
                editTeamButton.setEnabled(true);
                updateTeamButton.setVisibility(View.GONE);

                //Update the info onto the team menbers.

            }
        });


    }
}
