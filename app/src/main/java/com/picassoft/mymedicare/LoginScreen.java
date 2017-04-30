package com.picassoft.mymedicare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginScreen extends AppCompatActivity {

    public myMediCareDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        db = new myMediCareDB(getBaseContext());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_login_screen);

        TextView tv =(TextView)findViewById(R.id.WrittenLogo);

        tv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent dbmanager = new Intent(LoginScreen.this,AndroidDatabaseManager.class);
                startActivity(dbmanager);
            }
        });

        final Button login = (Button) findViewById(R.id.btn_login);
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_login) {

                    EditText uEmail = (EditText) findViewById(R.id.login_email);
                    String enteredEmail = uEmail.getText().toString();
                    EditText uPass = (EditText) findViewById(R.id.login_password);
                    String enteredPass = uPass.getText().toString();


                    //
                    try {
                        db.open();
                        String password = db.loginAuth(enteredEmail);
                        db.close();

                        if(TextUtils.isEmpty(enteredEmail)) {
                            uEmail.setError("Email Required");
                        }
                        if(TextUtils.isEmpty(enteredPass)) {
                            uPass.setError("Password Required");
                        }

                        if (enteredPass.equals(password)){
                            
                            Toast.makeText(LoginScreen.this, "Logged in successfully as " + enteredEmail + ".", Toast.LENGTH_LONG).show();

                            Intent loginClick = new Intent(LoginScreen.this, NavDrawer.class);
                            startActivity(loginClick);
                        } else if (!enteredPass.equals(password) && (!TextUtils.isEmpty(enteredEmail)) && (!TextUtils.isEmpty(enteredPass))) {
                            Toast.makeText(LoginScreen.this, "Email and Password Do Not Match!", Toast.LENGTH_SHORT).show();
                        }

                        // if there are no accounts in the database, the cursor index will be out of bounds (0) therefore throwing an exception.
                        // inform user to register before clicking login.
                    } catch (CursorIndexOutOfBoundsException noAccounts){
                        Toast failed = Toast.makeText(LoginScreen.this, "Please Register an Email by Creating an Account.", Toast.LENGTH_SHORT);
                        failed.show();
                    }
                }
            }
        });

        Button signUp = (Button) findViewById(R.id.btn_loginSCR_signup);
        signUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_loginSCR_signup){
                    Intent loginClick = new Intent(LoginScreen.this, CreateAccount.class);
                    startActivity(loginClick);
                }
            }
        });

    }

    @Override
    public  void onBackPressed(){

    }
}



