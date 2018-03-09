package ca.dal.cs.athletemonitor.athletemonitor;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Unit tests for the Team class
 */

public class TeamTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testValidTeam(){
        String name = "Warriors";
        String motto = "We have swords!";
        String owner = "team_owner";

        // Assert all fields are valid
        assertTrue(Team.validateAll(name, motto));
        // Create new team and expect no exception
        new Team(name, motto);
    }

    @Test
    public void testInvalidTeam(){
        String name = "We love really long team names that don't make sense";
        String motto = "";
        String owner = "";

        assertFalse(Team.validateName(name));
        assertFalse(Team.validateMotto(motto));
        assertFalse(Team.validateOwner(owner));
        assertFalse(Team.validateAll(name, motto));

        // Expect an exception when creating the new Team
        exception.expect(IllegalArgumentException.class);
        new Team(name, motto);
    }


}
