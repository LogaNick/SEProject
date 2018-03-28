package ca.dal.cs.athletemonitor.athletemonitor;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import ca.dal.cs.athletemonitor.athletemonitor.adapters.TeamMembersAdapter;
import ca.dal.cs.athletemonitor.athletemonitor.listeners.BooleanResultListener;

public class TeamDetailActivity extends AppCompatActivity {
    /**
     * Adapter for team member ListView component
     */
    private TeamMembersAdapter teamMemberAdapter;

    /**
     * Team currently being viewed
     */
    private Team team;

    /**
     * Current user
     */
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        user = (User) getIntent().getExtras().getSerializable("user");
        team = (Team) getIntent().getExtras().getSerializable("team");

        configureViews();
        this.populateMemberList();
    }

    /**
     * Configures options menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_team_detail, menu);
        if (user.getUsername().equals(team.getOwner())) {
            menu.findItem(R.id.action_quit_team).setVisible(false);
        } else {
            menu.findItem(R.id.action_quit_team).setVisible(true);
            menu.findItem(R.id.action_edit_team).setVisible(false);
            menu.findItem(R.id.action_invite_user).setVisible(false);
        }

        configureInviteMemberSearchView((SearchView) menu.findItem(R.id.action_invite_user).getActionView());

        return true;
    }

    /**
     * Configures the search view for inviting members to a team
     *
     * @param searchView
     */
    private void configureInviteMemberSearchView(final SearchView searchView) {
        searchView.setSearchableInfo(
                ((SearchManager)getSystemService(Context.SEARCH_SERVICE))
                        .getSearchableInfo(getComponentName()));
        searchView.onActionViewExpanded();
        searchView.setQueryHint("Search for a member");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                final AlertDialog.Builder confirmDialog = new AlertDialog.Builder(TeamDetailActivity.this);

                confirmDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                confirmDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TeamManager.inviteUser(searchView.getQuery().toString(),team);
                        searchView.setQuery("", true);
                        searchView.onActionViewCollapsed();
                        ((Toolbar)findViewById(R.id.toolbar)).collapseActionView();
                        dialog.dismiss();
                    }
                });

                confirmDialog.setTitle("Confirm Invitation")
                        .setMessage("Are you sure you want to invite " + searchView.getQuery().toString() + " to the team?")
                        .show();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setSubmitButtonEnabled(true);
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
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_team:
                setEditTextEditable(R.id.teamName, true);
                setEditTextEditable(R.id.teamMotto, true);
                findViewById(R.id.saveInfo).setVisibility(View.VISIBLE);
                return true;
            case R.id.action_quit_team:
                TeamManager.removeMemberFromTeam(team, user);
                Intent result = new Intent();
                result.putExtra("user", user);
                setResult(0, result);

                finish();
                return true;
            case R.id.action_invite_user:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Populates the list of teams associated with the user
     */
    private void populateMemberList() {
        final ListView teamMemberListView = findViewById(R.id.memberList);

        // retrieve database reference to the team members
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference teamsReference = database.getReference("teams/" + team.getId() + "/teamMembers");

        teamMemberAdapter = new TeamMembersAdapter(this, new ArrayList<String>());

        teamsReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                teamMemberAdapter.add(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                teamMemberAdapter.remove(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        teamMemberListView.setAdapter(teamMemberAdapter);
        teamMemberListView.setOnItemClickListener(new TeamDetailActivity.TeamMemberListClickListener());
    }

    /**
     * Team  Member ListView item click listener
     */
    private class TeamMemberListClickListener implements AdapterView.OnItemClickListener {
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
            final String member = teamMemberAdapter.getItem(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(TeamDetailActivity.this);

            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }});

            if (user.getUsername().equals(team.getOwner())) {

                builder.setPositiveButton("Transfer Ownership", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final AlertDialog.Builder confirmDialog = new AlertDialog.Builder(TeamDetailActivity.this);

                        confirmDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        confirmDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                team.setOwner(member);
                                TeamManager.updateTeam(team, new BooleanResultListener() {
                                    @Override
                                    public void onResult(boolean result) {

                                    }
                                });
                                dialog.dismiss();
                            }
                        });

                        confirmDialog.setTitle("Confirm Transfer")
                                .setMessage("Are you sure you want to transfer ownership to " + member + "?")
                                .show();

                    }
                });
            }

            builder.setTitle("Team Member")
                   .setMessage("\nUsername: " + member)
                   .show();
        }
    }

    /**
     * Populates the views on this activity with relevant information
     */
    private void configureViews() {
        setEditTextEditable(R.id.teamName, false);
        setEditTextEditable(R.id.teamMotto, false);

        ((EditText) findViewById(R.id.teamName)).setText(team.getName());
        ((EditText) findViewById(R.id.teamMotto)).setText(team.getMotto());

        findViewById(R.id.saveInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean changed = false;
                if (!team.getName().equals(((EditText) findViewById(R.id.teamName)).getText().toString())) {
                    team.setName(((EditText) findViewById(R.id.teamName)).getText().toString());
                    changed = true;
                }

                if (!team.getMotto().equals(((EditText) findViewById(R.id.teamMotto)).getText().toString())) {
                    team.setMotto(((EditText) findViewById(R.id.teamMotto)).getText().toString());
                    changed = true;
                }

                if (changed) {
                    TeamManager.updateTeam(team, new BooleanResultListener() {
                        @Override
                        public void onResult(boolean result) {
                            if (result) {
                                setEditTextEditable(R.id.teamName, false);
                                setEditTextEditable(R.id.teamMotto, false);
                                findViewById(R.id.saveInfo).setVisibility(View.GONE);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(TeamDetailActivity.this);

                                builder
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .setTitle("Error updating team")
                                        .setMessage("Team not updated.  Are you online?\n\nTry again.")
                                        .show();
                            }
                        }
                    });
                } else {
                    setEditTextEditable(R.id.teamName, false);
                    setEditTextEditable(R.id.teamMotto, false);
                    findViewById(R.id.saveInfo).setVisibility(View.VISIBLE);
                }

                // force the soft keyboard to close
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(TeamDetailActivity.this.getCurrentFocus().getWindowToken(), 0);
            }
        });
    }

    /**
     * Handle back button presses
     */
    @Override
    public void onBackPressed() {
        Intent result = new Intent();
        result.putExtra("user", user);
        setResult(0, result);

        finish();
    }

    /**
     * Configures an EditText view either editable or non-editable
     *
     * @param resourceId
     */
    private void setEditTextEditable(@IdRes int resourceId, boolean editState) {
        EditText textField = findViewById(resourceId);

        textField.setSelected(editState);
        textField.setCursorVisible(editState);
        textField.setBackgroundResource(android.R.color.transparent);
        textField.setClickable(editState);
        textField.setFocusable(editState);
        textField.setFocusableInTouchMode(editState);
        textField.setLongClickable(editState);

    }
}
