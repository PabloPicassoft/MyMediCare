package com.picassoft.mymedicare;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

public class Splashscreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 7000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getSupportActionBar().hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Splashscreen.this,MainActivity.class);
                Splashscreen.this.startActivity(mainIntent);
                Splashscreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}


