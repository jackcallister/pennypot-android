package co.pennypot.app;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.List;

import co.pennypot.app.db.GoalsProvider;
import co.pennypot.app.models.Goal;


public class HomeActivity extends Activity implements GoalsListAdapter.GoalActionsListener {

    private GoalsProvider mGoalsProvider;

    private ListView mGoalsList;

    private List<Goal> mGoals;

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

        mGoalsProvider = new GoalsProvider(this);

        initUI();
        initGoalsList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoalsProvider.close();
    }

    private void initUI() {
        mRootLayout = (RelativeLayout) findViewById(R.id.container);
        mBtnNewGoal = (ImageButton) findViewById(R.id.btn_new_goal);
        mNewGoalFormBlackout = (View) findViewById(R.id.new_goal_form_blackout);
    }

    private void initGoalsList() {
        try {
            mGoalsProvider.open();
            mGoals = mGoalsProvider.all();

            mGoalsList = (ListView) findViewById(R.id.goals_list);
            mGoalsListAdapter = new GoalsListAdapter(this, mGoals);
            mGoalsListAdapter.setGoalActionsListener(this);
            mGoalsList.setAdapter(mGoalsListAdapter);
            SwipeListViewTouchListener touchListener = new SwipeListViewTouchListener(mGoalsList);

            mGoalsList.setOnTouchListener(touchListener);
            mGoalsList.setOnScrollListener(touchListener.makeScrollListener());
        } catch(SQLException e) {
            Log.e("HomeActivity", "Failed to open goals database");
        }
    }

    public void onNewGoalPressed(View view) {
        if (!mNewGoalFormVisible) {
            showNewGoalForm();
        } else {
            hideNewGoalForm();
        }
    }

    //TODO: move validations to NewGoalForm
    public void onNewGoalFormAddPressed(View view) {
        EditText etGoalName = ((EditText) findViewById(R.id.new_goal_form_name));
        EditText etGoalTarget = ((EditText) findViewById(R.id.new_goal_form_target));
        String goalName = etGoalName.getText().toString();
        String goalTargetStr = etGoalTarget.getText().toString();

        boolean isValid = true;
        if (goalName == null || goalName.equals("")) {
            etGoalName.setError(getResources().getString(R.string.new_goal_name_error));
            isValid = false;
        }

        int goalTarget = 0;
        try {
            goalTarget = Integer.parseInt(goalTargetStr);
            if (goalTarget <= 0) {
                etGoalName.setError(getResources().getString(R.string.new_goal_name_error));
                isValid = false;
            }
        } catch (NumberFormatException e) {
            etGoalName.setError(getResources().getString(R.string.new_goal_name_error));
        }

        Goal newGoal = new Goal(goalName, 0, goalTarget);

        if (mGoalsProvider.save(newGoal)) {
            mGoals.add(newGoal);
            mGoalsListAdapter.notifyDataSetChanged();
            hideNewGoalForm();
        } else {
            Toast.makeText(this,
                    getResources().getString(R.string.new_goal_save_error),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    public void deleteGoal(Goal goal) {
        if (mGoalsProvider.delete(goal)) {
            mGoals.remove(goal);
            mGoalsListAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this,
                    getResources().getString(R.string.goal_delete_error),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    public void editGoal(Goal goal) {
        Intent intent = new Intent(this, EditGoalActivity.class);
        startActivity(intent);
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
        hideKeyboard();
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

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
