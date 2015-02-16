package co.pennypot.app;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class SwipeListViewRow extends FrameLayout {

    private ViewGroup mBackgroundView;

    private ViewGroup mForegroundView;

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

        Log.d("BACKGROUND VIEW", mBackgroundView.toString());
        Log.d("FOREGROUND VIEW", mForegroundView.toString());
    }

    public static SwipeListViewRow inflate(ViewGroup parent) {
        //TODO: genericize
        SwipeListViewRow row = (SwipeListViewRow) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_goal, parent, false);
        return row;
    }

    public void setForegroundTranslationX(float translationX) {
        Log.d("SWIPELISTROW", "setForegroundTranslationX: " + translationX);
        mForegroundView.setTranslationX(translationX);
    }

    public void resetForegroundTranslation() {
        mForegroundView.animate()
                .translationX(0)
                .setDuration(200)
                .setListener(null);
    }
}
