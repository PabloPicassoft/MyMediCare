package com.picassoft.mymedicare;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;


public class myMediCareDB {

    //variables for all columns in database
    private static final String TAG = "DBHelper";

    private static final String DATABASE_NAME = "MediDatabase";

    private static final String USER_TABLE = "users";
    public static final String COLUMN_USERID = "_id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_GP_NUMBER = "gpnumber";
    public static final String COLUMN_COLOURSCHEME = "colourScheme";
    //public static final String COLUMN_TEXTSIZE = "textSize";

    private static final String MEASUREMENTS_TABLE = "measurements";
    public static final String COLUMN_MEASUREMENTID = "_measureid";
    public static final String COLUMN_FOREIGN_USERID = "_userid";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_TEMPERATURE = "temperature";
    public static final String COLUMN_TEMPERATURE_VERDICT = "temperature_verdict";
    public static final String COLUMN_LBP = "lbp";
    public static final String COLUMN_LBP_VERDICT = "lbp_verdict";
    public static final String COLUMN_HBP = "hbp";
    public static final String COLUMN_HBP_VERDICT = "hbp_verdict";
    public static final String COLUMN_HEARTRATE = "heartrate";
    public static final String COLUMN_HEARTRATE_VERDICT = "heartrate_verdict";

    private static final int DATABASE_VERSION = 2;

    //string query to hold database table name and order of columns
    private static final String CREATE_USER_TABLE = "create table "+ USER_TABLE + "("
            + COLUMN_USERID + " integer primary key autoincrement, "
            + COLUMN_EMAIL + " text not null, "
            + COLUMN_PASSWORD + " text not null, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_COLOURSCHEME + " text not null, "
            + COLUMN_GP_NUMBER + " text not null);";

    //string query to hold database table name and order of columns
    private static final String CREATE_MEASUREMENTS_TABLE = "create table "+ MEASUREMENTS_TABLE + "("
            + COLUMN_MEASUREMENTID + " integer primary key autoincrement, "
            + COLUMN_FOREIGN_USERID + " text not null, "
            + COLUMN_DATE + " text, "
            + COLUMN_TIME + " text, "
            + COLUMN_TEMPERATURE + " text not null, "
            + COLUMN_TEMPERATURE_VERDICT + " text not null, "
            + COLUMN_LBP + " text not null, "
            + COLUMN_LBP_VERDICT + " text not null, "
            + COLUMN_HBP + " text not null, "
            + COLUMN_HBP_VERDICT + " text not null, "
            + COLUMN_HEARTRATE + " text not null, "
            + COLUMN_HEARTRATE_VERDICT + " text not null, "
            + "foreign key (" + COLUMN_FOREIGN_USERID + ") references " + USER_TABLE + "(" + COLUMN_USERID + ")" + ");";

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

            db.execSQL(CREATE_MEASUREMENTS_TABLE);
            db.execSQL(CREATE_USER_TABLE);

        }

        //if the database is updated, wipe it to prevent conflicts
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + MEASUREMENTS_TABLE);
            onCreate(db);
        }
    }

    //open database
    public myMediCareDB open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        //sqlDB = DBHelper.getWritableDatabase();
        return this;
    }

    //close the database
    public void close() {
        DBHelper.close();
    }

    //add new row and calc to database
    public void insertCalculation(Calculation calc) {

        //populate rows with measurements
        ContentValues newCalc = new ContentValues();

        newCalc.put(COLUMN_TEMPERATURE, calc.getTemperatureReading());
        newCalc.put(COLUMN_LBP, calc.getlBPReading());
        newCalc.put(COLUMN_HBP, calc.gethBPReading());
        newCalc.put(COLUMN_HEARTRATE, calc.getHeartRateReading());
        newCalc.put(COLUMN_FOREIGN_USERID, calc.getForeignUserID());
        newCalc.put(COLUMN_DATE, calc.getDate());
        newCalc.put(COLUMN_TIME, calc.getTime());
        newCalc.put(COLUMN_TEMPERATURE_VERDICT, calc.getVerdictTemp());
        newCalc.put(COLUMN_LBP_VERDICT, calc.getVerdictLBP());
        newCalc.put(COLUMN_HBP_VERDICT, calc.getVerdictHBP());
        newCalc.put(COLUMN_HEARTRATE_VERDICT, calc.getVerdictHR());

        db.insert(MEASUREMENTS_TABLE, null, newCalc);
    }

    public Cursor getCalculation(int i) throws SQLException {
        //query database for current row for data
        Cursor mCursor = db.query(true, MEASUREMENTS_TABLE, new String[]
                        {COLUMN_MEASUREMENTID,
                                COLUMN_FOREIGN_USERID,
                                COLUMN_DATE,
                                COLUMN_TIME,
                                COLUMN_TEMPERATURE,
                                COLUMN_LBP,
                                COLUMN_HBP,
                                COLUMN_HEARTRATE,
                                COLUMN_TEMPERATURE_VERDICT,
                                COLUMN_LBP_VERDICT,
                                COLUMN_HBP_VERDICT,
                                COLUMN_HEARTRATE_VERDICT},
                        COLUMN_FOREIGN_USERID + " like " + i , null,null, null, null, null);
        //if cursor exists, go to the first point in database
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        //return the cursor
        return mCursor;
    }

    public  void deleteByID(int row) {
        long result = db.delete(USER_TABLE, COLUMN_USERID + " = " + row, null);
        //db.delete(MEASUREMENTS_TABLE, COLUMN_FOREIGN_USERID + " = " + row, null);
        Log.d(TAG, String.valueOf(result));
    }

    public void updateDB(String email, String name, String pass, String gpNumber, int row, String colourSheme) {

        ContentValues updatedValues = new ContentValues();

        updatedValues.put(COLUMN_EMAIL, email);
        updatedValues.put(COLUMN_NAME, name);
        updatedValues.put(COLUMN_PASSWORD, pass);
        updatedValues.put(COLUMN_GP_NUMBER, gpNumber);
        updatedValues.put(COLUMN_COLOURSCHEME, colourSheme);

        db.update(USER_TABLE, updatedValues, COLUMN_USERID + " = " + row , null);
    }

    //get user details
    public String loginAuth(String username){

        String query = "select _id, email, password, gpnumber from " + USER_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        String a,b;
        b="NOT FOUND!";

        int positionCount = 0;

        if (cursor != null) {

            cursor.moveToFirst();

            do {
                    a = cursor.getString(1);

                if (a.equals(username)){
                    b = cursor.getString(2);
                    positionCount = Integer.parseInt(cursor.getString(0));

                    break;
                }

            } while (cursor.moveToNext());
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("positionCount",positionCount);
        editor.apply();

        return b;
    }

    public Cursor getAccount(int i) throws SQLException {
        //query database for current row for datca
        Cursor mCursor = db.query(true, USER_TABLE, new String[]
                        {COLUMN_USERID,
                                COLUMN_EMAIL,
                                COLUMN_PASSWORD,
                                COLUMN_NAME,
                                COLUMN_GP_NUMBER,
                                COLUMN_COLOURSCHEME},
                        COLUMN_USERID + " like " + i , null, null, null, null, null);
        //if cursor exists, go to the first point in database
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        //return the cursor
        return mCursor;
    }

    //add new row and contact to database
    public void insertUser(User user) {

        //populate rows with username and password
        ContentValues initialValues = new ContentValues();

        initialValues.put(COLUMN_EMAIL, user.getEmail());
        initialValues.put(COLUMN_NAME, user.getName());
        initialValues.put(COLUMN_PASSWORD, user.getPassword());
        initialValues.put(COLUMN_GP_NUMBER, user.getGpNumber());
        initialValues.put(COLUMN_COLOURSCHEME, "#DCF5F5F5");

        db.insert(USER_TABLE, null, initialValues);
    }
    //

    public Cursor findColour(int rowValue){

        String query = "select " + COLUMN_COLOURSCHEME + " from " + USER_TABLE + " where " + COLUMN_USERID + " = " + rowValue;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }
    /*
                          MEOW

                       .               ,.
                  T."-._..---.._,-"/|
                  l|"-.  _.v._   (" |
                  [l /.'_ \; _~"-.`-t
                  Y " _(o} _{o)._ ^.|
                  j  T  ,-<v>-.  T  ]
                  \  l ( /-^-\ ) !  !
                   \. \.  "~"  ./  /c-..,__
                     ^r- .._ .- .-"  `- .  ~"--.
                      > \.                      \
                      ]   ^.                     \
                      3  .  ">            .       Y
         ,.__.--._   _j   \ ~   .         ;       |
        (    ~"-._~"^._\   ^.    ^._      I     . l
         "-._ ___ ~"-,_7    .Z-._   7"   Y      ;  \        _
            /"   "~-(r r  _/_--._~-/    /      /,.--^-._   / Y
            "-._    '"~~~>-._~]>--^---./____,.^~        ^.^  !
                ~--._    '   Y---.                        \./
                     ~~--._  l_   )                        \
                           ~-._~~~---._,____..---           \
                               ~----"~       \
                                              \

    */
}
