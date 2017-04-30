package com.picassoft.mymedicare;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateAccount extends AppCompatActivity implements OnClickListener {

    int errorCount;
    myMediCareDB db;
    //boolean loginnable = true;

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
                    errorCount = 0;

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

                    if (TextUtils.isEmpty(nameStr)) {
                        name.setError("Enter your Name");
                        errorCount++;
                    }
                    if (TextUtils.isEmpty(emailStr)) {
                        email.setError("Enter your Email");
                        errorCount++;
                    }
                    if (TextUtils.isEmpty(pass1Str)){
                        pass1.setError("Enter your Password");
                        errorCount++;
                    }
                    if (TextUtils.isEmpty(passConfStr)) {
                        passConf.setError("Repeat your Password");
                        errorCount++;
                    }
                    if (TextUtils.isEmpty(gpNumStr)) {
                        gpNum.setError("Enter your GPs number");
                        errorCount++;
                    }

                    if (!pass1Str.equals(passConfStr)) {
                        pass1.setError("Passwords do not Match");
                        passConf.setError("Passwords do not Match");
                        Toast passFailed = Toast.makeText(CreateAccount.this, "Passwords Don't Match!", Toast.LENGTH_SHORT);
                        passFailed.show();
                    }

                    if((pass1Str.equals(passConfStr)) && errorCount == 0){
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
                    } else {
                        Toast.makeText(CreateAccount.this, errorCount + " Empty Fields", Toast.LENGTH_SHORT).show();
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
