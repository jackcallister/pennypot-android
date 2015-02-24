package co.pennypot.app;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

public class SwipeListViewTouchListener implements View.OnTouchListener {

    private ListView mListView;
    private int mTouchSlop;
    private GoalListViewRow mDownView;
    private float mDownX;
    private boolean mPaused;

    public SwipeListViewTouchListener(ListView listView) {
        ViewConfiguration vc = ViewConfiguration.get(listView.getContext());
        mTouchSlop = vc.getScaledTouchSlop();
        mListView = listView;
    }

    @Override
    public boolean onTouch(View row, MotionEvent event) {
        final float eventRawX = (int) event.getRawX();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (mPaused) {
                    return false;
                }

                Rect rect = new Rect();
                int childCount = mListView.getChildCount();
                int[] listViewCoords = new int[2];
                mListView.getLocationOnScreen(listViewCoords);
                int x = (int) event.getRawX() - listViewCoords[0];
                int y = (int) event.getRawY() - listViewCoords[1];

                GoalListViewRow child;
                for (int i = 0; i < childCount; i++) {
                    child = (GoalListViewRow) mListView.getChildAt(i);
                    child.getHitRect(rect);
                    if (rect.contains(x, y)) {
                        mDownView = child;
                        break;
                    }
                }

                if (mDownView != null) {
                    mDownX = eventRawX;
                }

                return false;
            case MotionEvent.ACTION_CANCEL:
                if (mDownView != null) {
                    mListView.requestDisallowInterceptTouchEvent(false);
                    mDownView.resetForegroundTranslation();
                    mDownView = null;
                    mDownX = 0;
                    break;
                }
            case MotionEvent.ACTION_UP:
                if (mDownView != null) {
                    mListView.requestDisallowInterceptTouchEvent(false);
                    mDownView.onSwipeReleased();
                    mDownView = null;
                    mDownX = 0;
                    break;
                }
            case MotionEvent.ACTION_MOVE:
                if (mPaused) {
                    break;
                }

                if (mDownView != null) {
                    float deltaX = eventRawX - mDownX;

                    if (deltaX > mTouchSlop) {
                        mDownView.setSwipePosition(deltaX);
                        mListView.requestDisallowInterceptTouchEvent(true);
                        return true;
                    }
                }

                break;
        }

        return false;
    }

    public AbsListView.OnScrollListener makeScrollListener() {
        return new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                mPaused = !(scrollState != AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL);
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        };
    }

}
