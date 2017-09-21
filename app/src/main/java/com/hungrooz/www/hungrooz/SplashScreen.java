package com.hungrooz.www.hungrooz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Ayush894 on 11-03-2017.
 * Simple Splash Screen
 */

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedpreferences = getSharedPreferences(Config.sharedLoginPREFERENCES,
                Context.MODE_PRIVATE);
        final String phone = sharedpreferences.getString(Config.sharedPhone, "");

        int SPLASH_TIME_OUT = 2200;
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if (phone.equals("")) {
                    Intent i = new Intent(SplashScreen.this, UserTypeActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
