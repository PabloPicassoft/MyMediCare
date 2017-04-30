package com.picassoft.mymedicare;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.picassoft.mymedicare.R.color.background_material_light;


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

        db.open();
        Cursor cursor = db.findColour(userPosition);
        db.close();

        String colour;
        colour = cursor.getString(0);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_past_measurements);
        relativeLayout.setBackgroundColor(Color.parseColor(colour));

        final TextView pastMeasure = (TextView) findViewById(R.id.current_measurement_view);

        try {
            db.open();
                final Cursor calCursor = db.getCalculation(userPosition);
            db.close();

            //Toast.makeText(getBaseContext(), userPosition, Toast.LENGTH_LONG).show();

            pastMeasure.setText("Measurement: " + calCursor.getString(0) + " \nUser: " + calCursor.getString(1) +
                    " \nDate: " + calCursor.getString(2) + " \n\nTEMP: " + calCursor.getString(3) +
                    " \nLBP: " + calCursor.getString(4) + " \nHBP: " + calCursor.getString(5) +
                    " \nHEARTRATE: " + calCursor.getString(6));

            Button next = (Button) findViewById(R.id.next_record);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (calCursor.isLast()){
                        calCursor.moveToFirst();
                        dispRecord(calCursor);
                    } else {
                        calCursor.moveToNext();
                        dispRecord(calCursor);
                    }
                }
            });

            Button prev = (Button) findViewById(R.id.prev_record);
            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (calCursor.isFirst()){
                        calCursor.moveToLast();
                        dispRecord(calCursor);
                    } else {
                        calCursor.moveToPrevious();
                        dispRecord(calCursor);
                    }
                }
            });

            Button home = (Button) findViewById(R.id.pastcalcBackToHome);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(PastMeasurements.this, NavDrawer.class));
                }
            });

        } catch (CursorIndexOutOfBoundsException noPastCalc) {

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

    public void dispRecord(Cursor calCursor) {
        TextView pastMeasure = (TextView) findViewById(R.id.current_measurement_view);
        pastMeasure.setText("Measurement: " + calCursor.getString(0) + " \nUser: " + calCursor.getString(1) +
                " \nDate: " + calCursor.getString(2) + " \n\nTEMP: " + calCursor.getString(3) +
                " \nLBP: " + calCursor.getString(4) + " \nHBP: " + calCursor.getString(5) +
                " \nHEARTRATE: " + calCursor.getString(6));
    }
}
