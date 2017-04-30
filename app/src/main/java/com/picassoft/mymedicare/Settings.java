package com.picassoft.mymedicare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

        preferences = PreferenceManager.getDefaultSharedPreferences(Settings.this);
        int h = 0;
        userPosition = preferences.getInt("positionCount", h);

        db.open();
        Cursor cursor = db.findColour(userPosition);
        db.close();

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

        try{
            db.open();
            Cursor num = db.getAccount(userPosition);
            currentNum.setText(String.valueOf(num.getString(4)));
            db.close();

            Toast.makeText(Settings.this, String.valueOf(num.getString(4)), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(Settings.this, "CHECKBOX CLICKED", Toast.LENGTH_SHORT).show();
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
                //get users new number
                String newGPNumStr = newGPNumber.getText().toString();

                if ((colourSelected) && (checkboxon && !newGPNumStr.equals(null))) {

                    db.open();
                    Cursor num = db.getAccount(userPosition);
                    db.updateDB(num.getString(1), num.getString(3), num.getString(2), newGPNumStr, userPosition, colour); //132
                    db.close();

                    currentNum.setText(newGPNumStr);

                    RelativeLayout settings = (RelativeLayout) findViewById(R.id.activity_settings);
                    settings.setBackgroundColor(Color.parseColor(colour));

                    Toast.makeText(Settings.this, "Colour Scheme and Number Changed", Toast.LENGTH_SHORT).show();
                } else if (colourSelected && !checkboxon) {
                    db.open();
                    Cursor colourOnly = db.getAccount(userPosition);
                    //currentNum.setText(newGPNumStr);
                    db.updateDB(colourOnly.getString(1), colourOnly.getString(3), colourOnly.getString(2), colourOnly.getString(4), userPosition, colour); //132
                    db.close();

                    RelativeLayout settings = (RelativeLayout) findViewById(R.id.activity_settings);
                    settings.setBackgroundColor(Color.parseColor(colour));

                    Toast.makeText(Settings.this, "Colour Scheme Changed", Toast.LENGTH_SHORT).show();
                } else if (checkboxon && !newGPNumStr.equals(null)) {
                    db.open();
                    Cursor numberOnly = db.getAccount(userPosition);
                    currentNum.setText(newGPNumStr);
                    db.updateDB(numberOnly.getString(1), numberOnly.getString(3), numberOnly.getString(2), newGPNumStr, userPosition, numberOnly.getString(5)); //132
                    db.close();

                    Toast.makeText(Settings.this, "New GP Number Set", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Settings.this, "No Settings to Update", Toast.LENGTH_SHORT).show();
                }


               // View pastcalcView = LayoutInflater.from(getApplication()).inflate(R.id.activity_past_measurements, null);

                //RelativeLayout mainmenu = (RelativeLayout) findViewById(R.id.content_nav_drawer);
                //RelativeLayout pastCalc = (RelativeLayout) findViewById(R.id.activity_past_measurements);
                //mainmenu.setBackgroundColor(Color.parseColor(colour));
               // pastcalcView.setBackgroundColor(Color.parseColor(colour));
//                currentNum.setText(String.valueOf(c.getString(4)));

                //refresh activity to show updated number
                //Intent refresh = new Intent(Settings.this, Settings.class);
                //startActivity(refresh);

            }
        });

        Button deleteAccount = (Button) findViewById(R.id.button_delete_account);
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Settings.this);
                int h = 0;
                int userPosition = preferences.getInt("positionCount", h);

                Toast.makeText(Settings.this, "USER POSITION = " + userPosition ,Toast.LENGTH_SHORT).show();
                db.open();

                db.deleteByID(userPosition);
                Toast.makeText(Settings.this, "USER POSITION = " + userPosition ,Toast.LENGTH_SHORT).show();

                db.close();

                Toast informDeletion = Toast.makeText(Settings.this, "Account has been deleted", Toast.LENGTH_LONG);
                informDeletion.show();


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
