package co.pennypot.app;

import android.animation.Animator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class GoalListViewRow extends FrameLayout {

    //TODO: move to resources
    private int mAnimationDuration = 200;
    private int mActionTriggerAnimationDuration = 250;

    public interface ActionTriggeredListener {
        public void onActionTriggered(Enum actionType);
    }

    private ActionTriggeredListener mActionTriggeredListener;

    private ViewGroup mBackgroundView;

    private ViewGroup mForegroundView;

    private ImageView mIvActionIcon;

    private int mBackgroundColourInactive;

    private int[] mBackgroundColours;

    private Drawable[] mActionIcons;


    private int mFirstActionThreshold;

    private int mSecondActionThreshold;

    private Enum[] mActionTypes;

    private float mLastSwipePosition;

    public GoalListViewRow(Context context) {
        super(context);
        init();
    }

    public GoalListViewRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GoalListViewRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        mBackgroundColourInactive = getResources().getColor(R.color.goal_list_row_background_inactive);
        mBackgroundColours = new int[] {
                getResources().getColor(R.color.pp_purple),
                getResources().getColor(R.color.pp_red)
        };

        mActionIcons = new Drawable[] {
                getResources().getDrawable(R.drawable.ic_coin),
                getResources().getDrawable(R.drawable.ic_trash)
        };
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mBackgroundView = (ViewGroup) findViewById(R.id.row_background);
        mForegroundView = (ViewGroup) findViewById(R.id.row_foreground);
        mIvActionIcon = (ImageView) findViewById(R.id.iv_background_action);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mFirstActionThreshold = mIvActionIcon.getRight();
        mSecondActionThreshold = (int) Math.floor(getWidth() * 0.66);
    }

    public void setActionTriggeredListener(ActionTriggeredListener listener) {
        mActionTriggeredListener = listener;
    }

    public static GoalListViewRow inflate(ViewGroup parent) {
        GoalListViewRow row = (GoalListViewRow) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_goal, parent, false);
        return row;
    }

    public void setActionTypes(Enum[] actionTypes) {
        this.mActionTypes = actionTypes;
    }

    public void onSwipeReleased() {
        if (mLastSwipePosition < mFirstActionThreshold) {
            resetForegroundTranslation(true);
            return;
        }
        triggerActionWithAnimation();
    }

    public void setSwipePosition(float swipePosition) {
        // Ignore any right-to-left swipes
        if (swipePosition < 0) {
            return;
        }

        setForegroundTranslation(swipePosition);
        setActionForPosition(swipePosition);
        setActionIconTranslation(swipePosition);
        mLastSwipePosition = swipePosition;
    }

    private void setForegroundTranslation(float translationX) {
        mForegroundView.setTranslationX(translationX);
    }

    public void resetForegroundTranslation(boolean animated) {
        if (mBackgroundView == null) return;
        mBackgroundView.setBackgroundColor(mBackgroundColourInactive);

        if (animated) {
            mIvActionIcon.animate()
                    .translationX(0)
                    .setDuration(mAnimationDuration)
                    .setListener(null);
            mForegroundView.animate()
                    .translationX(0)
                    .setDuration(mAnimationDuration)
                    .setListener(null);
        } else {
            mIvActionIcon.setTranslationX(0);
            mForegroundView.setTranslationX(0);
        }
    }

    private void setActionForPosition(float translationX) {
        if (translationX < mFirstActionThreshold) {
            mBackgroundView.setBackgroundColor(mBackgroundColourInactive);
            mIvActionIcon.setImageDrawable(mActionIcons[0]);
        } else if (translationX < mSecondActionThreshold) {
            mBackgroundView.setBackgroundColor(mBackgroundColours[0]);
            mIvActionIcon.setImageDrawable(mActionIcons[0]);
        } else {
            mBackgroundView.setBackgroundColor(mBackgroundColours[1]);
            mIvActionIcon.setImageDrawable(mActionIcons[1]);
        }
    }

    private void setActionIconTranslation(float translationX) {
        if (translationX > mIvActionIcon.getRight()) {
            float deltaX = translationX - mIvActionIcon.getRight();
            mIvActionIcon.setTranslationX(deltaX);
        }
    }

    private void triggerActionWithAnimation() {
        mIvActionIcon.animate()
                .translationX(getWidth())
                .setDuration(mActionTriggerAnimationDuration);
        mForegroundView.animate()
                .translationX(getWidth())
                .setDuration(mActionTriggerAnimationDuration)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                         if (mActionTriggeredListener != null) {
                             if (mLastSwipePosition < mSecondActionThreshold) {
                                 mActionTriggeredListener.onActionTriggered(mActionTypes[0]); }
                             else {
                                 mActionTriggeredListener.onActionTriggered(mActionTypes[1]);
                             }
                         }
                    }

                    public void onAnimationStart(Animator animation) { }
                    public void onAnimationCancel(Animator animation) { }
                    public void onAnimationRepeat(Animator animation) { }
                });
    }

}
