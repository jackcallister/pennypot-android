package co.pennypot.app;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class NewGoalFormView extends LinearLayout {

    public NewGoalFormView(Context context) {
        super(context);
        init();
    }

    public NewGoalFormView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NewGoalFormView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.view_new_goal_form, this);
    }
}
