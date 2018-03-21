package ca.dal.cs.athletemonitor.athletemonitor;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * The TeamManager class manages Team data in Firebase
 */

public class TeamManager {
    /**
     * Listener interface for receiving team invitations.
     *
     * Callers of userExists must implement this listener interface.
     */
    public interface TeamInvitationListener {
        void onInvitationsPopulated(ArrayList<Team> invitations);
    }

    private TeamManager() {}

    /**
     * Submit team data to firebase
     * @param team The team data to be submitted
     *
     * @return whether team creation is successful
     */
    public static boolean newTeam(Team team){
        // retrieve database reference to the teams
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference teamsReference = database.getReference("teams");

        // Submit the team to the database
        teamsReference.child(team.getName()).setValue(team);

        // TODO check whether the team was successfully created (for example, does not already exist)
        return true;
    }
    /**
     This method invite a user to a team.

     It create an invitation on the Teaminvitation branch on firebase.
     */
    public static void inviteUser(String username, final Team team){
        //if (Username not in ) //The aim here is to test if the user is not already on the team.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersReference = database.getReference("teams_invitations");
        //attempt to write the data
        usersReference.child(username).setValue(team);
    }

    /**
     * Retrieves a list of team invitations for the specified user.  Returns a list of teams
     * where invitations are active through the callback.  If no invitations are active then the
     * returned list is empty.
     *
     * @param user User to check for team invitations
     * @param listener Callback to receive list of invitations
     */
    public static void getTeamInvites(User user, final TeamInvitationListener listener) {
        // retrieve database reference to the teams
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference teamsReference = database.getReference("teams_invitations/" + user.getUsername());

        teamsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // return an empty list if no records found
                if (!dataSnapshot.exists()) {
                    listener.onInvitationsPopulated(new ArrayList<Team>());
                }

                // iterate through the snapsnot and add all of the team invitations
                Iterable<DataSnapshot> invitations = dataSnapshot.getChildren();
                ArrayList<Team> teamInvitations = new ArrayList<>();
                for (DataSnapshot invitation : invitations) {
                    Team team = new Team(invitation.child("name").toString(), "", invitation.child("owner").toString());
                    teamInvitations.add(team);
                }

                listener.onInvitationsPopulated(teamInvitations);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
