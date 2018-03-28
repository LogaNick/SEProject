package ca.dal.cs.athletemonitor.athletemonitor;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ca.dal.cs.athletemonitor.athletemonitor.listeners.BooleanResultListener;

/**
 * The TeamManager class manages Team data in Firebase
 */

public abstract class TeamManager {
    /**
     * Listener interface for receiving team invitations.
     *
     * Callers of userExists must implement this listener interface.
     */
    public interface TeamInvitationListener {
        void onInvitationsPopulated(ArrayList<Team> invitations);
    }

    /**
     * Listener interface for receiving team information.
     *
     * Callers of userExists must implement this listener interface.
     */
    public interface TeamPopulatedListener {
        void onTeamPopulated(Team team);
    }

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

        //generate a new business id
        team.setId(teamsReference.push().getKey());

        // Submit the team to the database
        teamsReference.child(team.getId()).setValue(team);

        // TODO check whether the team was successfully created (for example, does not already exist)
        return true;
    }

    /**
     This method invite a user to a team.

     It create an invitation on the Teaminvitation branch on firebase.
     */
    public static void inviteUser(final String username, final Team team){
        //if (Username not in ) //The aim here is to test if the user is not already on the team.
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference usersReference = database.getReference("teams_invitations");
        //attempt to write the data

        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(username)) {
                    usersReference.child(username).setValue(username);
                }

                DatabaseReference userReference = database.getReference("teams_invitations/" + username);
                userReference.child(userReference.push().getKey()).setValue(team);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        final ArrayList<Team> teamInvitations = new ArrayList<>();

        // retrieve database reference to the teams
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference teamsReference = database.getReference("teams_invitations/" + user.getUsername());

        teamsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // return an empty list if no records found
                if (!dataSnapshot.exists()) {
                    listener.onInvitationsPopulated(new ArrayList<Team>());
                    return;
                }

                Iterable<DataSnapshot> invitations = dataSnapshot.getChildren();

                for (DataSnapshot invitation : invitations) {
                    teamInvitations.add(invitation.getValue(Team.class));
                }

                listener.onInvitationsPopulated(teamInvitations);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Retrieves a team from firebase for accessing the list of team members
     *
     * @param team Team to access
     * @param listener Callback to pass information to
     */
    public static void getTeamMembers(final Team team, final TeamPopulatedListener listener) {
        // retrieve database reference to the teams
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference teamsReference = database.getReference("teams/" + team.getId());

        teamsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listener.onTeamPopulated(dataSnapshot.getValue(Team.class));
                    return;
                } else {
                    listener.onTeamPopulated(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Updates team membership based on whether the user accepted or declined the invitation
     *
     * @param user User responding to an invitation
     * @param team Team associated with the invitation
     * @param accepted Whether or not the user accepted or declined.  True if accepted, otherwise false
     */
    public static void handleInvitation(final User user, final Team team, boolean accepted) {
        // retrieve database reference to the teams
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference teamOwnerReference = database.getReference("users/" + team.getOwner());

        if (accepted) {
            teamOwnerReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    TeamManager.addMemberToTeam(team, user);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        // retrieve database reference to the teams
        final DatabaseReference userInvitations = database.getReference("teams_invitations/" + user.getUsername());

        userInvitations.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> invitations = dataSnapshot.getChildren();

                for (DataSnapshot invitation : invitations) {
                    if (invitation.getValue(Team.class).getName().equals(team.getName())) {
                        userInvitations.child(invitation.getKey()).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Removes the user from the specified team.
     *
     * @param team Team that user belongs to
     * @param user User to be removed
     */
    public static void removeMemberFromTeam(Team team, final User user) {
        // retrieve database reference to the teams
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference teamsReference = database.getReference("teams/" + team.getId() + "/teamMembers");

        teamsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> members = dataSnapshot.getChildren();

                    for (DataSnapshot member : members) {
                        if (((String)member.getValue()).equals(user.getUsername())) {
                            teamsReference.child(member.getKey()).removeValue();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Adds the user to the specified team.
     *
     * @param team Team that user belongs to
     * @param user User to be removed
     */
    public static void addMemberToTeam(Team team, final User user) {
        // retrieve database reference to the teams
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference teamsReference = database.getReference("teams/" + team.getId());

        teamsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Team team = dataSnapshot.getValue(Team.class);
                    team.addTeamMember(user.getUsername());

                    teamsReference.setValue(team);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void updateTeam(Team team, final BooleanResultListener listener) {
        if(AccountManager.isOnline()) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference usersReference = database.getReference("teams/" + team.getId());
            usersReference.setValue(team, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    listener.onResult(databaseError == null);
                }
            });
        } else {
            listener.onResult(false);
        }

    }


}
