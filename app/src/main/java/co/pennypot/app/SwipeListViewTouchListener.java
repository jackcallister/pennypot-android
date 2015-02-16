package co.pennypot.app;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

public class SwipeListViewTouchListener implements View.OnTouchListener {

    private ListView mListView;

    private float mDownX;
    private boolean mPaused;
    private SwipeListViewRow mDownView;

    public SwipeListViewTouchListener(ListView listView) {
        mListView = listView;
    }

    @Override
    public boolean onTouch(View row, MotionEvent event) {
        Log.d("SwipeListViewTouchListener", "On touch");
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
                SwipeListViewRow child;
                for (int i = 0; i < childCount; i++) {
                    child = (SwipeListViewRow) mListView.getChildAt(i);
                    Log.d("CHILD", child.toString());
                    child.getHitRect(rect);
                    if (rect.contains(x, y)) {
                        Log.d("MDOWNVIEW", child.toString());
                        mDownView = child;
                        break;
                    }
                }

                if (mDownView != null) {
                    mDownX = eventRawX;
                }

                return false;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mDownView.resetForegroundTranslation();
                mDownView = null;
                mListView.requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("MOVE", ""+eventRawX);
                if (mDownView != null) {
                    Log.d("MOVE", mDownView.toString());
                    mListView.requestDisallowInterceptTouchEvent(true);
                    float deltaX = eventRawX - mDownX;
                    mDownView.setForegroundTranslationX(deltaX);
                    return true;
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
