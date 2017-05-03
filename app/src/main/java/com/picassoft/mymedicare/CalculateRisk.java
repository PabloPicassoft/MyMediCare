package com.picassoft.mymedicare;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

import static java.text.DateFormat.getTimeInstance;

public class CalculateRisk extends AppCompatActivity {

    myMediCareDB db;
    int highRiskCount = 0;
    Intent backToMain = new Intent(CalculateRisk.this, NavDrawer.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new myMediCareDB(getBaseContext());

        setContentView(R.layout.activity_calculate_risk);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CalculateRisk.this);
        int h = 0;
        int userPosition = preferences.getInt("positionCount", h);

        db.open();
        Cursor cursor = db.findColour(userPosition);
        db.close();

        String colour = cursor.getString(0);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_calculate_risk);
        relativeLayout.setBackgroundColor(Color.parseColor(colour));

        Spinner tempDropdown = (Spinner) findViewById(R.id.spinner_temp_format);

        String[] items = new String[]{"°C", "°F"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        tempDropdown.setAdapter(adapter);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);

        final TextView HRMeasureText = (TextView) findViewById(R.id.result_label);
        //HRMeasureText.setVisibility(View.INVISIBLE);

        Button results = (Button) findViewById(R.id.button_calculate);
        results.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    TextView temp = (TextView) findViewById(R.id.input_temperature);
                    TextView Hibp = (TextView) findViewById(R.id.input_bp_high);
                    TextView Lobp = (TextView) findViewById(R.id.input_bp_low);
                    //Lobp.setTextSize(DataManager.textSizeHeader);
                    TextView hr = (TextView) findViewById(R.id.input_heartrate);

                    //setup variable to hold spinner id value holding temp type selected
                    Spinner tempDropdown = (Spinner) findViewById(R.id.spinner_temp_format);

                    final double tempInt = Integer.parseInt(temp.getText().toString());
                    final int HBPInt = Integer.parseInt(Hibp.getText().toString());
                    final int LBPInt = Integer.parseInt(Lobp.getText().toString());
                    final int HRInt = Integer.parseInt(hr.getText().toString());

                    final String celsiusOrFahr = (String) tempDropdown.getSelectedItem();
                    String verdictTemp = null;
                    String verdictHBP = null;
                    String verdictLBP = null;
                    String verdictHR = null;

                    //if the degrees value is fahrenheit
                    if (celsiusOrFahr.equals("°F")) {

                        if (tempInt > 100.4) {
                            verdictTemp = "HIGH RISK";
                            highRiskCount += 1;
                        } else if (tempInt <= 100.4 && tempInt > 98.6) {
                            verdictTemp = "LOW RISK";
                        } else if (tempInt <= 98.6) {
                            verdictTemp = "NORMAL";
                        }

                        if (HBPInt >= 180) {
                            verdictHBP = "HIGH RISK";
                            highRiskCount += 1;
                        } else if (HBPInt < 180 && HBPInt > 120) {
                            verdictHBP = "LOW RISK";
                        } else if (HBPInt <= 120) {
                            verdictHBP = "NORMAL";
                        }

                        if (LBPInt >= 110) {
                            verdictLBP = "HIGH RISK";
                            highRiskCount += 1;
                        } else if (LBPInt < 110 && LBPInt > 80) {
                            verdictLBP = "LOW RISK";
                        } else if (LBPInt <= 80) {
                            verdictLBP = "NORMAL";
                        }

                        if (HRInt > 160) {
                            verdictHR = "HIGH RISK";
                            highRiskCount += 1;
                        } else if (HRInt <= 160 && HRInt >= 72) {
                            verdictHR = "LOW RISK";
                        } else if (HRInt < 72) {
                            verdictHR = "NORMAL";
                        }

                        //if the degrees value is in celcius
                    } else if (celsiusOrFahr.equals("°C")) {

                        if (tempInt > 38) {
                            verdictTemp = "HIGH RISK";
                            highRiskCount += 1;
                        } else if (tempInt <= 38 && tempInt > 37) {
                            verdictTemp = "LOW RISK";
                        } else if (tempInt <= 37) {
                            verdictTemp = "NORMAL";
                        }

                        if (HBPInt >= 180) {
                            verdictHBP = "HIGH RISK";
                            highRiskCount += 1;
                        } else if (HBPInt < 180 && HBPInt > 120) {
                            verdictHBP = "LOW RISK";
                        } else if (HBPInt <= 120) {
                            verdictHBP = "NORMAL";
                        }

                        if (LBPInt >= 110) {
                            verdictLBP = "HIGH";
                            highRiskCount += 1;
                        } else if (LBPInt < 110 && LBPInt > 80) {
                            verdictLBP = "LOW RISK";
                        } else if (LBPInt <= 80) {
                            verdictLBP = "NORMAL";
                        }

                        if (HRInt > 160) {
                            verdictHR = "HIGH RISK";
                            highRiskCount += 1;
                        } else if (HRInt <= 160 && HRInt > 72) {
                            verdictHR = "LOW RISK";
                        } else if (HRInt <= 72) {
                            verdictHR = "NORMAL";
                        }
                    }

                    HRMeasureText.setVisibility(View.VISIBLE);
                    TextView output = (TextView) findViewById(R.id.output_riskyness);
                    output.setText(String.valueOf(highRiskCount));

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CalculateRisk.this);

                    int h = 0;
                    int userPosition = preferences.getInt("positionCount", h);

                    /***************************** INSERT NEW CALCULATION RECORD *********************/
                    Calculation calc = new Calculation();

                    //Foreign userID
                    calc.setForeignUserID(userPosition);

                    //temperature data
                    calc.setTemperatureReading(temp.getText().toString());
                    calc.setVerdictTemp(verdictTemp);

                    //lbp data
                    calc.setlBPReading(Lobp.getText().toString());
                    calc.setVerdictLBP(verdictLBP);

                    //hbp data
                    calc.sethBPReading(Hibp.getText().toString());
                    calc.setVerdictHBP(verdictHBP);

                    //Heart Rate data
                    calc.setHeartRateReading(hr.getText().toString());
                    calc.setVerdictHR(verdictHR);

                    //Date Data
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String strDate = sdf.format(calendar.getTime());

                    calc.setDate(strDate);

                    //Time Data
                    long millis = new Date().getTime();
                    String time = getTimeInstance(SimpleDateFormat.MEDIUM, Locale.UK).format(millis);

                    String strTime = time + " GMT";
                    calc.setTime(strTime);

                    db.open();
                    final Cursor c = db.getAccount(userPosition);
                    db.insertCalculation(calc);
                    db.close();

                    final AlertDialog.Builder highRiskAlert  = new AlertDialog.Builder(CalculateRisk.this);
                    highRiskAlert.setMessage("An SMS has been sent to alert your GP of your measurements, and an appointment will be booked. \n" +
                            "\nPlease keep an eye on your inbox for your GP's confirmation of the appointment.");
                    highRiskAlert.setTitle("HIGH RISK!");
                    highRiskAlert.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(backToMain);
                            Toast.makeText(CalculateRisk.this, "Text Message Sent to " + c.getString(4), Toast.LENGTH_LONG).show();
                        }
                    });
                    highRiskAlert.setCancelable(false);


                    AlertDialog.Builder resultAlert  = new AlertDialog.Builder(CalculateRisk.this);

                    resultAlert.setMessage("\nTemperature: " + verdictTemp + " \n\n" +
                            "Higher Blood Pressure: " + verdictHBP + "  \n\n" +
                            "Lower Blood Pressure: " + verdictLBP + " \n\n" +
                            "Heart Rate: " + verdictHR);

                    resultAlert.setTitle("Results");
                    resultAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (highRiskCount >= 3) {
                                        try {
                                            //Store SMS message containing user's measurements,
                                            String gpSMSMessage = c.getString(3) + " has a Temperature of " + tempInt + celsiusOrFahr +
                                                    ", Blood Pressure Reading of " + LBPInt + "/" + HBPInt +
                                                    ", and an average Heart Rate of " + HRInt + ". Please book an appointment for " + c.getString(3) + " as soon as possible.";

                                            //initialise an smsmanager to user the default messaging application
                                            SmsManager smsService = SmsManager.getDefault();

                                            //as the message is longer than 160 characters, use divideMessage to split the message into transmittable chunks.
                                            ArrayList<String> parts = smsService.divideMessage(gpSMSMessage);

                                            //instead of using sendTextMessage, use sendMultiPart version that accepts String arraylist rather than String variable.
                                            smsService.sendMultipartTextMessage(c.getString(4), null, parts, null, null);
                                        }
                                        catch (Exception e) {
                                            Toast messageSent = Toast.makeText(CalculateRisk.this, "Text Message Failed to Send. Please check stored number.", Toast.LENGTH_LONG);
                                            messageSent.show();
                                        }
                                        highRiskAlert.create().show();
                                    } else {
                                        startActivity(backToMain);
                                    }
                                    highRiskCount = 0;

                                }
                            });
                    resultAlert.setCancelable(false);
                    resultAlert.create().show();

                } catch (NumberFormatException e) {
                    Toast.makeText(getBaseContext(), "Please Use Int Values", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}