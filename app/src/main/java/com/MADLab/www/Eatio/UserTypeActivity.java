package com.MADLab.www.Eatio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Select whether to Login with ID or as a Guest or Sign Up
 */

public class UserTypeActivity extends AppCompatActivity {

    public static Activity userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userType = this;
    }

    public void onLoginButton(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void onGuestButton(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onSignUpButton(View view) {
        startActivity(new Intent(this, RegisterUserActivity.class));
    }
}
