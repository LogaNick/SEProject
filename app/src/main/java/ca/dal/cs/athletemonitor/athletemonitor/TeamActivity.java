package ca.dal.cs.athletemonitor.athletemonitor;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class TeamActivity extends AppCompatActivity {
    /**
     * Current user using the application
     */
    private User user;

    /**
     * Sets up activity when created
     *
     * @param savedInstanceState Instance state loaded from a previously killed app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        // get the active user
        user = (User) getIntent().getExtras().getSerializable("user");

        this.populateTeamList();
        this.handleTeamInvitations();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_team, menu);

        MenuItem searchItem = menu.findItem(R.id.action_join_team);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(((SearchManager)getSystemService(Context.SEARCH_SERVICE)).getSearchableInfo(getComponentName()));
        return true;//super.onCreateOptionsMenu(menu);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_join_team:
                //onSearchRequested();
                super.onOptionsItemSelected(item);
                return true;
            case R.id.action_create_team:
                Intent createTeamActivityIntent = new Intent(TeamActivity.this, CreateTeamActivity.class);

                createTeamActivityIntent.putExtra("user", user);
                startActivity(createTeamActivityIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Manages team invitations
     */
    private void handleTeamInvitations() {
        TeamManager.getTeamInvites(user, new TeamManager.TeamInvitationListener() {
            @Override
            public void onInvitationsPopulated(ArrayList<Team> invitations) {
                for (int i = 0; i < invitations.size(); i++) {
                    final Team team = invitations.get(i);
                    AlertDialog.Builder builder = new AlertDialog.Builder(TeamActivity.this);

                    builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            TeamManager.handleInvitation(user, team, false);
                            dialog.dismiss();
                        }})
                            .setPositiveButton("Accept", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    team.addTeamMembers(user.getUsername());
                                    user.addUserTeam(team);
                                    AccountManager.updateUser(user);
                                    TeamManager.handleInvitation(user, team, true);
                                    populateTeamList();
                                    dialog.dismiss();
                                }})
                            .setTitle("You have an invitation!")
                            .setMessage("\nTeam: " + team.getName() + "\nOwner: " + team.getOwner())
                            .show();
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
                    .setNeutralButton("Quit Team", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ListIterator<Team> teams = user.getUserTeams().listIterator();
                            while(teams.hasNext()){
                                if(teams.next().getName().equals(team.getName())){
                                    teams.remove();
                                }
                            }
                            populateTeamList();
                            AccountManager.updateUser(user);
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("More", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Intent TeamDetailActivityIntent = new Intent(TeamActivity.this, TeamDetailActivity.class);
                            TeamDetailActivityIntent.putExtra("team", team);
                            TeamDetailActivityIntent.putExtras(getIntent().getExtras());
                            startActivityForResult(TeamDetailActivityIntent, 1);
                    }})
                    .setTitle(team.getName())
                    .setMessage("\nMotto: " + team.getMotto() + "\nOwner: " + team.getOwner())
                    .show();
        }
    }

    /**
     * Handles updating the activity when activated as a result of starting a child activity
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1){
            this.user = (User) data.getSerializableExtra("user");
            populateTeamList();
            this.handleTeamInvitations();
        }
    }

    /**
     * Populates the list of teams associated with the user
     */
    private void populateTeamList() {
        // Get the layout to add exercises to
        LinearLayout layout = findViewById(R.id.teamLinearLayout);
        layout.removeAllViewsInLayout();

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
}
