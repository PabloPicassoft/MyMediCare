package com.picassoft.mymedicare;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CalculateRisk extends AppCompatActivity {

    myMediCareDB db;

    String tempResult = "";
    String HBPResult = "";
    String LBPResult = "";
    String BPMResult = "";

    int highRiskCount = 0;

    Intent backToMain = new Intent(CalculateRisk.this, NavDrawer.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_risk);

        Spinner tempDropdown = (Spinner) findViewById(R.id.spinner_temp_format);
        //String array to hold different temperature formats
        String[] items = new String[]{"°C", "°F"};
        //set spinner to contain values of items string above
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //initialize spinner
        tempDropdown.setAdapter(adapter);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);

        //create variable for results button
        Button results = (Button) findViewById(R.id.button_calculate);
        results.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //try and catch to prevent possible errors
                try {
                    //setup variables for all text fields for input values
                    TextView temp = (TextView) findViewById(R.id.input_temperature);
                    TextView Hibp = (TextView) findViewById(R.id.input_bp_high);
                    TextView Lobp = (TextView) findViewById(R.id.input_bp_low);
                    TextView hr = (TextView) findViewById(R.id.input_heartrate);

                    //setup variable to hold spinner id value holding temp type selected
                    Spinner tempDropdown = (Spinner) findViewById(R.id.spinner_temp_format);

                    //variables hold the values of all data input to text views
                    final double tempInt = Integer.parseInt(temp.getText().toString());
                    final int HBPInt = Integer.parseInt(Hibp.getText().toString());
                    final int LBPInt = Integer.parseInt(Lobp.getText().toString());
                    final int HRInt = Integer.parseInt(hr.getText().toString());

                    final String celsiusOrFahr = (String) tempDropdown.getSelectedItem();
                    //set up strings to holds results value of calculations
                    String verdictTemp = null;
                    String verdictHBP = null;
                    String verdictLBP = null;
                    String verdictHR = null;
                    //if the degrees value is in fahrenheit
                    if (celsiusOrFahr.equals("°F")) {

                        //follow statistic struction to determin where value sits
                        if (tempInt > 100.4) {
                            verdictTemp = "HIGH RISK";
                            highRiskCount += 1;
                        } else if (tempInt <= 100.4 && tempInt > 98.6) {
                            verdictTemp = "LOW RISK";
                        } else if (tempInt <= 98.6) {
                            verdictTemp = "NORMAL RISK";
                        }
                        //follow statistic struction to determin where value sits
                        if (HBPInt >= 180) {
                            verdictHBP = "HIGH RISK";
                            highRiskCount += 1;
                        } else if (HBPInt < 180 && HBPInt > 120) {
                            verdictHBP = "LOW RISK";
                        } else if (HBPInt <= 120) {
                            verdictHBP = "NORMAL";
                        }
                        //follow statistic struction to determin where value sits
                        if (LBPInt >= 110) {
                            verdictLBP = "HIGH RISK";
                            highRiskCount += 1;
                        } else if (LBPInt < 110 && LBPInt > 80) {
                            verdictLBP = "LOW RISK";
                        } else if (LBPInt <= 80) {
                            verdictLBP = "NORMAL RISK";
                        }
                        //follow statistic struction to determin where value sits
                        if (HRInt > 160) {
                            verdictHR = "HIGH RISK";
                            highRiskCount += 1;
                        } else if (HRInt <= 160 && HRInt >= 72) {
                            verdictHR = "LOW RISK";
                        } else if (HRInt < 72) {
                            verdictHR = "NORMAL RISK";
                        }

                        //if the degrees value is in celcius
                    } else if (celsiusOrFahr.equals("°C")) {
                        //follow statistic struction to determin where value sits
                        if (tempInt > 38) {
                            verdictTemp = "HIGH RISK";
                            highRiskCount += 1;
                        } else if (tempInt <= 38 && tempInt > 37) {
                            verdictTemp = "LOW RISK";
                        } else if (tempInt <= 37) {
                            verdictTemp = "NORMAL RISK";
                        }
                        //follow statistic struction to determin where value sits
                        if (HBPInt >= 180) {
                            verdictHBP = "HIGH RISK";
                            highRiskCount += 1;
                        } else if (HBPInt < 180 && HBPInt > 120) {
                            verdictHBP = "LOW RISK";
                        } else if (HBPInt <= 120) {
                            verdictHBP = "NORMAL RISK";
                        }
                        //follow statistic struction to determin where value sits
                        if (LBPInt >= 110) {
                            verdictLBP = "HIGH RISK";
                            highRiskCount += 1;
                        } else if (LBPInt < 110 && LBPInt > 80) {
                            verdictLBP = "LOW RISK";
                        } else if (LBPInt <= 80) {
                            verdictLBP = "NORMAL RISK";
                        }
                        //follow statistic struction to determin where value sits
                        if (HRInt > 160) {
                            verdictHR = "HIGH RISK";
                            highRiskCount += 1;
                        } else if (HRInt <= 160 && HRInt > 72) {
                            verdictHR = "LOW RISK";
                        } else if (HRInt <= 72) {
                            verdictHR = "NORMAL RISK";
                        }
                    }

                    //Store SMS message containing user's measurements,
                    String gpSMSMessage = "<USER'S NAME> has a Temperature of " + tempInt + celsiusOrFahr +
                            ", Blood Pressure Reading of " + LBPInt + "/" + HBPInt +
                            ", and an average Heart Rate of " + HRInt + ". Please book an appointment for <USER'S NAME> as soon as possible.";

                    //initialise an smsmanager to user the default messaging application
                    SmsManager smsService = SmsManager.getDefault();

                    //as the message is longer than 160 characters, use divideMessage to split the message into transmittable chunks.
                    ArrayList<String> parts = smsService.divideMessage(gpSMSMessage);

                    //instead of using sendTextMessage, use sendMultiPart version that accepts String arraylist rather than String variable.
                    smsService.sendMultipartTextMessage("+447522148149", null, parts, null, null);

                    final AlertDialog.Builder highRiskAlert  = new AlertDialog.Builder(CalculateRisk.this);
                    highRiskAlert.setMessage("An SMS has been sent to alert your GP of your measurements, and an appointment will be booked. \n" +
                            "\nPlease keep an eye on your inbox for your GP's confirmation of the appointment.");
                    highRiskAlert.setTitle("HIGH RISK!");
                    highRiskAlert.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(backToMain);
                        }
                    });
                    highRiskAlert.setCancelable(false);


                    AlertDialog.Builder resultAlert  = new AlertDialog.Builder(CalculateRisk.this);
                    resultAlert.setMessage(highRiskCount + "\nTemperature: " + verdictTemp + " \n\n" +
                            "Higher Blood Pressure: " + verdictHBP + "  \n\n" +
                            "Lower Blood Pressure: " + verdictLBP + " \n\n" +
                            "Heart Rate: " + verdictHR);
                    resultAlert.setTitle("Results");
                    resultAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (highRiskCount >= 3) {
                                        highRiskAlert.create().show();
                                    } else {
                                        startActivity(backToMain);
                                    }
                                    highRiskCount = 0;

                                }
                            });
                    resultAlert.setCancelable(false);
                    resultAlert.create().show();

                    //if any of the verdicts are HIGH send text to GP
//                    if (verdictTemp.equals("HIGH") || verdictLBP.equals("HIGH") || verdictHBP.equals("HIGH") || verdictHR.equals("HIGH")) {

// open the database
//                        db.open();
//                        //get value of shared preferences file for line number
//                        //SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
//                        int rowValue = prefs.getInt("rowVal", 0);
//                        //get data values for current users data row
//                        Cursor c = db.getAccount(rowValue);
//                        //if a GP number exists, send message
//                        if (!c.getString(7).toString().equals(null) || !c.getString(7).toString().equals("")) {
//                            //compile warning message which includes all readings
//                            String textMsg = (c.getString(3) + " has high readings: Temperature = " + temperatInt + " " + verdictTemp + ", High Blood Pressure: " + HighbloodPresureInt + " " + verdictHBP + ", Low Blood Pressure: " + LowbloodPresureInt + " " + verdictLBP + ", Heart Rate: " + hearRateInt + " " + verdictHR);
//                            //tell user a message has been sent to the GP
//                            Toast.makeText(getBaseContext(), "a message has been sent to your GP warning them of HIGH RISK reading", Toast.LENGTH_LONG).show();
//                            //open new instance of messanger applicaiton
//                            SmsManager sms = SmsManager.getDefault();
//                            //send message to GP's number with textMsg string
//                            sms.sendTextMessage(c.getString(7), null, textMsg, null, null);
//                        }
//                        //if GP's number does not exist then tell user to add number and try again.
//                        else {
//                            Toast.makeText(getBaseContext(), "Please enter GP information on 'Your Details' page then re-enter this information", Toast.LENGTH_LONG).show();
//                        }
//                        //close databsae
//                        db.close();
//
//
//                    }
                    //if error occurs and user does not add values to text fields
                } catch (Exception e) {

                    Toast.makeText(getBaseContext(), "Please Use Int Values", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}
