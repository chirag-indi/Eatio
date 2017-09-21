package com.hungrooz.www.hungrooz;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    Calendar myCalendar = Calendar.getInstance();
    EditText dobText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText nameText = (EditText) findViewById(R.id.profile_name);
        EditText phoneText = (EditText) findViewById(R.id.profile_phone_number);
        EditText emailText = (EditText) findViewById(R.id.profile_email);
        dobText = (EditText) findViewById(R.id.profile_dob);
        EditText addressText = (EditText) findViewById(R.id.profile_address);
        RadioGroup genderRadio = (RadioGroup) findViewById(R.id.profile_gender);
        sharedpreferences = getSharedPreferences(Config.sharedLoginPREFERENCES,
                Context.MODE_PRIVATE);

        String name = sharedpreferences.getString(Config.sharedName, "");
        String phone = sharedpreferences.getString(Config.sharedPhone, "");
        String email = sharedpreferences.getString(Config.sharedEmail, "");
        String gender = sharedpreferences.getString(Config.sharedGender, "");
        String dob = sharedpreferences.getString(Config.sharedDOB, "");
        String address = sharedpreferences.getString(Config.sharedAddress, "");

        nameText.setText(name);
        emailText.setText(email);
        phoneText.setText(phone);
        dobText.setText(dob);
        addressText.setText(address);

        genderRadio.check(R.id.profile_gender_mr);
        if (gender.equals(getString(R.string.profile_string_ms))) {
            genderRadio.check(R.id.profile_gender_ms);
        } else if (gender.equals(getString(R.string.profile_string_mrs))) {
            genderRadio.check(R.id.profile_gender_mrs);
        }

        dobText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ProfileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dobText.setText(sdf.format(myCalendar.getTime()));
    }


    void updateCustomer(String Cname, String Cphone, String Cemail, String Cdob, String Caddress, String Cgender) {
        UpdateCustomer updateCustomer = new UpdateCustomer(Cname, Cphone, Cemail, Cdob, Caddress, Cgender);
        updateCustomer.execute();
    }


    // Profile Update Button
    public void onUpdateProfileButton(View view) {
        EditText nameText = (EditText) findViewById(R.id.profile_name);
        EditText phoneText = (EditText) findViewById(R.id.profile_phone_number);
        EditText emailText = (EditText) findViewById(R.id.profile_email);
        EditText dobText = (EditText) findViewById(R.id.profile_dob);
        EditText addressText = (EditText) findViewById(R.id.profile_address);

        RadioGroup genderRadio = (RadioGroup) findViewById(R.id.profile_gender);
        RadioButton genderRb = (RadioButton) findViewById(genderRadio.getCheckedRadioButtonId());

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String phone = phoneText.getText().toString();
        String dob = dobText.getText().toString();
        String address = addressText.getText().toString();
        String gender = genderRb.getText().toString();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || dob.isEmpty() ||
                address.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "All fields are required to be filled", Toast.LENGTH_SHORT).show();
        } else {
            updateCustomer(name, phone, email, dob, address, gender);

            SharedPreferences sharedPreferences = getSharedPreferences(Config.sharedLoginPREFERENCES,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Config.sharedName, name);
            editor.putString(Config.sharedEmail, email);
            editor.putString(Config.sharedGender, gender);
            editor.putString(Config.sharedDOB, dob);
            editor.putString(Config.sharedAddress, address);
            editor.apply();
        }
    }


    private class UpdateCustomer extends AsyncTask<Void, Void, String> {
        private ProgressDialog loading;
        String name;
        String email;
        String dob;
        String address;
        String gender;
        String phone;

        UpdateCustomer(final String Cname, final String Cphone, final String Cemail, final String Cdob,
                       final String Caddress, final String Cgender) {
            name = Cname;
            email = Cemail;
            dob = Cdob;
            address = Caddress;
            gender = Cgender;
            phone = Cphone;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(ProfileActivity.this, "Signing Up", "Wait...",
                    false, false);
        }

        @Override
        protected void onPostExecute(String s) {  // s is the acknowledgment(echo) received from php
            super.onPostExecute(s);
            loading.dismiss();
            Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(Void... v) {
            // Details sent to php server
            HashMap<String, String> params = new HashMap<>();
            params.put(Config.KEY_CUSTOMER_PHONE, phone);
            params.put(Config.KEY_CUSTOMER_NAME, name);
            params.put(Config.KEY_CUSTOMER_EMAIL, email);
            params.put(Config.KEY_CUSTOMER_DOB, dob);
            params.put(Config.KEY_CUSTOMER_ADDRESS, address);
            params.put(Config.KEY_CUSTOMER_GENDER, gender);

            RequestHandler rh = new RequestHandler();
            return rh.sendPostRequest(Config.URL_UPDATE_CUSTOMER, params);
        }
    }
}
