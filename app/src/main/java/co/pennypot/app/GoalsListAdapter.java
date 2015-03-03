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

    public interface GoalActionsListener {
        void deleteGoal(Goal goal);
        void editGoal(Goal goal);
    }

    private GoalActionsListener mGoalActionsListener;

    private enum ActionType { EDIT, DELETE };

    public GoalsListAdapter(Context context, List<Goal> objects) {
        super(context, 0, objects);
    }

    public void setGoalActionsListener(GoalActionsListener listener) {
        mGoalActionsListener = listener;
    }

    static class ViewHolder {
        TextView tvName;
        TextView tvProgress;
        GoalProgressBarView progressBar;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        GoalListViewRow row = null;
        ViewHolder holder;
        final Goal goal = getItem(position);

        if (convertView != null) {
            row = (GoalListViewRow) convertView;
        }

        if (row == null) {
            row = GoalListViewRow.inflate(parent);
            row.setActionTypes(ActionType.values());

            holder = new ViewHolder();
            holder.tvName = (TextView) row.findViewById(R.id.goal_name);
            holder.tvProgress = (TextView) row.findViewById(R.id.goal_progress);
            holder.progressBar = (GoalProgressBarView) row.findViewById(R.id.progress_bar);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        row.setActionTriggeredListener(new GoalListViewRow.ActionTriggeredListener() {
            @Override
            public void onActionTriggered(Enum actionType) {
                Log.d("TRIGGER", "Action type: " + actionType.name());
                Log.d("TRIGGER", "Goal: " + goal.getName());
                if (mGoalActionsListener == null) return;
                switch ((ActionType) actionType) {
                    case EDIT:
                        mGoalActionsListener.editGoal(goal);
                        return;
                    case DELETE:
                        mGoalActionsListener.deleteGoal(goal);
                        return;
                }
            }
        });

        holder.tvName.setText(goal.getName());
        holder.tvProgress.setText(formatProgressString(goal));
        holder.progressBar.setProgress(goal.getProgressPercentage());

        return row;
    }

    private static String formatProgressString(Goal goal) {
        return String.format("$%d of $%d", goal.getBalance(), goal.getTarget());
    }

}
