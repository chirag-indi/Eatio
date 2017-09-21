package com.hungrooz.www.hungrooz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedpreferences = getSharedPreferences(Config.sharedLoginPREFERENCES, Context.MODE_PRIVATE);
    }

    public void onSignInButton(View view) {
        EditText phoneText = (EditText) findViewById(R.id.sign_in_phone);
        EditText passwordText = (EditText) findViewById(R.id.sign_in_password);
        String phone = phoneText.getText().toString();
        String password = passwordText.getText().toString();

        if (phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required to be filled", Toast.LENGTH_SHORT).show();
        }

        if (isInternetConnected()) {
            GetCustomerDetails gcd = new GetCustomerDetails(phone, password);
            gcd.execute();
        } else {
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private class GetCustomerDetails extends AsyncTask<Void, Void, String> {
        private ProgressDialog loading;
        private String phone;
        private String password;

        private GetCustomerDetails(final String Cphone, final String Cpassword) {
            phone = Cphone;
            password = Cpassword;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(LoginActivity.this, "Connecting...", "Wait...",
                    false, false);
        }

        @Override
        protected void onPostExecute(String json) {  // json is the JSON String received
            super.onPostExecute(json);
            loading.dismiss();
            String[] arrayResult = new String[5];
            String[] arrayTag = {Config.KEY_CUSTOMER_NAME, Config.KEY_CUSTOMER_EMAIL,
                    Config.KEY_CUSTOMER_GENDER, Config.KEY_CUSTOMER_DOB, Config.KEY_CUSTOMER_ADDRESS};
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
                JSONObject c = result.getJSONObject(0);
                for (int i = 0; i < 5; i++) {
                    arrayResult[i] = c.getString(arrayTag[i]);
                    Log.d("Value " + i + ": ", arrayResult[i]);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (arrayResult[0].equals("0")) {
                Toast.makeText(getBaseContext(), "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
            } else {
                createSession(phone, arrayResult);
            }
        }

        @Override
        protected String doInBackground(Void... v) {
            HashMap<String, String> params = new HashMap<>();
            params.put(Config.KEY_CUSTOMER_PHONE, phone);
            params.put(Config.KEY_CUSTOMER_PASSWORD, password);

            RequestHandler rh = new RequestHandler();
            return rh.sendPostRequest(Config.URL_GET_CUSTOMER, params);
        }
    }

    public void createSession(String phone, String[] profile) {
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(Config.sharedName, profile[0]);
        editor.putString(Config.sharedPhone, phone);
        editor.putString(Config.sharedEmail, profile[1]);
        editor.putString(Config.sharedGender, profile[2]);
        editor.putString(Config.sharedDOB, profile[3]);
        editor.putString(Config.sharedAddress, profile[4]);
        editor.apply();

        UserTypeActivity.userType.finish();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();

    }

    public boolean isInternetConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
