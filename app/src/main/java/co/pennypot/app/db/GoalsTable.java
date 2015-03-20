package co.pennypot.app.db;

import android.database.sqlite.SQLiteDatabase;

public class GoalsTable {
    public static final String TABLE_NAME= "goals";

    public static final String KEY_ID = "id";

    public static final String KEY_NAME = "name";

    public static final String KEY_BALANCE = "balance";

    public static final String KEY_TARGET = "target";

    public static final String[] ALL_COLUMNS = {
        KEY_ID, KEY_NAME, KEY_BALANCE, KEY_TARGET
    };

    public static void onCreate(SQLiteDatabase database) {
        String createGoalsTable = "CREATE TABLE " + TABLE_NAME + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_BALANCE + " INTEGER," + KEY_TARGET + " INTEGER" + ")";
        database.execSQL(createGoalsTable);
    }

    public static void onUpgrade(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + GoalsTable.TABLE_NAME);
    }
}
