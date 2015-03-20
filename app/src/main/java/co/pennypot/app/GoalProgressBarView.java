package co.pennypot.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GoalProgressBarView extends View {

    private static final int PROGRESS_ROUND_TO = 10;

    private int mProgress;

    private Paint mProgressPaint;

    public GoalProgressBarView(Context context) {
        super(context);
        init();
    }

    public GoalProgressBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GoalProgressBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
       mProgressPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int wd = canvas.getWidth();
        int ht = canvas.getHeight();

        int progressWd = calculateProgressWidth(wd);
        mProgressPaint.setColor(getResources().getColor(getProgressColorId()));

        canvas.drawRect(0, 0, progressWd, ht, mProgressPaint);
    }

    private int calculateProgressWidth(int width) {
        return (int) Math.round(
                ((mProgress/100.0) * width) / PROGRESS_ROUND_TO
                        * PROGRESS_ROUND_TO);
    }

    private int getProgressColorId() {
        int resId = getResources().getIdentifier("progress_bar_" + roundedProgress(), "color", getContext().getPackageName());
        return (resId != -1 ? resId : R.color.pp_grey_background);
     }

    public int roundedProgress() {
        return (int) (Math.floor(mProgress/PROGRESS_ROUND_TO) * PROGRESS_ROUND_TO);
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int mProgress) {
        this.mProgress = mProgress;
        invalidate();
    }

}
