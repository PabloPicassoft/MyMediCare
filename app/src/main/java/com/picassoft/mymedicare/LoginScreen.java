package com.picassoft.mymedicare;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginScreen extends AppCompatActivity {

    //public myMediCareDB db = new myMediCareDB(getBaseContext());



    //ID to identity READ_CONTACTS permission request.
    private static final int REQUEST_READ_CONTACTS = 0;

    private EditText mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

//                    EditText uEmail = (EditText) findViewById(R.id.login_email);
//                    String enteredEmail = uEmail.getText().toString();
//                    EditText uPass = (EditText) findViewById(R.id.login_password);
//                    String enteredPass = uPass.getText().toString();
//
//                    String password = db.searchUser(enteredEmail);

                    //if (enteredPass.equals(password)){
                        Intent loginClick = new Intent(LoginScreen.this, NavDrawer.class);
                        //signUpClick.putExtra("Username", password);
                        startActivity(loginClick);
//                    } else {
//                        Toast loginFailed = Toast.makeText(LoginScreen.this, "Email and Password Do Not Match!", Toast.LENGTH_SHORT);
//                        loginFailed.show();
//                    }
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

    private boolean mayRequestContacts() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }




}



