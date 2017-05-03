package com.picassoft.mymedicare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    myMediCareDB db;
    SharedPreferences preferences;
    String colour;
    static int userPosition;

    boolean colourSelected = false;
    boolean checkboxon = false;
   // boolean checkboxOnAndHasText = false;

    RadioGroup radioGroup;
    private static final String TAG = "Settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new myMediCareDB(getBaseContext());

        setContentView(R.layout.activity_settings);

        /*stop the keyboard automatically popping up when activity begins. Keyboard will appear when
        * the user taps an EditText.
        */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //store users position in the scope of the OnCreate
        preferences = PreferenceManager.getDefaultSharedPreferences(Settings.this);
        int h = 0;
        userPosition = preferences.getInt("positionCount", h);

        //get a writeable version of the database
        db.open();
            Cursor cursor = db.findColour(userPosition);
        db.close();
        //close the database

        
        String initialColour = cursor.getString(0);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_settings);
        relativeLayout.setBackgroundColor(Color.parseColor(initialColour));

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup_text_size);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG,  "Clicked = " + checkedId);

                switch(checkedId)
                {
                    case R.id.radio_colour_blue:
                        colour = "#b7d4ff";
                        colourSelected = true;
                    break;

                    case R.id.radio_colour_normal:
                        colour = "#DCF5F5F5";
                        colourSelected = true;
                    break;

                    case R.id.radio_colour_pink:
                        colour = "#ffaaf9";
                        colourSelected = true;
                    break;
                }
            }
        });

        final TextView currentNum = (TextView) findViewById(R.id.current_num_view);

        try {
            db.open();
            Cursor num = db.getAccount(userPosition);
            currentNum.setText(String.valueOf(num.getString(4)));
            db.close();

            //Toast.makeText(Settings.this, String.valueOf(num.getString(4)), Toast.LENGTH_LONG).show();
        } catch (CursorIndexOutOfBoundsException c) {
            Toast.makeText(Settings.this, "NO NUMBER TO DISPLAY", Toast.LENGTH_LONG).show();
        }

        final CheckBox updateNumber = (CheckBox) findViewById(R.id.checkBox_updategpnum);

        final EditText newGPNumber = (EditText) findViewById(R.id.input_gp_number);
        newGPNumber.setEnabled(false);

        updateNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (updateNumber.isChecked()){
                    newGPNumber.setEnabled(true);
                    checkboxon = true;
                } else {
                    //newGPNumber.setKeyListener(null);
                    newGPNumber.setEnabled(false);
                }
            }
        });


        Button updateSettings = (Button) findViewById(R.id.button_update_settings);
        updateSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                preferences = PreferenceManager.getDefaultSharedPreferences(Settings.this);
                int h = 0;
                userPosition = preferences.getInt("positionCount", h);

                String newGPNumStr = newGPNumber.getText().toString();

                /*check if both a colour radio button is clicked, and there is a number in the editText
                * if condition is true, the user wants to update both colour and number, call updateDB
                * and pass in the new number and colour scheme
                */
                if ((colourSelected) && checkboxon && !TextUtils.isEmpty(newGPNumStr)) {

                    db.open();
                    Cursor num = db.getAccount(userPosition);
                    db.updateDB(num.getString(1), num.getString(3), num.getString(2), newGPNumStr, userPosition, colour); //132
                    db.close();

                    currentNum.setText(newGPNumStr);

                    RelativeLayout settings = (RelativeLayout) findViewById(R.id.activity_settings);
                    settings.setBackgroundColor(Color.parseColor(colour));

                    Toast.makeText(Settings.this, "Colour Scheme and Number Changed", Toast.LENGTH_SHORT).show();
                /*check if only a colour radio button is selected if condition is true, the user wants
                * to only update the accounts colour scheme, call updateDB and pass in the new colour scheme
                * and alert user their colour scheme was changed
                */
                } else if (colourSelected) {
                    db.open();
                    Cursor colourOnly = db.getAccount(userPosition);
                    db.updateDB(colourOnly.getString(1), colourOnly.getString(3), colourOnly.getString(2), colourOnly.getString(4), userPosition, colour); //132
                    db.close();

                    RelativeLayout settings = (RelativeLayout) findViewById(R.id.activity_settings);
                    settings.setBackgroundColor(Color.parseColor(colour));

                    Toast.makeText(Settings.this, "Colour Scheme Changed", Toast.LENGTH_SHORT).show();

                /*check if the enable checkbox is selected and there is a number in the edit text,
                * if true, pass in the new GP number to replace the onld number in the DB
                * alert user their number was changed.
                */
                } else if (checkboxon && !TextUtils.isEmpty(newGPNumStr)) {

                    db.open();
                    Cursor numberOnly = db.getAccount(userPosition);
                    currentNum.setText(newGPNumStr);
                    db.updateDB(numberOnly.getString(1), numberOnly.getString(3), numberOnly.getString(2), newGPNumStr, userPosition, numberOnly.getString(5)); //132
                    db.close();

                    Toast.makeText(Settings.this, "New GP Number Set", Toast.LENGTH_SHORT).show();

                } else {
                    //if none are selected, alert user there were no settings selected.
                    Toast.makeText(Settings.this, "No Settings to Update", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button deleteAccount = (Button) findViewById(R.id.button_delete_account);
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //find the user's id using shared prefs
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Settings.this);
                int h = 0;
                int userPosition = preferences.getInt("positionCount", h);

                //call deleteByID passing in the user's position in the database to delete the correct record.
                db.open();
                db.deleteByID(userPosition);
                db.close();

                //inform that user was deleted
                Toast informDeletion = Toast.makeText(Settings.this, "Account has been deleted", Toast.LENGTH_LONG);
                informDeletion.show();

                //return to the login page to revoke access to that account completely
                Intent backToLogin = new Intent(Settings.this, LoginScreen.class);
                startActivity(backToLogin);
            }
        });

        Button home = (Button) findViewById(R.id.back_to_main);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this,NavDrawer.class));
            }
        });
    }
}
