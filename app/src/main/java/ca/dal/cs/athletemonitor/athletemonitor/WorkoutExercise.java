package ca.dal.cs.athletemonitor.athletemonitor;

/**
 * Created by Brent on 2018-02-27.
 */

public class WorkoutExercise extends Exercise{

    private int data;

    public WorkoutExercise ()
    {
        this.data = 0;
    }

    public int getData ()
    {
        return this.data;
    }

    public void setData (int d)
    {
        this.data = d;
    }

}
