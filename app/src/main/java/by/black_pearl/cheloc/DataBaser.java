package by.black_pearl.cheloc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Worker with DB.
 */
public class DataBaser extends SQLiteOpenHelper {
    private Context context;
    private final static String NAME_DB = "Chelocbase";
    private final static String LOG_TAG = "DataBaser";
    public final static String TABLE_NAME = "myLocations";
    public final static String TABLE_METRO = "metroLocations";
    private final static int VERSION_DB = 1;
    public final static String ID_COLUMN = "id";
    public final static String ADDRESS_COLUMN = "address";
    public final static String LATITUDE_COLUMN = "latitude";
    public final static String LONGTITUDE_COLUMN = "longtitude";
    public final static String ALTITUDE_COLUMN = "altitude";

    public DataBaser(Context context) {
        super(context, NAME_DB, null, VERSION_DB);
        this.context = context;
        printAllRecordsDbToLog();
        Log.i(LOG_TAG, "DataBaser");
    }

    public void insertNewAddress(String address, String latitude, String longtitude, String altitude) {
        insertNewAddress(this.getWritableDatabase(), TABLE_NAME, address, latitude, longtitude, altitude);
        Toast.makeText(context, "Address has saved.", Toast.LENGTH_SHORT).show();
    }

    private boolean insertNewAddress(SQLiteDatabase db, String table, String address, String latitude,
                                  String longtitude, String altitude) {
        try {
            Log.i(LOG_TAG, "insertNewAddress");
            ContentValues cv = new ContentValues();
            cv.put(ADDRESS_COLUMN, address);
            cv.put(LATITUDE_COLUMN, latitude);
            cv.put(LONGTITUDE_COLUMN, longtitude);
            cv.put(ALTITUDE_COLUMN, altitude);
            db.insert(table, null, cv);
            return true;
        }
        catch (Exception e) {
            Toast.makeText(context, "Not saved. Unknown error.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void deleteAddress(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID_COLUMN + "=" + id, null);
    }

    private void printAllRecordsDbToLog() {
        Log.i(LOG_TAG, "printAllRecordsDbToLog");
        SQLiteDatabase db = this.getWritableDatabase();
        cursorPrinter(db.query(TABLE_METRO, null, null, null, null, null, null), TABLE_METRO);
        cursorPrinter(db.query(TABLE_NAME, null, null, null, null, null, null), TABLE_NAME);
    }

    private void cursorPrinter(Cursor cursor, String table) {
        Log.i(LOG_TAG, "cursorPrinter");
        Log.i(LOG_TAG, "\nReading of db " + table + "...");
        if(cursor.moveToFirst()) {
            do {
                Log.i(LOG_TAG,
                        "\nid = " + cursor.getInt(cursor.getColumnIndex(DataBaser.ID_COLUMN)) +
                        "\naddress = " + cursor.getString(cursor.getColumnIndex(DataBaser.ADDRESS_COLUMN)) +
                        "\nlatitude = " + cursor.getString(cursor.getColumnIndex(DataBaser.LATITUDE_COLUMN)) +
                        "\nlongtitude = " + cursor.getString(cursor.getColumnIndex(DataBaser.LONGTITUDE_COLUMN)) +
                        "\naltitude = " + cursor.getString(cursor.getColumnIndex(DataBaser.ALTITUDE_COLUMN)));
            }
            while(cursor.moveToNext());
        }
        else {
            Log.i(LOG_TAG, "Database is null ;(");
        }
        cursor.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (" +
                ID_COLUMN +" integer primary key autoincrement, " +
                ADDRESS_COLUMN + " text, " +
                LATITUDE_COLUMN + " text, " +
                LONGTITUDE_COLUMN + " text, " +
                ALTITUDE_COLUMN + " text);");
        db.execSQL("create table " + TABLE_METRO + " (" +
                ID_COLUMN +" integer primary key autoincrement, " +
                ADDRESS_COLUMN + " text, " +
                LATITUDE_COLUMN + " text, " +
                LONGTITUDE_COLUMN + " text, " +
                ALTITUDE_COLUMN + " text);");
        Toast.makeText(context, "Была создана новая БД приложения.", Toast.LENGTH_SHORT).show();
        Log.i(LOG_TAG, "insertNewAddress");
        insertNewAddress(db, TABLE_NAME, "Энергосбыт", "53.923269", "27.596573", "203");
        String[] metroAddressArray = context.getResources().getStringArray(R.array.metroAddresses);
        String[] metroLatitudeArray = context.getResources().getStringArray(R.array.metroLatitude);
        String[] metroLongtitude = context.getResources().getStringArray(R.array.metroLongtitude);
        String metroAltitude = context.getResources().getString(R.string.metroAltitude);
        int length = metroAddressArray.length;
        for(int i = 0; i < length; i++) {
            if(!insertNewAddress(db, TABLE_METRO, metroAddressArray[i], metroLatitudeArray[i],
                    metroLongtitude[i], metroAltitude)) {
                Toast.makeText(context, "Создать корректно БД не удалось!", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.i(LOG_TAG, "onOpen");
    }
}
