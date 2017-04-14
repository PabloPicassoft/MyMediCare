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

public class myDatabase {
    //veriables for all columns in database
    public static final String KEY_ROWID = "_id";
    public static final String KEY_userNAME = "uName";
    public static final String KEY_password = "password";
    public static final String KEY_Name = "name";
    public static final String KEY_Address = "address";
    public static final String KEY_Age = "age";
    public static final String KEY_GPname = "gpname";
    public static final String KEY_GPnumber = "gpnumber";
    public static final String KEY_colourScheme = "colourScheme";
    public static final String KEY_textSize = "textSize";
    private static final String TAG = "MyDBHelper";
    private static final String DATABASE_NAME = "MyDB";
    private static final String DATABASE_TABLE = "login";
    private static final int DATABASE_VERSION = 2;

    //string told database table name and order
    private static final String DATABASE_CREATE = "create table login(_id integer primary key autoincrement, "
            +"uName text not null, password text not null, name, address, age, gpname, gpnumber);";

    //variables for holding database context, helper and SQLite instances
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    //method to create instance of database helper
    public myDatabase(Context ctx)
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
    public myDatabase open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }
    //close the database
    public void close(){
        DBHelper.close();
    }
    //add new row and contact to databse
    public void insertContact(String name, String pass)
    {
        //populate row with username and password
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_userNAME, name);
        initialValues.put(KEY_password, pass);
        db.insert(DATABASE_TABLE, null, initialValues);
    }
    //return all contacts from database

    public Cursor getAllContacts()
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_userNAME,
                KEY_password}, null, null, null, null, null);
    }

    //return certain account
    public Cursor getAccount(int i) throws SQLException {
        //query database for current row for data
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_userNAME, KEY_password,KEY_Name, KEY_Address, KEY_Age, KEY_GPname, KEY_GPnumber}, KEY_ROWID + " like " + i , null,
            null, null, null, null);

        //if cursor exists, go to the first point in database
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        //return the cursor
        return mCursor;
    }

    //insert data values into the database
    public void insertDetails(String name, String address, String age, String gpname, String gpnumber, int row){
        //new instance of content values
        //add all parsed information
        ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_Name, name);
            initialValues.put(KEY_Address, address);
            initialValues.put(KEY_Age, age);
            initialValues.put(KEY_GPname, gpname);
            initialValues.put(KEY_GPnumber, gpnumber);

            db.update(DATABASE_TABLE, initialValues, KEY_ROWID + "=" + row  ,null );
        }

}
