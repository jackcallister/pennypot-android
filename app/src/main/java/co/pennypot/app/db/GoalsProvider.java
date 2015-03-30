package co.pennypot.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.pennypot.app.models.Goal;

public class GoalsProvider {

    private SQLiteDatabase mDatabase;

    private PennypotDatabaseHelper mDbHelper;

    public GoalsProvider(Context context) {
        mDbHelper = new PennypotDatabaseHelper(context);
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public List<Goal> all() {
        List<Goal> goals = new ArrayList<Goal>();

        Cursor cursor = mDatabase.query(GoalsTable.TABLE_NAME, GoalsTable.ALL_COLUMNS,
                null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            goals.add(toGoal(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return goals;
    }

    public boolean save(Goal goal) {
        if (isValid(goal)) {
            ContentValues values = new ContentValues();
            values.put(GoalsTable.KEY_NAME, goal.getName());
            values.put(GoalsTable.KEY_BALANCE, goal.getBalance());
            values.put(GoalsTable.KEY_TARGET, goal.getTarget());

            if (goal.getId() > 0) {
                int result = mDatabase.update(GoalsTable.TABLE_NAME, values, "id = ?", new String[] { String.valueOf(goal.getId()) });
                return result == 1;
            } else {
                int id = (int) mDatabase.insert(GoalsTable.TABLE_NAME, null, values);
                if (id > 0) {
                    goal.setId(id);
                    return true;
                }
            }
        }

        return false;
    }

    public boolean delete(Goal goal) {
        if (goal.getId() <= 0) { return false; }
        int rowsDeleted = mDatabase.delete(GoalsTable.TABLE_NAME,
                "id=?", new String[] { String.valueOf(goal.getId()) });
        return rowsDeleted == 1;
    }

    private boolean isValid(Goal goal) {
        if (goal.getName() == null || goal.getName().equals("")) return false;
        return true;
    }

    private Goal toGoal(Cursor cursor) {
        Goal goal = new Goal();
        goal.setId(cursor.getInt(0));
        goal.setName(cursor.getString(1));
        goal.setBalance(cursor.getInt(2));
        goal.setTarget(cursor.getInt(3));
        return goal;
    }

}
