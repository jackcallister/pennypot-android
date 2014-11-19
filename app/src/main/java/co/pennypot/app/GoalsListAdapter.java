package co.pennypot.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import co.pennypot.app.models.Goal;

public class GoalsListAdapter extends ArrayAdapter<Goal> {

    public GoalsListAdapter(Context context, List<Goal> objects) {
        super(context, 0, objects);
    }

    static class ViewHolder {
        TextView tvName;
        TextView tvProgress;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_goal, null);

            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.goal_name);
            holder.tvProgress = (TextView) convertView.findViewById(R.id.goal_progress);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Goal goal = getItem(position);
        holder.tvName.setText(goal.getName());
        holder.tvProgress.setText(formatProgressString(goal));

        return convertView;
    }

    private static String formatProgressString(Goal goal) {
        return String.format("$%d of $%d", goal.getBalance(), goal.getTarget());
    }
}
