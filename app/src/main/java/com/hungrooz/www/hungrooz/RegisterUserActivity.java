package com.hungrooz.www.hungrooz;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Sign Up Form
 */

public class RegisterUserActivity extends AppCompatActivity {

    private EditText nameText;
    private EditText phoneText;
    private EditText passwordText;
    private EditText confirmPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameText = (EditText) findViewById(R.id.sign_up_name);
        phoneText = (EditText) findViewById(R.id.sign_up_phone);
        passwordText = (EditText) findViewById(R.id.sign_up_password);
        confirmPasswordText = (EditText) findViewById(R.id.sign_up_confirm_password);
    }

    public void onRegister(View view) {
        String name = nameText.getText().toString();
        String phone = phoneText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();

        if (name.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "All fields are required to be filled", Toast.LENGTH_SHORT).show();
        } else if (phone.length() < 10) {
            Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        } else if (isInternetConnected()) {
            addCustomer(name, phone, password);
        } else {
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private void addCustomer(String Cname, String Cphone, String Cpassword) {
//        button.setEnabled(false);
        AddCustomer as = new AddCustomer(Cname, Cphone, Cpassword);
        as.execute();
    }

    private class AddCustomer extends AsyncTask<Void, Void, String> {
        String name;
        String phone;
        String password;
        private ProgressDialog loading;

        AddCustomer(final String Cname, final String Cphone, final String Cpassword) {
            name = Cname;
            phone = Cphone;
            password = Cpassword;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(RegisterUserActivity.this, "Signing Up", "Wait...",
                    false, false);
        }

        @Override
        protected void onPostExecute(String s) {  // s is the acknowledgment(echo) received from php
            super.onPostExecute(s);
            loading.dismiss();
            Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
            RegisterUserActivity.this.finish();
        }

        @Override
        protected String doInBackground(Void... v) {

            // Details sent to php server
            HashMap<String, String> params = new HashMap<>();
            params.put(Config.KEY_CUSTOMER_NAME, name);
            params.put(Config.KEY_CUSTOMER_PHONE, phone);
            params.put(Config.KEY_CUSTOMER_PASSWORD, password);

            RequestHandler rh = new RequestHandler();
            return rh.sendPostRequest(Config.URL_ADD_CUSTOMER, params);
        }
    }

    public boolean isInternetConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
