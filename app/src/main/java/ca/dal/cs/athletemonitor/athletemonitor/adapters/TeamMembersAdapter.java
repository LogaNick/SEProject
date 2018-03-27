package ca.dal.cs.athletemonitor.athletemonitor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ca.dal.cs.athletemonitor.athletemonitor.R;

/**
 * Custom adapter for representing teams
 * Source: https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */
public class TeamMembersAdapter extends ArrayAdapter<String> {
    public TeamMembersAdapter(Context context, ArrayList<String> members) {
        super(context, 0, members);
    }

    // View lookup cache
    private static class ViewHolder {
        TextView name;
        TextView lastOnline;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String member = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_team_member, parent, false);
            viewHolder.name = convertView.findViewById(R.id.teamName);
            viewHolder.lastOnline = (TextView) convertView.findViewById(R.id.onlineTime);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.name.setText(member);
        //TODO: add AccountManager method to get last online time, setLoginState to record time of login
        viewHolder.lastOnline.setText((CharSequence)"never");

        // Return the completed view to render on screen
        return convertView;
    }
}
