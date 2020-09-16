package signalstrengthmeter.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class MyDBOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="bluetooth.db";
    public static final int DATABASE_VERSION=1;
    private static final String TAG = "mytag";

    public MyDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MacAddressTable.SQL_CREATE);
        Log.d(TAG, "onCreate: Database created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MacAddressTable.SQL_DELETE);
        Log.d(TAG, "onUpgrade: Database updated...");
        onCreate(db);

    }
}