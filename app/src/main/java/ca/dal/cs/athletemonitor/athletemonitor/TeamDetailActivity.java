package ca.dal.cs.athletemonitor.athletemonitor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
        //TODO: Move quit team here
        if (user.getUsername().equals(team.getOwner())) {
        getMenuInflater().inflate(R.menu.menu_team_detail, menu);

            //menu.findItem(R.id.action_edit_team).setVisible(false);
        }
//        MenuItem searchItem = menu.findItem(R.id.action_join_team);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setSearchableInfo(((SearchManager)getSystemService(Context.SEARCH_SERVICE)).getSearchableInfo(getComponentName()));
//        searchView.onActionViewExpanded();

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
            case R.id.action_edit_team:
                setEditTextEditable(R.id.teamName, true);
                setEditTextEditable(R.id.teamMotto, true);
                findViewById(R.id.saveInfo).setVisibility(View.VISIBLE);
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

            builder.setTitle("Team Member")
                   .setMessage("\nUsername: " + member)
                   .show();
        }
    }

    /**
     * Configures the transfer ownership button
     */
    private void setupTransferOwnershipButton() {
//        final Button transferOwnershipButton = findViewById(R.id.transferOwnerButton);
//
//        transferOwnershipButton.setOnClickListener(new View.OnClickListener() {
//            /**
//             * Set the components on this activity to editable if not currently editing, otherwise
//             * update the database with the new information and return to the previous activity
//             *
//             * @param v View that received the click event
//             */
//            @Override
//            public void onClick(View v) {
//                final Team teamToUpdate = user.getUserTeams().get(user.getUserTeams().indexOf(team));
//
//                AccountManager.transferOwnership(team, ((EditText) findViewById(R.id.teamOwner)).getText().toString(), new BooleanResultListener() {
//                    @Override
//                    public void onResult(boolean result) {
//                        if (result) {
//                            teamToUpdate.setOwner(((EditText) findViewById(R.id.teamOwner)).getText().toString());
//                            AccountManager.updateUser(user);
//
//                            ((Button) findViewById(R.id.editTeamButton)).setVisibility(View.INVISIBLE);
//                            ((Button) findViewById(R.id.transferOwnerButton)).setVisibility(View.INVISIBLE);
//                            ((TextView) findViewById(R.id.lblMessage)).setText(R.string.ownershipTransferred);
//
//                        } else {
//                            ((TextView) findViewById(R.id.lblMessage)).setText(R.string.ownershipTransferredFailed);
//                        }
//                    }
//                });
//            }
//        });
    }

//    private void setupInviteUser(){
//        final Button inviteUserButton = findViewById(R.id.inviteUserButton);
//
//        inviteUserButton.setOnClickListener(new View.OnClickListener() {
//              @Override
//              public void onClick(View v) {
//                  if (inviteUserButton.getText().toString().equals(getString(R.string.inviteUser))) {
//                      findViewById(R.id.inviteUsername).setVisibility(View.VISIBLE);
//                      findViewById(R.id.inviteUsername).setEnabled(true);
//                      inviteUserButton.setText(R.string.sendInvite);
//                  }
//                  else {
//                      findViewById(R.id.inviteUsername).setVisibility(View.GONE);
//                      findViewById(R.id.inviteUsername).setEnabled(false);
//                      String inviteUser = ((TextView) findViewById(R.id.inviteUsername)).getText().toString();
//                      inviteUserButton.setText(R.string.inviteUser);
//                      //Write some firebase stuff.
//                      TeamManager.inviteUser(inviteUser,team);
//                      ((TextView)findViewById(R.id.lblMessage)).setText("Invitation sent!");
//                  }
//              }
//        });
//    }

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
