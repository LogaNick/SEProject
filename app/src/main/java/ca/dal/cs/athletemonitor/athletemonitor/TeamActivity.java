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

public class TeamActivity extends AppCompatActivity {
    /**
     * Sets up activity when created
     *
     * @param savedInstanceState Instance state loaded from a previously killed app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        // Click listener for create new exercise button
        findViewById(R.id.createTeamButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createTeamActivityIntent = new Intent(TeamActivity.this, CreateTeamActivity.class);

                createTeamActivityIntent.putExtras(getIntent().getExtras());
                startActivity(createTeamActivityIntent);
            }
        });

        // retrieves the user from Firebase based on username passed in extras
        AccountManager.getUser(getIntent().getExtras().getString("username"), new AccountManager.UserObjectListener() {
            @Override
            public void onUserPopulated(User user) {
                // Get the layout to add exercises to
                LinearLayout layout = findViewById(R.id.teamLinearLayout);

                boolean alternateColor = false;

                // Get the user's list of exercises
                List<Team> teams = user.getUserTeams();

                // Iterate and add exercises to screen
                for (Team team : teams) {
                    // Build a new TextView for this team
                    TextView teamText = new TextView(TeamActivity.this);

                    teamText.setText(team.getName());
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
                    teamText.setOnClickListener(new TeamActivity.DialogOnClickListener(team));
                    // Add the text view to the screen
                    layout.addView(teamText);

                    alternateColor = !alternateColor;
                }
            }
        });
    }

    /**
     * Listener for the individual team in the list of teams.
     */
    class DialogOnClickListener implements View.OnClickListener{
        Team team;

        public DialogOnClickListener(Team team){
            this.team = team;
        }

        @Override
        public void onClick(View v){
            AlertDialog.Builder builder = new AlertDialog.Builder(TeamActivity.this);

            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }})
                    .setPositiveButton("More", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                    }})
                    .setTitle(team.getName())
                    .setMessage("\nMotto: " + team.getMotto() + "\nOwner: " + team.getOwner())
                    .show();
        }
    }
}
