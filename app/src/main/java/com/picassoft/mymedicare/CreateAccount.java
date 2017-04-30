package com.picassoft.mymedicare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateAccount extends AppCompatActivity implements OnClickListener {

    myMediCareDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        db = new myMediCareDB(getBaseContext());

        TextView bckToLogin = (TextView) findViewById(R.id.link_login);
        bckToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.link_login) {
                    Intent loginClick = new Intent(CreateAccount.this, LoginScreen.class);
                    startActivity(loginClick);
                }

            }
        });

        Button signUp = (Button) findViewById(R.id.btn_signup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_signup) {
                    EditText name = (EditText) findViewById(R.id.signup_name);
                    EditText email = (EditText) findViewById(R.id.signup_email);
                    EditText pass1 = (EditText) findViewById(R.id.signup_password1);
                    EditText passConf = (EditText) findViewById(R.id.confirm_password);
                    EditText gpNum = (EditText) findViewById(R.id.signup_gp_phone);

                    String nameStr = name.getText().toString();
                    String emailStr = email.getText().toString();
                    String pass1Str = pass1.getText().toString();
                    String passConfStr = passConf.getText().toString();
                    String gpNumStr = gpNum.getText().toString();

                    if (!pass1Str.equals(passConfStr)) {
                        Toast passFailed = Toast.makeText(CreateAccount.this, "Passwords Don't Match!", Toast.LENGTH_SHORT);
                        passFailed.show();
                    } else {

                        //Toast checkNumber = Toast.makeText(CreateAccount.this, "number: " + gpNumStr, Toast.LENGTH_SHORT);
                        //checkNumber.show();


                        User user = new User();

                        user.setName(nameStr);
                        user.setEmail(emailStr);
                        user.setPassword(passConfStr);
                        user.setGpNumber(gpNumStr);

                        db.open();
                        db.insertUser(user);
                        db.close();

                        Toast loginSucceeded = Toast.makeText(CreateAccount.this, "Account: \"" + emailStr + "\" has been created.", Toast.LENGTH_LONG);
                        loginSucceeded.show();

                        Intent signUpClick = new Intent(CreateAccount.this, LoginScreen.class);
                        startActivity(signUpClick);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        //do nothing
    }

    @Override
    public void onClick(View v) {

    }
}
