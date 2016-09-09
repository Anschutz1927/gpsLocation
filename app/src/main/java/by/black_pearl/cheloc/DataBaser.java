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

<<<<<<< HEAD
    private void insertFirstLocation(SQLiteDatabase db) {
        Log.i(LOG_TAG, "insertFirstLocation");
        inserNewAddress("Энергосбыт", "53.923269","27.596573", "203");
    }

=======
>>>>>>> da794f8... 2 (interface work with bd)
    public void inserNewAddress(String address, String latitude,
                                String longtitude, String altitude) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Log.i(LOG_TAG, "inserNewAddress");
            ContentValues cv = new ContentValues();
            cv.put(ADDRESS_COLUMN, address);
            cv.put(LATITUDE_COLUMN, latitude);
            cv.put(LONGTITUDE_COLUMN, longtitude);
            cv.put(ALTITUDE_COLUMN, altitude);
            db.insert(TABLE_NAME, null, cv);
            db.close();
            Toast.makeText(context, "Address has saved.", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Toast.makeText(context, "Not saved. Unknown error.", Toast.LENGTH_SHORT).show();
        }
    }

    private void printAllRecordsDbToLog() {
        Log.i(LOG_TAG, "printAllRecordsDbToLog");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(DataBaser.TABLE_NAME, null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DataBaser.ID_COLUMN);
            int addressIndex = cursor.getColumnIndex(DataBaser.ADDRESS_COLUMN);
            int latitudeIndex = cursor.getColumnIndex(DataBaser.LATITUDE_COLUMN);
            int longtitudeIndex = cursor.getColumnIndex(DataBaser.LONGTITUDE_COLUMN);
            int altitudeIndex = cursor.getColumnIndex(DataBaser.ALTITUDE_COLUMN);
            do {
                Log.i(LOG_TAG, "Reading of db...\n" +
                        "\nid = " + cursor.getInt(idIndex) +
                        " " +String.valueOf(idIndex) +
                        "\naddress = " + cursor.getString(addressIndex) +
                        " " + String.valueOf(addressIndex) +
                        "\nlatitude = " + cursor.getString(latitudeIndex) +
                        " " + String.valueOf(latitudeIndex) +
                        "\nlongtitude = " + cursor.getString(longtitudeIndex) +
                        " " + String.valueOf(longtitudeIndex) +
                        "\naltitude = " + cursor.getString(altitudeIndex) +
                        " " + String.valueOf(altitudeIndex));
            }
            while(cursor.moveToNext());
        }
        else {
            Log.i(LOG_TAG, "database is null ;(");
        }
        cursor.close();
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (" +
                ID_COLUMN +" integer primary key autoincrement, " +
                ADDRESS_COLUMN + " text, " +
                LATITUDE_COLUMN + " text, " +
                LONGTITUDE_COLUMN + " text, " +
                ALTITUDE_COLUMN + " text);");
        try {
            Log.i(LOG_TAG, "inserNewAddress");
            ContentValues cv = new ContentValues();
            cv.put(ADDRESS_COLUMN, "Энергосбыт");
            cv.put(LATITUDE_COLUMN, "53.923269");
            cv.put(LONGTITUDE_COLUMN, "27.596573");
            cv.put(ALTITUDE_COLUMN, "203");
            db.insert(TABLE_NAME, null, cv);
            db.close();
            Toast.makeText(context, "Была создана новая БД приложения.", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Toast.makeText(context, "Создать БД не удалось!", Toast.LENGTH_SHORT).show();
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
