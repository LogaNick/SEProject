package ca.dal.cs.athletemonitor.athletemonitor;

/**
 * The Team class contains data for a team
 */

public class Team {
    private String name;
    private String motto;

    /**
     * No args constructor used primarily for database based initialization of exercise objects
     */
    public Team() { /* intentionally left blank */ }

    /**
     * Constructor to create an exercise with all necessary information. This validates the
     * parameters and throws an exception if any are invalid.
     *
     * @param name Name of team
     * @param motto Team motto
     */
    public Team(String name, String motto){
        if (!(Team.validateAll(name, motto))) {
            throw new IllegalArgumentException("Invalid Exercise parameters");
        }

        this.name = name;
        this.motto = motto;
    }

    /**
     * Retrieve name of team
     * @return team name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the team name
     * @param name of team
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieve team motto
     * @return team motto
     */
    public String getMotto() {
        return motto;
    }

    /**
     * Set the team motto
     * @param motto of team
     */
    public void setMotto(String motto) {
        this.motto = motto;
    }

    public static boolean validateName(String name){
        return name.length() > 0 && name.length() <= 20;
    }

    public static boolean validateMotto(String motto){
        return motto.length() > 0 && motto.length() <= 50;
    }

    public static boolean validateAll(String name, String motto){
        return validateName(name) && validateMotto(motto);
    }
}
