package ca.dal.cs.athletemonitor.athletemonitor;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * The TeamManager class manages Team data in Firebase
 */

public class TeamManager {
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
        DatabaseReference usersReference = database.getReference("*teams_invitations");
        //attempt to write the data
        usersReference.child(username).setValue(team);
    }
}
