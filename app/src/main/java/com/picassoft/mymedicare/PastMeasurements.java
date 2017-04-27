package com.picassoft.mymedicare;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


public class PastMeasurements extends AppCompatActivity {

    myMediCareDB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_measurements);

        db = new myMediCareDB(getBaseContext());


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PastMeasurements.this);
        int h = 0;
        int userPosition = preferences.getInt("positionCount", h);

        try {
            db.open();
            Cursor calCursor = db.getCalculation(userPosition);
            db.close();

            //Toast.makeText(getBaseContext(), userPosition, Toast.LENGTH_LONG).show();
            TextView test = (TextView) findViewById(R.id.test);
            test.setText("Measurement: " + calCursor.getString(0) + " \nUser: " + calCursor.getString(1) +
                    " \nDate: " + calCursor.getString(2) + " \n\nTEMP: " + calCursor.getString(3) +
                    " \nLBP: " + calCursor.getString(4) + " \nHBP: " + calCursor.getString(5) +
                    " \nHEARTRATE: " + calCursor.getString(6));

        }catch (CursorIndexOutOfBoundsException noPastCalc) {

            final AlertDialog.Builder noCalcsFoundDialog  = new AlertDialog.Builder(PastMeasurements.this);
            noCalcsFoundDialog.setMessage("There are no past calculations to view. \n\nClick \"Calculate Risk\" in the side navigation or the main menu to calculate your risk.");
            noCalcsFoundDialog.setTitle("Nothing to see here");
            noCalcsFoundDialog.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(PastMeasurements.this, NavDrawer.class));
                }
            });
            noCalcsFoundDialog.setCancelable(false);
            noCalcsFoundDialog.create().show();
        }

    }
}
