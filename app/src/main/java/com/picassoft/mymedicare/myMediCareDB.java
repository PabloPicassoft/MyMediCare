package com.picassoft.mymedicare;

/**
 * Created by Paul on 14/04/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Alex Hermon on 03/05/2016.
 *
 */

public class myMediCareDB {
    //variables for all columns in database
    public static final String COLUMN_ROWID = "_id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_NAME = "name";
    //public static final String COLUMN_GPNAME = "gpname";
    public static final String COLUMN_GP_NUMBER = "gpnumber";
    //public static final String COLUMN_COLOURSCHEME = "colourScheme";
    //public static final String COLUMN_TEXTSIZE = "textSize";
    private static final String TAG = "DBHelper";
    private static final String DATABASE_NAME = "MyMediCare.db";
    private static final String DATABASE_TABLE = "users";
    private static final int DATABASE_VERSION = 2;

    //string told database table name and order
    private static final String DATABASE_CREATE = "create table users(_id integer primary key not null, "
            +"email text not null, password text not null, name, gpnumber);";

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
            //try and catch used to avoid errors
            try {
                //execute SQL command within database
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                //print exception error
                e.printStackTrace();
            }
        }
        //if the database is updated, whipe it to prevent issues
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
    public void close(){
        DBHelper.close();
    }

    //add new row and contact to databse
    public void insertUser(User user) {

       // db = DBHelper.getWritableDatabase();

        //populate row with username and password
        ContentValues initialValues = new ContentValues();

        String query = "select * from " + DATABASE_TABLE ;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        initialValues.put(COLUMN_ROWID, count);
        initialValues.put(COLUMN_NAME, user.getName());
        initialValues.put(COLUMN_EMAIL, user.getEmail());
        initialValues.put(COLUMN_PASSWORD, user.getPassword());
        initialValues.put(COLUMN_GP_NUMBER, user.getGpNumber());

        db.insert(DATABASE_TABLE, null, initialValues);
        db.close();
    }

    public String searchUser(String username){

        String query = "select email, password from " + DATABASE_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        String a,b;
        b="NOT FOUND!";

        if(cursor.moveToFirst()){
            do {
                a = cursor.getString(0);

                if (a.equals(username)){
                    b = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        return b;
    }
    //return all contacts from database

//    public Cursor getAllUsers()
//    {
//        return db.query(DATABASE_TABLE, new String[] {COLUMN_ROWID, COLUMN_EMAIL,
//                COLUMN_PASSWORD}, null, null, null, null, null);
//    }
//
//    //return certain account
//    public Cursor getAccount(int i) throws SQLException {
//        //query database for current row for data
//        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[]
//                {COLUMN_ROWID, COLUMN_EMAIL, COLUMN_PASSWORD, COLUMN_NAME, COLUMN_GP_NUMBER}, COLUMN_ROWID
//                + " like " + i , null, null, null, null, null);
//
//        //if cursor exists, go to the first point in database
//        if (mCursor != null) {
//            mCursor.moveToFirst();
//        }
//        //return the cursor
//        return mCursor;
//    }
//
//    //insert data values into the database
//    public void insertDetails(String name, String address, String gpname, String gpnumber, int row){
//        //new instance of content values
//        //add all parsed information
//        ContentValues initialValues = new ContentValues();
//            initialValues.put(COLUMN_NAME, name);
//            //initialValues.put(COLUMN_GPNAME, gpname);
//            initialValues.put(COLUMN_GP_NUMBER, gpnumber);
//
//            db.update(DATABASE_TABLE, initialValues, COLUMN_ROWID + "=" + row  ,null );
//        }

}
