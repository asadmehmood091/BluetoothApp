package signalstrengthmeter.database;

public class MacAddressTable {
    public static final String TABLE_BLUETOOTH = "macAddress";
    public static final String COLUMN_ID = "macId";
    public static final String COLUMN_MAC = "mac";
    public static final String COLUMN_TYPYE = "type";


    public static final String[] ALL_COLUMNS=
            new String[]{COLUMN_ID,COLUMN_MAC,COLUMN_TYPYE};

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_BLUETOOTH + "(" +
                    COLUMN_ID + " TEXT PRIMARY KEY," +
                    COLUMN_MAC + " TEXT ," +
                    COLUMN_TYPYE + " TEXT);";

    public static final String SQL_DELETE=
            "DROP TABLE IF EXISTS "+TABLE_BLUETOOTH;

}
