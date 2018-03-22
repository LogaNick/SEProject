package ca.dal.cs.athletemonitor.athletemonitor;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by nicholasbarreyre on 2018-03-10.
 *
 * The Goal class, when instantiated, can be used store and manipulate data about a particular gaol
 */

public class Goal implements Serializable {
    private String name;
    private Date deadline;
    private String description;


    /**
     * No args constructor
     */
    public Goal(){}

    public Goal(String goalName){
        this.name = goalName;
    }

    /**
     * Creates goal
     *
     * @param name
     * @param description
     */
    public Goal(String name, String description){
        this.name = name;
        this.description = description;
    }

    /**
     * Retrieve name of this goal
     * @return name of this goal
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this goal
     * @param name of goal
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Check if string can be used as a name for a goal
     * @param name
     * @return true if given name is valid
     */
    public static Boolean validateGoalName(String name){
        Boolean result = true;

        return result;
    }

    /**
     * Set the deadling of this goal
     *
     * @param date
     */
    public void setDeadline(Date date){
        this.deadline = date;
    }

    /**
     * Get the deadline of this goal.
     *
     * @return deadline
     */
    public Date getDeadline(){
        return this.deadline;
    }

    /**
     * Retrieve description of this goal
     * @return description of this goal
     */
    public String getDescription() {
        return description;
    }

    /**
     * Validate the name of a goal
     * @param name to be used for goal
     * @return whether name is valid
     */
    public static boolean validateName(String name) {
        return name.length() > 0 && name.length() <= 20;
    }

    /**
     * Validate the description of the goal
     * @param description to be used for the goal
     * @return whether description is valid
     */
    public static boolean validateDescription(String description){
        return description.length() > 0 && description.length() <= 500;
    }

    /**
     * Static validator methods to check each of the fields.
     * @param name Name of exercise to check
     * @param description Description of exercise
     * @return True if the exercise is valid
     */
    public static boolean validateAll(String name, String description){
        return validateName(name) && validateDescription(description);
    }

}
