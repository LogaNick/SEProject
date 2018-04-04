package ca.dal.cs.athletemonitor.athletemonitor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ca.dal.cs.athletemonitor.athletemonitor.R;
import ca.dal.cs.athletemonitor.athletemonitor.Team;

/**
 * Custom adapter for representing teams
 * Source: https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */
public class TeamsAdapter extends ArrayAdapter<Team> {
    public TeamsAdapter(Context context, ArrayList<Team> teams) {
        super(context, 0, teams);
    }

    // View lookup cache
    private static class ViewHolder {
        TextView name;
        TextView owner;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Team team = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_team, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.teamName);
            viewHolder.owner = (TextView) convertView.findViewById(R.id.teamOwner);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.name.setText(team.getName());
        viewHolder.owner.setText(team.getOwner());

        // Return the completed view to render on screen
        return convertView;
    }
}
