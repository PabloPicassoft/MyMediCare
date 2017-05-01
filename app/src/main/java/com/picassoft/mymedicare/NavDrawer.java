package com.picassoft.mymedicare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



public class NavDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    myMediCareDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        db = new myMediCareDB(getBaseContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(NavDrawer.this);
        int h = 0;
        int userPosition = preferences.getInt("positionCount", h);

        db.open();
        Cursor cursor = db.getAccount(userPosition);
        db.close();

        String colour = cursor.getString(5);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.content_nav_drawer);
        relativeLayout.setBackgroundColor(Color.parseColor(colour));

        final Button calcRisk = (Button) findViewById(R.id.main_calculate_risk);
        calcRisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.main_calculate_risk) {
                    startActivity(new Intent(NavDrawer.this, CalculateRisk.class));
                }
            }
        });

        final Button pastMeasure = (Button) findViewById(R.id.main_past_measurements);
        pastMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.main_past_measurements) {
                    startActivity(new Intent(NavDrawer.this, PastMeasurements.class));
                }
            }
        });

        final Button settings = (Button) findViewById(R.id.main_settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.main_settings) {
                    startActivity(new Intent(NavDrawer.this, Settings.class));
                }
            }
        });

        final Button logout = (Button) findViewById(R.id.button_main_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.button_main_logout) {
                    startActivity(new Intent(NavDrawer.this, LoginScreen.class));
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
//        do nothing to disable going back to login
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, Settings.class);
            startActivity(settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_calculate) {
            Intent toCalc = new Intent(this, CalculateRisk.class);
            startActivity(toCalc);
        } else if (id == R.id.nav_logout) {
            Intent logout = new Intent(this, LoginScreen.class);
            startActivity(logout);
        } else if (id == R.id.nav_past_measurements) {
            Intent profile = new Intent(this, PastMeasurements.class);
            startActivity(profile);
        } else if (id == R.id.nav_settings) {
            Intent profile = new Intent(this, Settings.class);
            startActivity(profile);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
