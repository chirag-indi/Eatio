package com.hungrooz.www.hungrooz;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;



public class ConfirmOrderPayFragment extends Fragment {

    SharedPreferences sharedpreferences;
    SharedPreferences sharedPreferences2;
    String orderId;
    String phone;
    String phone2;
    String date;
    Button confirmButton;

    public ConfirmOrderPayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_confirm_order_pay, container, false);

        confirmButton = (Button) rootView.findViewById(R.id.order_confirm_button);
        sharedpreferences = getContext().getSharedPreferences(Config.sharedLoginPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences2 = getContext().getSharedPreferences(Config.tempCustomerPreferences, Context.MODE_PRIVATE);
        phone = sharedpreferences.getString(Config.sharedPhone, "");
        phone2 = sharedPreferences2.getString(Config.tempSharedPhone, "");
        String previousOrderCount = sharedPreferences2.getString(Config.tempCountPreviousOrder, "");

        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String[] d = date.split("-");
        orderId = d[2].substring(2) + d[1] + d[0];

        if (phone.equals("")) {
            orderId = orderId + phone2 + String.valueOf(Integer.parseInt(previousOrderCount) + 1);
            SharedPreferences.Editor editor2 = sharedPreferences2.edit();
            editor2.putString(Config.finalOrderIdTag, orderId);
            editor2.apply();
        } else {
            orderId = orderId + phone2 + String.valueOf(Integer.parseInt(previousOrderCount) + 1);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(Config.finalOrderIdTag, orderId);
            editor.apply();
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton codRadio = (RadioButton) getActivity().findViewById(R.id.payment_cod_radio);
                String paymentMode = codRadio.getText().toString();
                String promoCode = "";
                String address = sharedPreferences2.getString(Config.tempSharedAddress, "");

                if (isInternetConnected()) {
                    if (phone.equals("")) {
                        DatabaseHandler db = new DatabaseHandler(getContext());
                        int itemCount = db.getCartItemCount();
                        String[][] orderCart = db.readFromCartDB();
                        int totalPrice = 0;
                        String[] order = new String[itemCount];
                        String[] store = new String[itemCount];
                        String[] quantity = new String[itemCount];
                        String[] price = new String[itemCount];

                        for (int i = 0; i < itemCount; i++) {
                            order[i] = orderCart[i][0];
                            store[i] = orderCart[i][1];
                            quantity[i] = orderCart[i][2];
                            price[i] = orderCart[i][3];

                            totalPrice += (Integer.parseInt(quantity[i]) * Integer.parseInt(price[i]));
                        }
                        String totalAmount = String.valueOf(totalPrice);

                        CompleteCustomerOrder cco = new CompleteCustomerOrder(phone2, orderId,
                                totalAmount, date, address, paymentMode, promoCode, store, order, quantity,
                                price, false);
                        cco.execute();

                    } else {
                        String totalBill = sharedPreferences2.getString(Config.tempTotalPrice, "");
                        CompleteCustomerOrder cco = new CompleteCustomerOrder(phone, orderId,
                                totalBill, date, address, paymentMode, promoCode, null, null, null, null, true);
                        cco.execute();
                    }

                } else {
                    Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });
        return rootView;
    }

    public boolean isInternetConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    private class CompleteCustomerOrder extends AsyncTask<Void, Void, String> {
        String phone;
        String order_id;
        String total_price;
        String time;
        String address;
        String payment_mode;
        String promo_code;
        String[] store_name;
        String[] order_item;
        String[] item_count;
        String[] price;
        boolean isRegistered;
        private ProgressDialog loading;

        CompleteCustomerOrder(final String cphone, final String corder_id,
                              final String ctotal_price, final String ctime,
                              final String caddress, final String cpayment_mode,
                              final String cpromo_code, final String[] cstore_name,
                              final String[] corder_item, final String[] citem_count,
                              final String[] cprice, final boolean cisRegistered) {
            phone = cphone;
            order_id = corder_id;
            total_price = ctotal_price;
            time = ctime;
            address = caddress;
            payment_mode = cpayment_mode;
            promo_code = cpromo_code;
            order_id = corder_id;
            store_name = cstore_name;
            order_item = corder_item;
            item_count = citem_count;
            price = cprice;
            isRegistered = cisRegistered;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(getContext(), "Connecting", "Wait...",
                    false, false);
        }

        @Override
        protected void onPostExecute(String s) {  // s is the acknowledgment(echo) received from php
            super.onPostExecute(s);
            loading.dismiss();
            Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
            CartActivity.activity.finish();
            getActivity().finish();
        }

        @Override
        protected String doInBackground(Void... v) {

            // Details sent to php server
            HashMap<String, String> params = new HashMap<>();
            params.put(Config.KEY_CONFIRM_ORDER_PHONE, phone);
            params.put(Config.KEY_CONFIRM_ORDER_ORDER_ID, order_id);
            params.put(Config.KEY_CONFIRM_ORDER_TOTAL_PRICE, total_price);
            params.put(Config.KEY_CONFIRM_ORDER_TIME, time);
            params.put(Config.KEY_CONFIRM_ORDER_ADDRESS, address);
            params.put(Config.KEY_CONFIRM_ORDER_PAYMENT_MODE, payment_mode);
            params.put(Config.KEY_CONFIRM_ORDER_PROMO_CODE, promo_code);

            RequestHandler rh = new RequestHandler();
            String result = rh.sendPostRequest(Config.URL_ORDER_CONFIRM, params);

            if (isRegistered) {
                HashMap<String, String> params2 = new HashMap<>();
                params2.put(Config.KEY_CONFIRM_ORDER_PHONE, phone);
                params2.put(Config.KEY_CONFIRM_ORDER_ORDER_ID, order_id);
                RequestHandler rh2 = new RequestHandler();
                String s = rh2.sendPostRequest(Config.URL_REGISTERED_ORDER_CONFIRM_DETAILS, params2);
            } else {
                int n = 0;
                if (store_name != null) {
                    n = store_name.length;
                }
                HashMap<String, String> params2 = new HashMap<>();
                for (int i = 0; i < n; i++) {
                    params2.put(Config.KEY_CONFIRM_ORDER_ORDER_ID, order_id);
                    params2.put(Config.KEY_CONFIRM_ORDER_STORE_NAME, store_name[i]);
                    params2.put(Config.KEY_CONFIRM_ORDER_ORDER_ITEM, order_item[i]);
                    params2.put(Config.KEY_CONFIRM_ORDER_ITEM_COUNT, item_count[i]);
                    params2.put(Config.KEY_CONFIRM_ORDER_PRICE, price[i]);

                    RequestHandler rh2 = new RequestHandler();
                    rh2.sendPostRequest(Config.URL_GUEST_ORDER_CONFIRM_DETAILS, params2);
                }
            }
            return result;
        }
    }
}
