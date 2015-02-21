package co.pennypot.app;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class GoalListViewRow extends FrameLayout {

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

    private int mAnimationDuration = 200;

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

    //TODO: success action animation
    // translate foreground to opposite end
    public void onSwipeReleased() {
        if (mLastSwipePosition < mFirstActionThreshold) {
            resetForegroundTranslation();
            return;
        }

        if (mActionTriggeredListener != null) {
            if (mLastSwipePosition < mSecondActionThreshold) {
                mActionTriggeredListener.onActionTriggered(mActionTypes[0]);
            } else {
                mActionTriggeredListener.onActionTriggered(mActionTypes[1]);
            }
        }

        resetForegroundTranslation();
    }

    public void setSwipePosition(float swipePosition) {
        // Ignore any right-to-left swipes
        if (swipePosition < 0) {
            return;
        }

        setForegroundTranslation(swipePosition);
        setAction(swipePosition);
        setActionIconTranslation(swipePosition);
        mLastSwipePosition = swipePosition;
    }

    private void setForegroundTranslation(float translationX) {
        mForegroundView.setTranslationX(translationX);
    }

    public void resetForegroundTranslation() {
        mBackgroundView.setBackgroundColor(mBackgroundColourInactive);
        mIvActionIcon.animate()
                .translationX(0)
                .setDuration(mAnimationDuration)
                .setListener(null);
        mForegroundView.animate()
                .translationX(0)
                .setDuration(mAnimationDuration)
                .setListener(null);
    }

    private void setAction(float translationX) {
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

}
