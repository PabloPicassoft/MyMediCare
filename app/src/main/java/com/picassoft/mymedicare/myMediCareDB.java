package com.picassoft.mymedicare;

/**
 * Created by Paul on 14/04/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;


public class myMediCareDB {
    //variables for all columns in database
    private static final String TAG = "DBHelper";

    private static final String DATABASE_NAME = "MediDatabase";

    private static final String USER_TABLE = "users";
    public static final String COLUMN_ROWID = "_id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_NAME = "name";
    //public static final String COLUMN_GPNAME = "gpname";
    public static final String COLUMN_GP_NUMBER = "gpnumber";
    //public static final String COLUMN_COLOURSCHEME = "colourScheme";
    //public static final String COLUMN_TEXTSIZE = "textSize";

    private static final String MEASUREMENTS_TABLE = "measurements";
    public static final String COLUMN_MEASUREMENTID = "_measureid";
    public static final String COLUMN_FOREIGN_USERID = "_userid";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TEMPERATURE = "name";


    private static final int DATABASE_VERSION = 2;

    //string told database table name and order
    private static final String CREATE_USER_TABLE = "create table "+ USER_TABLE + "(_id integer primary key autoincrement, "
            +"email text not null, password text not null, name, gpnumber);";

    private static final String CREATE_MEASUREMENTS_TABLE = "";
    //variables for holding database context, helper and SQLite instances
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    //method to create instance of database helper
    public myMediCareDB(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        //generate instance of database helper with defined database name and version
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        //first method ran on load of class
        @Override
        public void onCreate(SQLiteDatabase db){

                db.execSQL(CREATE_USER_TABLE);
        }

        //if the database is updated, wipe it to prevent conflicts
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS login");
            onCreate(db);
        }
    }

    //open database
    public myMediCareDB open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //close the database
    public void close() {
        DBHelper.close();
    }

    //add new row and contact to database
    public void insertUser(User user) {

        //populate rows with username and password
        ContentValues initialValues = new ContentValues();

        initialValues.put(COLUMN_EMAIL, user.getEmail());
        initialValues.put(COLUMN_NAME, user.getName());
        initialValues.put(COLUMN_PASSWORD, user.getPassword());
        initialValues.put(COLUMN_GP_NUMBER, user.getGpNumber());

        db.insert(USER_TABLE, null, initialValues);
    }

    public  void deleteByID(int row) {
        long result = db.delete(USER_TABLE, COLUMN_ROWID + " = " + row, null);
        Log.d(TAG, String.valueOf(result));
    }

    public void updateDB(String email, String name, String pass, String gpNumber, int row) {

        ContentValues updatedValues = new ContentValues();

        updatedValues.put(COLUMN_EMAIL, email);
        updatedValues.put(COLUMN_NAME, name);
        updatedValues.put(COLUMN_PASSWORD, pass);
        updatedValues.put(COLUMN_GP_NUMBER, gpNumber);

        db.update(USER_TABLE, updatedValues, COLUMN_ROWID + " = " + row , null);
    }

    //get user details
    public String loginAuth(String username){

        String query = "select _id, email, password from " + USER_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        String a,b;
        b="NOT FOUND!";

        int positionCount = 0;
      //
        if (cursor != null) {

            cursor.moveToFirst();

            do {
                //check if any email has been registered
//                if (cursor.getString(0).equals(null)){
//                    Toast warning = Toast.makeText(this.context, "Please register an email.", Toast.LENGTH_SHORT);
//                    warning.show();
//                    break;
//                } else {
                    a = cursor.getString(1);
//                }

                if (a.equals(username)){
                    b = cursor.getString(2);
                    positionCount = Integer.parseInt(cursor.getString(0));

                    break;
                }

            } while (cursor.moveToNext());

        }
        //adds one to save from gaining previous lines information later (in Calculate Risk)


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("positionCount",positionCount);
        editor.apply();

        return b;
    }


    public Cursor getAccount(int i) throws SQLException {
        //query database for current row for data
        Cursor mCursor = db.query(true, USER_TABLE, new String[]
                        {COLUMN_ROWID, COLUMN_EMAIL, COLUMN_PASSWORD, COLUMN_NAME, COLUMN_GP_NUMBER}, COLUMN_ROWID + " like " + i , null,
                null, null, null, null);
        //if cursor exists, go to the first point in database
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        //return the cursor
        return mCursor;
    }
}
//name 1, pass 2, username 3, email 4, number 5
