package ca.dal.cs.athletemonitor.athletemonitor;

import android.app.Activity;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import ca.dal.cs.athletemonitor.athletemonitor.adapters.TeamsAdapter;
import ca.dal.cs.athletemonitor.athletemonitor.listeners.BooleanResultListener;

public class SearchResultsActivity extends AppCompatActivity {
    /**
     * Adapter for connecting database results to the results list view
     */
    private TeamsAdapter teamAdapter;

    /**
     * Current user using the application
     */
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        setWindowSize(0.9, 0.6);
        this.setFinishOnTouchOutside(false);
        handleIntent(getIntent());
    }

    /**
     * Adjust activity window size to a percentage of height/width
     *
     * @param widthScale Percentage of window width
     * @param heightScale Percentage of window height
     */
    private void setWindowSize(double widthScale, double heightScale) {
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        this.getWindow().setLayout((int)(displayRectangle.width() * widthScale),
                (int)(displayRectangle.height() * heightScale));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    /**
     * Perform the search based on intent
     *
     * @param intent
     */
    private void handleIntent(Intent intent) {


        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            Bundle appData = getIntent().getBundleExtra(SearchManager.APP_DATA);
            if (appData != null) {
                this.user = (User) appData.getSerializable("user");
            }

            String query = intent.getStringExtra(SearchManager.QUERY);
            populateListWithSearchResults(query);
        }
    }

    private void populateListWithSearchResults(final String query) {
        //Get the reference to the UI contents
        final ListView teamListView = findViewById(R.id.teamList);

        // retrieve database reference to the teams
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference teamsReference = database.getReference("teams");

        teamAdapter = new TeamsAdapter(this, new ArrayList<Team>());

        teamsReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // only add teams with names that partially match the query string
                //TODO: Don't add teams already a member of
                if (dataSnapshot.getValue(Team.class).getName().toUpperCase().contains(query.toUpperCase())) {
                    teamAdapter.add(dataSnapshot.getValue(Team.class));
                    teamAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // only add teams with names that partially match the query string
                //TODO: Don't add teams already a member of
                if (dataSnapshot.getValue(Team.class).getName().toUpperCase().contains(query.toUpperCase())) {
                    teamAdapter.add(dataSnapshot.getValue(Team.class));
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (teamAdapter.getPosition(dataSnapshot.getValue(Team.class)) != -1) {
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
        teamListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Team team = teamAdapter.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(SearchResultsActivity.this);

                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }});

                builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final AlertDialog.Builder confirmDialog = new AlertDialog.Builder(SearchResultsActivity.this);

                        confirmDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        confirmDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TeamManager.requestToJoinTeam(team, user);
                                dialog.dismiss();
                                finish();
                            }
                        });

                        confirmDialog.setTitle("Confirm Application")
                                .setMessage("Are you sure you want to apply to " + team.getName() + "?")
                                .show();

                    }
                });

                builder.setTitle("Apply To Team")
                        .setMessage("\nDo you want to apply to " + team.getName())
                        .show();

            }
        });
    }
}



