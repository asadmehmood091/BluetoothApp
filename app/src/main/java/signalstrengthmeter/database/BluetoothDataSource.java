package signalstrengthmeter.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import signalstrengthmeter.DataModel;

public class BluetoothDataSource {
    private static final String TAG = "mytag";
    private Context mContext;
    private MyDBOpenHelper myDBOpenHelper;
    private SQLiteDatabase sqLiteDatabase;

    public BluetoothDataSource(Context mContext) {
        this.mContext = mContext;
        this.myDBOpenHelper=new MyDBOpenHelper(mContext);
        sqLiteDatabase=myDBOpenHelper.getWritableDatabase();

    }
//    public void open(){
//        sqLiteDatabase=myDBOpenHelper.getWritableDatabase();
//
//    }
//
//    public void close(){
//        sqLiteDatabase.close();
//    }

    public void insertItem(DataModel item){
        ContentValues values=item.getValues();

        long insert = sqLiteDatabase.insert(MacAddressTable.TABLE_BLUETOOTH, null, values);
        Log.d(TAG, "insertItem: "+insert);

    }

//    public long getItemsCount(){
//        return DatabaseUtils.queryNumEntries(sqLiteDatabase,MacAddressTable.TABLE_BLUETOOTH);
//    }

//    public List<DataModel> getAllItems(String selection){
//        List<DataModel> dataModels=new ArrayList<>();
//
//        Cursor cursor;
//        if(selection==null){
//
//            cursor = sqLiteDatabase.query(MacAddressTable.TABLE_BLUETOOTH, MacAddressTable.ALL_COLUMNS,
//                    null, null, null, null, MacAddressTable.COLUMN_MAC);
//        }else{
//
//            String[] select={selection};
//            cursor = sqLiteDatabase.query(MacAddressTable.TABLE_BLUETOOTH, MacAddressTable.ALL_COLUMNS,
//                    MacAddressTable.COLUMN_TYPYE+"=?", select, null, null, MacAddressTable.COLUMN_MAC);
//        }
//
//
//        while (cursor.moveToNext()){
//           DataModel item = new DataModel();
//
//            item.setMacId(cursor.getString(cursor.getColumnIndex(MacAddressTable.COLUMN_ID)));
//            item.setMac(cursor.getString(cursor.getColumnIndex(MacAddressTable.COLUMN_MAC)));
//            item.setType(cursor.getString(cursor.getColumnIndex(MacAddressTable.COLUMN_TYPYE)));
//
//            dataModels.add(item);
//
//        }
//
//        cursor.close();
//
//        return dataModels;
//
//    }
//
//    public void seedDatabase(){
//
//        List<DataModel> dataItems= SampleDataProvider.dataItemList;
//
//
//        for (DataModel item:dataItems){
//            insertItem(item);
//        }
//
//    }

//    public void updateName(String name){
//
//        ContentValues values=new ContentValues(1);
//        values.put(MacAddressTable.COLUMN_MAC,name);
//
//        String where=MacAddressTable.COLUMN_MAC + " LIKE ?";
//
//        String whereArgs[] ={"home"};
//
//        int update = sqLiteDatabase.update(MacAddressTable.TABLE_BLUETOOTH, values, where, whereArgs);
//
//        Log.d(TAG, "updateMac: "+update);
//    }

}

