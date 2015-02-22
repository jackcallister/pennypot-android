package co.pennypot.app;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import co.pennypot.app.models.Goal;


public class HomeActivity extends Activity {

    private ListView mGoalsList;

    private boolean mNewGoalFormVisible;

    private RelativeLayout mRootLayout;

    private View mNewGoalForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mRootLayout = (RelativeLayout) findViewById(R.id.container);
        initGoalsList();
    }

    private void initGoalsList() {
        //TODO: remove static data
        ArrayList<Goal> goalsList = new ArrayList<Goal>();
        goalsList.add(new Goal("San Fran", 2250, 2500));
        goalsList.add(new Goal("Macbook Pro", 450, 3200));
        goalsList.add(new Goal("New Sofa", 900, 1600));

        mGoalsList = (ListView) findViewById(R.id.goals_list);
        mGoalsList.setAdapter(new GoalsListAdapter(this, goalsList));
        SwipeListViewTouchListener touchListener = new SwipeListViewTouchListener(mGoalsList);

        mGoalsList.setOnTouchListener(touchListener);
        mGoalsList.setOnScrollListener(touchListener.makeScrollListener());
    }

    public void onNewGoalPressed(View view) {
        if (!mNewGoalFormVisible) {
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
        } else {
            float height = mNewGoalForm.getHeight();
            ObjectAnimator anim = ObjectAnimator.ofFloat(mNewGoalForm, "translationY", height, 0.0f);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRootLayout.removeView(mNewGoalForm);
                }

                public void onAnimationStart(Animator animation) { }
                public void onAnimationCancel(Animator animation) { }
                public void onAnimationRepeat(Animator animation) { }
            });
            anim.start();
        }

        mNewGoalFormVisible = !mNewGoalFormVisible;
    }

}
