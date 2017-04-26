package com.picassoft.mymedicare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginScreen extends AppCompatActivity {

    public myMediCareDB db;

    //ID to identity READ_CONTACTS permission request.
    private static final int REQUEST_READ_CONTACTS = 0;

    private EditText mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        db = new myMediCareDB(getBaseContext());

        //remove title bar for better look, set content view afterwards to avoid runtime error
        ////this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ////fullscreen?
        ////this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login_screen);

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

                        if (enteredPass.equals(password)){
                            
                            Toast loginSucceeded = Toast.makeText(LoginScreen.this, "Logged in successfully as " + enteredEmail + ".", Toast.LENGTH_LONG);
                            loginSucceeded.show();

//                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginScreen.this);
//                            //int h = 0;
//                            int userPosition = preferences.getInt("positionCount", 0);
//                            Toast loginSucceed = Toast.makeText(LoginScreen.this, "name = " +  userPosition, Toast.LENGTH_LONG);
//                            loginSucceed.show();

                            Intent loginClick = new Intent(LoginScreen.this, NavDrawer.class);
                            startActivity(loginClick);
                        } else {
                            Toast loginFailed = Toast.makeText(LoginScreen.this, "Email and Password Do Not Match!", Toast.LENGTH_SHORT);
                            loginFailed.show();
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



