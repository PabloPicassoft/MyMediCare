package com.picassoft.mymedicare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    myMediCareDB db;
    SharedPreferences preferences;
    static int userPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new myMediCareDB(getBaseContext());

        setContentView(R.layout.activity_settings);

        preferences = PreferenceManager.getDefaultSharedPreferences(Settings.this);
        int h = 0;
        userPosition = preferences.getInt("positionCount", h);


        final TextView currentNum = (TextView) findViewById(R.id.current_num_view);
        db.open();
        Cursor num = db.getAccount(userPosition);
        db.close();
        currentNum.setText(String.valueOf(num.getString(4)));

        Button updateSettings = (Button) findViewById(R.id.button_update_settings);
        updateSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText newGPNumber = (EditText) findViewById(R.id.input_gp_number);
                String newGPNumStr = newGPNumber.getText().toString();

                db.open();

                Cursor c = db.getAccount(userPosition);
                db.updateDB(c.getString(1), c.getString(3), c.getString(2), newGPNumStr, userPosition); //132

                db.close();

                currentNum.setText(String.valueOf(c.getString(4)));

                //refresh activity to show updated number
                Intent refresh = new Intent(Settings.this, Settings.class);
                startActivity(refresh);

            }
        });

        Button deleteAccount = (Button) findViewById(R.id.button_delete_account);
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.open();
                db.deleteByID(userPosition);
                db.close();

                Toast informDeletion = Toast.makeText(Settings.this, "Account has been deleted", Toast.LENGTH_LONG);
                informDeletion.show();

                //refresh activity to show updated number
                Intent backToLogin = new Intent(Settings.this, LoginScreen.class);
                startActivity(backToLogin);



            }
        });

    }
}
