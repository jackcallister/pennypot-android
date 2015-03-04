package co.pennypot.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PennypotDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "pennypot.db";

    public PennypotDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createGoalsTable = "CREATE_TABLE " + GoalsTable.TABLE_NAME + "(" +
                GoalsTable.KEY_ID + " INTEGER PRIMARY KEY," + GoalsTable.KEY_NAME + " TEXT,"
                + GoalsTable.KEY_BALANCE + " INTEGER," + GoalsTable.KEY_TARGET + " INTEGER" + ")";
        db.execSQL(createGoalsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 1) {
            db.execSQL("DROP TABLE IF EXISTS " + GoalsTable.TABLE_NAME);
            onCreate(db);
        }
    }
}
