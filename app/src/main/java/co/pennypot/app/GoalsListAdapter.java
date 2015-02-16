package co.pennypot.app;

import android.content.Context;
import android.util.Log;
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
        GoalProgressBarView progressBar;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SwipeListViewRow row = null;
        ViewHolder holder;

        if (convertView != null) {
            row = (SwipeListViewRow) convertView;
        }

        if (row == null) {
            row = SwipeListViewRow.inflate(parent);

            holder = new ViewHolder();
            holder.tvName = (TextView) row.findViewById(R.id.goal_name);
            holder.tvProgress = (TextView) row.findViewById(R.id.goal_progress);
            holder.progressBar = (GoalProgressBarView) row.findViewById(R.id.progress_bar);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Goal goal = getItem(position);
        holder.tvName.setText(goal.getName());
        holder.tvProgress.setText(formatProgressString(goal));
        holder.progressBar.setProgress(goal.getProgressPercentage());
        Log.d("GoalsListAdapter", "Goal : " + goal.getName() + " progress: " + goal.getProgressPercentage());

        return row;
    }

    private static String formatProgressString(Goal goal) {
        return String.format("$%d of $%d", goal.getBalance(), goal.getTarget());
    }

}
