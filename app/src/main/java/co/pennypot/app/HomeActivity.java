package co.pennypot.app;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import co.pennypot.app.models.Goal;


public class HomeActivity extends Activity implements GoalsListAdapter.GoalActionsListener {

    private ListView mGoalsList;

    private ArrayList<Goal> mGoals;

    private GoalsListAdapter mGoalsListAdapter;

    private boolean mNewGoalFormVisible;

    private RelativeLayout mRootLayout;

    private View mNewGoalForm;

    private View mNewGoalFormBlackout;

    private ImageButton mBtnNewGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initUI();
        initGoalsList();
    }

    private void initUI() {
        mRootLayout = (RelativeLayout) findViewById(R.id.container);
        mBtnNewGoal = (ImageButton) findViewById(R.id.btn_new_goal);
        mNewGoalFormBlackout = (View) findViewById(R.id.new_goal_form_blackout);
    }

    private void initGoalsList() {
        //TODO: remove static data
        mGoals = new ArrayList<Goal>();
        mGoals.add(new Goal("San Fran", 2250, 2500));
        mGoals.add(new Goal("Macbook Pro", 450, 3200));
        mGoals.add(new Goal("New Sofa", 900, 1600));

        mGoalsList = (ListView) findViewById(R.id.goals_list);
        mGoalsListAdapter = new GoalsListAdapter(this, mGoals);
        mGoalsListAdapter.setGoalActionsListener(this);
        mGoalsList.setAdapter(mGoalsListAdapter);
        SwipeListViewTouchListener touchListener = new SwipeListViewTouchListener(mGoalsList);

        mGoalsList.setOnTouchListener(touchListener);
        mGoalsList.setOnScrollListener(touchListener.makeScrollListener());
    }

    public void onNewGoalPressed(View view) {
        if (!mNewGoalFormVisible) {
            showNewGoalForm();
        } else {
            hideNewGoalForm();
        }
    }

    public void onNewGoalFormAddPressed(View view) {
        String goalName = ((EditText) findViewById(R.id.new_goal_form_name)).getText().toString();
        String goalTargetStr = ((EditText) findViewById(R.id.new_goal_form_target)).getText().toString();

        try {
            int goalTarget = Integer.parseInt(goalTargetStr);
            Goal newGoal = new Goal(goalName, 0, goalTarget);

            mGoals.add(newGoal);
            mGoalsListAdapter.notifyDataSetChanged();
            hideNewGoalForm();
        } catch (NumberFormatException e) {
            Toast.makeText(this,
                    getResources().getString(R.string.new_goal_save_error),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    public void deleteGoal(Goal goal) {
        mGoals.remove(goal);
        mGoalsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void editGoal(Goal goal) {
        //TODO
    }

    private void showNewGoalForm() {
        ObjectAnimator.ofFloat(mBtnNewGoal, "rotation", 0.0f, -45.0f).start();
        ObjectAnimator.ofFloat(mNewGoalFormBlackout, "alpha", 0.0f, 0.75f).start();

        mNewGoalForm = new NewGoalFormView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ABOVE, R.id.goal_list_container);
        mNewGoalForm.setLayoutParams(params);
        mRootLayout.addView(mNewGoalForm, 1);

        mNewGoalForm.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mNewGoalForm.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                float height = mNewGoalForm.getHeight();
                ObjectAnimator.ofFloat(mNewGoalForm, "translationY", 0.0f, height).start();
            }
        });

        mNewGoalFormVisible = true;
    }

    private void hideNewGoalForm() {
        ObjectAnimator.ofFloat(mBtnNewGoal, "rotation", -45.0f, 0.0f).start();
        ObjectAnimator.ofFloat(mNewGoalFormBlackout, "alpha", 0.75f, 0).start();

        float height = mNewGoalForm.getHeight();
        ObjectAnimator anim = ObjectAnimator.ofFloat(mNewGoalForm, "translationY", height, 0.0f);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRootLayout.removeView(mNewGoalForm);
                mNewGoalFormVisible = false;
            }

            public void onAnimationStart(Animator animation) { }
            public void onAnimationCancel(Animator animation) { }
            public void onAnimationRepeat(Animator animation) { }
        });
        anim.start();
    }

}
