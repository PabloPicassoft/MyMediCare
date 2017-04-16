package com.picassoft.mymedicare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Button signUp = (Button) findViewById(R.id.btn_signup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_signup) {
                    Intent signUpClick = new Intent(CreateAccount.this, NavDrawer.class);
                    startActivity(signUpClick);
                }
            }
        });

        TextView bckToLogin = (TextView) findViewById(R.id.link_login);
        bckToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.link_login){
                    Intent loginClick = new Intent(CreateAccount.this, LoginScreen.class);
                    startActivity(loginClick);
                }
            }
        });
    }


}
