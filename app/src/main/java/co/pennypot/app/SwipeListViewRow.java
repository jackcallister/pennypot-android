package co.pennypot.app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class SwipeListViewRow extends FrameLayout {

    private ViewGroup mBackgroundView;

    private ViewGroup mForegroundView;

    private ImageView mIvActionIcon;

    public SwipeListViewRow(Context context) {
        super(context);
        setupChildren();
    }

    public SwipeListViewRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupChildren();
    }

    public SwipeListViewRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupChildren();
    }

    public void setupChildren() {
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mBackgroundView = (ViewGroup) findViewById(R.id.row_background);
        mForegroundView = (ViewGroup) findViewById(R.id.row_foreground);
        mIvActionIcon = (ImageView) findViewById(R.id.iv_background_action);
    }

    public static SwipeListViewRow inflate(ViewGroup parent) {
        //TODO: genericize
        SwipeListViewRow row = (SwipeListViewRow) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_goal, parent, false);
        return row;
    }

    public void setForegroundTranslationX(float translationX) {
        if (translationX > mIvActionIcon.getRight()) {
            mBackgroundView.setBackgroundColor(getResources().getColor(R.color.pp_purple));
        }
        mForegroundView.setTranslationX(translationX);
    }

    public void resetForegroundTranslation() {
        mBackgroundView.setBackgroundColor(getResources().getColor(R.color.goal_list_row_background_inactive));
        mForegroundView.animate()
                .translationX(0)
                .setDuration(200)
                .setListener(null);
    }
}
