package ca.dal.cs.athletemonitor.athletemonitor;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import ca.dal.cs.athletemonitor.athletemonitor.adapters.TeamsAdapter;

public class TeamActivity extends AppCompatActivity {
    /**
     * Adapter for team ListView component
     */
    private TeamsAdapter teamAdapter;

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
        this.user = (User) getIntent().getExtras().getSerializable("user");
        this.populateTeamList();
        this.handleTeamInvitations();
    }

    /**
     * Configures options menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_team, menu);

        MenuItem searchItem = menu.findItem(R.id.action_join_team);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(((SearchManager)getSystemService(Context.SEARCH_SERVICE)).getSearchableInfo(getComponentName()));
        searchView.onActionViewExpanded();

        return true;
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
            case R.id.action_create_team:
                //TODO: update for startActivityForResult...
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
                                    team.addTeamMember(user.getUsername());
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
        //Get the reference to the UI contents
        final ListView teamListView = findViewById(R.id.teamList);

        // retrieve database reference to the teams
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference teamsReference = database.getReference("teams");

        teamAdapter = new TeamsAdapter(this, new ArrayList<Team>());

        teamsReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue(Team.class).getTeamMembers().contains(user.getUsername())) {
                    teamAdapter.add(dataSnapshot.getValue(Team.class));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue(Team.class).getTeamMembers().contains(user.getUsername())) {
                    //TODO: UPdate team information
                    //teamAdapter.add(dataSnapshot.getValue(Team.class));
                } else if (teamAdapter.getPosition(dataSnapshot.getValue(Team.class)) != -1) {
                    teamAdapter.remove(dataSnapshot.getValue(Team.class));
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Team.class).getTeamMembers().contains(user.getUsername())) {
                    teamAdapter.remove(dataSnapshot.getValue(Team.class));
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("TeamList", dataSnapshot.getValue(Team.class).getName() + "has moved locations...");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        teamListView.setAdapter(teamAdapter);
        teamListView.setOnItemClickListener(new TeamListClickListener());
    }

    /**
     * Team ListView item click listener
     */
    private class TeamListClickListener implements AdapterView.OnItemClickListener {
        /**
         * Callback method to be invoked when an item in this AdapterView has
         * been clicked.
         * <p>
         * Implementers can call getItemAtPosition(position) if they need
         * to access the data associated with the selected item.
         *
         * @param parent   The AdapterView where the click happened.
         * @param view     The view within the AdapterView that was clicked (this
         *                 will be a view provided by the adapter)
         * @param position The position of the view in the adapter.
         * @param id       The row id of the item that was clicked.
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final Team team = (Team) teamAdapter.getItem(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(TeamActivity.this);

            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }})
                    .setPositiveButton("More", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Intent TeamDetailActivityIntent = new Intent(TeamActivity.this, TeamDetailActivity.class);
                            TeamDetailActivityIntent.putExtra("team", team);
                            TeamDetailActivityIntent.putExtras(getIntent().getExtras());
                            startActivityForResult(TeamDetailActivityIntent, 1);
                        }})
                    .setTitle(team.getName())
                    .setMessage("\nMotto: " + team.getMotto() + "\nOwner: " + team.getOwner());

            // only display quit team if the team isn't owned by the user
            if (!team.getOwner().equals(user.getUsername())) {
                builder.setNeutralButton("Quit Team", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        teamAdapter.remove(team);
                        TeamManager.removeMemberFromTeam(team, user);
                        dialog.dismiss();
                    }
                });
            }
            builder.show();
        }
    }
}
