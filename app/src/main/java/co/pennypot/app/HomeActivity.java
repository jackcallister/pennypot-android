package co.pennypot.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import co.pennypot.app.models.Goal;


public class HomeActivity extends Activity {

    private ListView mGoalsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initGoalsList();
    }

    private void initGoalsList() {
        //TODO: remove static data
        ArrayList<Goal> goalsList = new ArrayList<Goal>();
        goalsList.add(new Goal("San Fran", 200, 2500));
        goalsList.add(new Goal("Macbook Pro", 150, 3200));
        goalsList.add(new Goal("New Sofa", 80, 1600));

        mGoalsList = (ListView) findViewById(R.id.goals_list);
        mGoalsList.setAdapter(new GoalsListAdapter(this, goalsList));
    }

}
