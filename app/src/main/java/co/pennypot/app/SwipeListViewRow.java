package co.pennypot.app;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class SwipeListViewRow extends FrameLayout {

    private ViewGroup mBackgroundView;

    private ViewGroup mForegroundView;

    private ImageView mIvActionIcon;

    private int mBackgroundColourInactive;

    private int[] mBackgroundColours;

    private Drawable[] mActionIcons;

    private int mAnimationDuration = 200;

    public SwipeListViewRow(Context context) {
        super(context);
        init();
    }

    public SwipeListViewRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipeListViewRow(Context context, AttributeSet attrs, int defStyle) {
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

    public static SwipeListViewRow inflate(ViewGroup parent) {
        SwipeListViewRow row = (SwipeListViewRow) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_goal, parent, false);
        return row;
    }

    public void setForegroundTranslationX(float translationX) {
        // Ignore any right-to-left swipes
        if (translationX < 0) {
            return;
        }

        mForegroundView.setTranslationX(translationX);
        setAction(translationX);
        setActionIconTranslation(translationX);
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
        if (translationX < mIvActionIcon.getRight()) {
            mBackgroundView.setBackgroundColor(mBackgroundColourInactive);
            mIvActionIcon.setImageDrawable(mActionIcons[0]);
        } else if (translationX < 0.66 * getWidth()) {
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
