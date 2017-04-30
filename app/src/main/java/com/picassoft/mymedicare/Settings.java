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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class Settings extends AppCompatActivity {

    myMediCareDB db;
    SharedPreferences preferences;
    String colour = "RED";
    static int userPosition;

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

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup_text_size);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG,  "Clicked = " + checkedId);
                switch(checkedId)
                {
                    case R.id.radio_colour_blue:
                        colour = "#b7d4ff";
                    break;

                    case R.id.radio_colour_normal:
                        colour = "#DCF5F5F5";
                    break;

                    case R.id.radio_colour_pink:
                        colour = "#ffaaf9";
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



        Button updateSettings = (Button) findViewById(R.id.button_update_settings);
        updateSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText newGPNumber = (EditText) findViewById(R.id.input_gp_number);
                String newGPNumStr = newGPNumber.getText().toString();

                preferences = PreferenceManager.getDefaultSharedPreferences(Settings.this);
                int h = 0;
                userPosition = preferences.getInt("positionCount", h);

                db.open();
                Cursor num = db.getAccount(userPosition);
                currentNum.setText(newGPNumStr);

                //Cursor c = db.getAccount(userPosition);
                db.updateDB(num.getString(1), num.getString(3), num.getString(2), newGPNumStr, userPosition, colour); //132

                db.close();


               // View pastcalcView = LayoutInflater.from(getApplication()).inflate(R.id.activity_past_measurements, null);

                RelativeLayout settings = (RelativeLayout) findViewById(R.id.activity_settings);
                //RelativeLayout mainmenu = (RelativeLayout) findViewById(R.id.content_nav_drawer);
                //RelativeLayout pastCalc = (RelativeLayout) findViewById(R.id.activity_past_measurements);

                settings.setBackgroundColor(Color.parseColor(colour));
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
