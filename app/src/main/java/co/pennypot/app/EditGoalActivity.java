package co.pennypot.app;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.pennypot.app.models.Goal;


public class EditGoalActivity extends Activity {

    private static final int MAX_DEPOSIT_AMOUNT = 1000;

    private static final int DEPOSIT_INCREMENT = 50;

    private RelativeLayout mBackgroundContainer;

    private TextView mTvGoalBalance;

    private Goal mGoal;

    private int mDepositAmount;

    private GestureDetector mGestureDetector;

    private int mBackgroundColour;

    private float[] mGradientDarkHSV = new float[3];
    private float[] mGradientLightHSV = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_goal);
        ViewConfiguration.get(this).getScaledMinimumFlingVelocity();

        initGoal();
        initUI();

        setDepositAmount(0);
        setBackgroundForAmount();
    }

    private void initGoal() {
    }

    private void initUI() {
        mBackgroundContainer = (RelativeLayout) findViewById(R.id.container);
        mTvGoalBalance = (TextView) findViewById(R.id.goal_balance);

        mGestureDetector = new GestureDetector(this, new ScrollGestureListener());
        mBackgroundContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });

        int gradientDark = getResources().getColor(R.color.edit_goal_gradient_dark);
        int gradientLight = getResources().getColor(R.color.edit_goal_gradient_light);
        Color.colorToHSV(gradientDark, mGradientDarkHSV);
        Color.colorToHSV(gradientLight, mGradientLightHSV);
    }

    private void setDepositAmount(int depositAmount) {
        mDepositAmount = depositAmount;
        if (mDepositAmount < 0) { mDepositAmount = 0; }
        else if (mDepositAmount > MAX_DEPOSIT_AMOUNT) { mDepositAmount = MAX_DEPOSIT_AMOUNT; }

        if (mTvGoalBalance != null) {
            mTvGoalBalance.setText(String.valueOf(mDepositAmount));
        }
    }

    private static final float STEP_OFFSET = 0.25f;

    private void setBackgroundForAmount() {
        float[] hsvTop = new float[3];
        float[] hsvBottom = new float[3];

        float step = mDepositAmount / (1.0f * MAX_DEPOSIT_AMOUNT);
        float stepTop = Math.min(step + STEP_OFFSET, 1.0f);
        float stepBottom = Math.max(step - STEP_OFFSET, 0.0f);

        for (int i = 0; i < 3; i++) {
            hsvTop[i] = mGradientDarkHSV[i] * (1f - stepTop) + mGradientLightHSV[i] * stepTop;
            hsvBottom[i] =  mGradientDarkHSV[i] * (1f - stepBottom) + mGradientLightHSV[i] * stepBottom;
        }

        int[] colours = new int[] { Color.HSVToColor(hsvTop), Color.HSVToColor(hsvBottom) };
        GradientDrawable backgroundGradient = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colours);
        mBackgroundContainer.setBackground(backgroundGradient);
    }

    private class ScrollGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SCROLL_THRESHOLD = 50;

        private float mDistanceY;

        @Override
        public boolean onDown(MotionEvent e) {
            mDistanceY = 0;
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mDistanceY += distanceY;
            if (Math.abs(mDistanceY) > SCROLL_THRESHOLD) {
                int adjustment = (mDistanceY > 0) ? DEPOSIT_INCREMENT : -DEPOSIT_INCREMENT;
                setBackgroundForAmount();
                setDepositAmount(mDepositAmount + adjustment);
                mDistanceY = 0;
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

}
