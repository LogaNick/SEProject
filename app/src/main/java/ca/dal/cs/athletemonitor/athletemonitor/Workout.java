
package ca.dal.cs.athletemonitor.athletemonitor;

import java.util.ArrayList;

/**
 * Created by Brent on 2018-02-16.
 */

public class Workout {

    private int id;
    private String name;
    private ArrayList<WorkoutExercise> exercises;
    private boolean active;
    private boolean completed;

    public Workout(int id)
    {
        this.id = id;
        this.name = "Workout "+id;
        this.exercises = new ArrayList<>();
        this.active = true;
    }

    public void setCompleted (boolean value)
    {
        this.completed = value;
    }

    public boolean isCompleted ()
    {
        return this.completed;
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

}










