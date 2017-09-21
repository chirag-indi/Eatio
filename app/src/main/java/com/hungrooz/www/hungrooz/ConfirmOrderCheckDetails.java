package com.hungrooz.www.hungrooz;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmOrderCheckDetails extends Fragment {

    SharedPreferences sharedpreferences;
    SharedPreferences sharedPreferences2;
    EditText dobText;
    Calendar myCalendar = Calendar.getInstance();
    Button checkDetailsNextButton;

    EditText nameText;
    EditText phoneText;
    EditText emailText;
    EditText addressText;
    RadioGroup genderRadio;

    public ConfirmOrderCheckDetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_confirm_order_check_details, container, false);

        nameText = (EditText) rootView.findViewById(R.id.confirm_check_details_name);
        phoneText = (EditText) rootView.findViewById(R.id.confirm_check_details_phone_number);
        emailText = (EditText) rootView.findViewById(R.id.confirm_check_details_email);
        dobText = (EditText) rootView.findViewById(R.id.confirm_check_details_dob);
        addressText = (EditText) rootView.findViewById(R.id.confirm_check_details_address);
        genderRadio = (RadioGroup) rootView.findViewById(R.id.confirm_check_details_gender);
        checkDetailsNextButton = (Button) rootView.findViewById(R.id.confirm_check_details_next_button);

        sharedpreferences = getActivity().getSharedPreferences(Config.sharedLoginPREFERENCES,
                Context.MODE_PRIVATE);
        sharedPreferences2 = getActivity()
                .getSharedPreferences(Config.tempCustomerPreferences, Context.MODE_PRIVATE);

        String name = sharedpreferences.getString(Config.sharedName, "");
        String phone = sharedpreferences.getString(Config.sharedPhone, "");
        String email = sharedpreferences.getString(Config.sharedEmail, "");
        String gender = sharedpreferences.getString(Config.sharedGender, "");
        String dob = sharedpreferences.getString(Config.sharedDOB, "");
        String address = sharedpreferences.getString(Config.sharedAddress, "");

        String name2, phone2, email2, gender2, dob2, address2;
        if (phone.equals("")) {
            name2 = sharedPreferences2.getString(Config.tempSharedName, "");
            phone2 = sharedPreferences2.getString(Config.tempSharedPhone, "");
            email2 = sharedPreferences2.getString(Config.tempSharedEmail, "");
            gender2 = sharedPreferences2.getString(Config.tempSharedGender, "");
            dob2 = sharedPreferences2.getString(Config.tempSharedDob, "");
            address2 = sharedPreferences2.getString(Config.tempSharedAddress, "");
        } else {
            name2 = name;
            phone2 = phone;
            email2 = email;
            gender2 = gender;
            dob2 = dob;
            address2 = address;
        }

        nameText.setText(name2);
        emailText.setText(email2);
        phoneText.setText(phone2);
        dobText.setText(dob2);
        addressText.setText(address2);

        if (gender2.equals(getString(R.string.profile_string_ms))) {
            genderRadio.check(R.id.profile_gender_ms);
        } else if (gender2.equals(getString(R.string.profile_string_mrs))) {
            genderRadio.check(R.id.profile_gender_mrs);
        }

        if (phone.equals("")) {
            phoneText.setFocusable(true);
            phoneText.setFocusableInTouchMode(true);
        }

        dobText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        checkDetailsNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name3 = nameText.getText().toString();
                String phone3 = phoneText.getText().toString();
                String email3 = emailText.getText().toString();
                String dob3 = dobText.getText().toString();
                RadioButton genderRadioButton = (RadioButton) rootView.findViewById(genderRadio.getCheckedRadioButtonId());
                String gender3 = genderRadioButton.getText().toString();
                String address3 = addressText.getText().toString();

                if (name3.isEmpty() || phone3.isEmpty() || email3.isEmpty() || dob3.isEmpty() ||
                        gender3.isEmpty() || address3.isEmpty()) {
                    Toast.makeText(getContext(), "All fields are required to be filled", Toast.LENGTH_SHORT).show();
                } else {
                    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                    GetCustomerOrderCount gcoc = new GetCustomerOrderCount(phone3, date);
                    gcoc.execute();

                    saveCustomerDetails(name3, phone3, email3, dob3, gender3, address3);

                    Fragment fragment = null;
                    Class fragmentClass;
                    FragmentManager fragmentManager;
                    FragmentTransaction transaction;

                    fragmentClass = ConfirmOrderBillFragment.class;
                    try {
                        fragment = (Fragment) fragmentClass.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    fragmentManager = getActivity().getSupportFragmentManager();
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.confirm_order_content, fragment);
                    transaction.commit();
                }
            }
        });

        return rootView;
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

    public void saveCustomerDetails(String name, String phone, String email, String dob,
                                    String gender, String address) {
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        editor.putString(Config.tempSharedName, name);
        editor.putString(Config.tempSharedPhone, phone);
        editor.putString(Config.tempSharedEmail, email);
        editor.putString(Config.tempSharedGender, gender);
        editor.putString(Config.tempSharedDob, dob);
        editor.putString(Config.tempSharedAddress, address);
        editor.apply();
    }


    private class GetCustomerOrderCount extends AsyncTask<Void, Void, String> {
        private ProgressDialog loading;
        private String phone;
        private String date;

        private GetCustomerOrderCount(final String Cphone, final String Cdate) {
            phone = Cphone;
            date = Cdate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(getContext(), "Connecting...", "Wait...", false, false);
        }

        @Override
        protected void onPostExecute(String count) {  // json is the JSON String received
            super.onPostExecute(count);
            loading.dismiss();
            SharedPreferences.Editor editor = sharedPreferences2.edit();
            editor.putString(Config.tempCountPreviousOrder, count);
            editor.apply();
        }

        @Override
        protected String doInBackground(Void... v) {
            HashMap<String, String> params = new HashMap<>();
            params.put(Config.KEY_CUSTOMER_PHONE, phone);
            params.put("date", date);

            RequestHandler rh = new RequestHandler();
            return rh.sendPostRequest(Config.URL_CUSTOMER_ORDER_COUNT, params);
        }
    }
}
