
package ca.dal.cs.athletemonitor.athletemonitor;

import java.util.ArrayList;

/**
 * Workout class manages the data for a workout, which is a collection of exercises.
 */

public class Workout {

    private String name;
    private ArrayList<WorkoutExercise> exercises;
    private boolean completed;

    /**
     * No args constructor used primarily for database based initialization of workout objects
     */
    public Workout() { /* intentionally left blank */ }

    public Workout(String name)
    {
        this.name = name;
        this.exercises = new ArrayList<>();
        this.completed = false;
    }

    public boolean isCompleted ()
    {
        return this.completed;
    }

    public void setCompleted (boolean value)
    {
        this.completed = value;
    }

    public void addWorkoutExercise (WorkoutExercise exercise)
    {
        this.exercises.add(exercise);
    }

    public ArrayList<WorkoutExercise> getExercises ()
    {
        return this.exercises;
    }

    public String toString()
    {
        return this.name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName (String n) { this.name = n; }
}