package com.hungrooz.www.hungrooz;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmOrderBillFragment extends Fragment {

    SharedPreferences sharedpreferences;
    SharedPreferences sharedPreferences2;

    TextView billItemName;
    TextView billItemPrice;
    TextView billTotalPrice;
    Button billNextButton;

    public ConfirmOrderBillFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_confirm_order_bill, container, false);

        sharedpreferences = getActivity().getSharedPreferences(Config.sharedLoginPREFERENCES,
                Context.MODE_PRIVATE);
        sharedPreferences2 = getActivity()
                .getSharedPreferences(Config.tempCustomerPreferences, Context.MODE_PRIVATE);

        String phone = sharedpreferences.getString(Config.sharedPhone, "");

        billItemName = (TextView) rootView.findViewById(R.id.bill_item_name);
        billItemPrice = (TextView) rootView.findViewById(R.id.bill_item_price);
        billTotalPrice = (TextView) rootView.findViewById(R.id.bill_total_amount_text);
        billNextButton = (Button) rootView.findViewById(R.id.bill_next_button);

        int itemCount;
        String itemName = "";
        String itemPrice = "";

        if (phone.equals("")) {
            DatabaseHandler db = new DatabaseHandler(getContext());
            itemCount = db.getCartItemCount();
            String[][] orderCart = db.readFromCartDB();
            int totalPrice = 0;

            for (int i = 0; i < itemCount; i++) {
                String order_item = orderCart[i][0];
                String store = orderCart[i][1];
                int quantity = Integer.parseInt(orderCart[i][2]);
                String price = orderCart[i][3];

                totalPrice = totalPrice + (quantity * Integer.parseInt(price));

                if (order_item.equals("Chotu Maharaja Burger(with Paneer)")) {
                    order_item = "Chotu Maharaja\nBurger(with Paneer)";
                    price = price + "\n";
                } else if (order_item.equals("Chotu Maharaja Burger(without Paneer)")) {
                    order_item = "Chotu Maharaja\nBurger(without Paneer)";
                    price = price + "\n";
                }

                itemName = itemName + order_item + "\n\n";
                itemPrice = itemPrice + quantity + "x" + price + "\n\n";

            }
            billItemName.setText(itemName);
            billItemPrice.setText(itemPrice);
            billTotalPrice.setText(String.valueOf(totalPrice));
        } else {
            GetCartDetails2 gcd = new GetCartDetails2(phone);
            gcd.execute();
        }

        final BottomNavigationView navigation = (BottomNavigationView) rootView.findViewById(R.id.navigation);

        billNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                Class fragmentClass;
                FragmentManager fragmentManager;
                FragmentTransaction transaction;

                fragmentClass = ConfirmOrderPayFragment.class;
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
        });
        return rootView;
    }


    private class GetCartDetails2 extends AsyncTask<Void, Void, String> {
        private ProgressDialog loading;
        private String phone;

        private GetCartDetails2(final String Cphone) {
            phone = Cphone;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(getContext(), "Fetching...", "Wait...", false, false);
        }

        @Override
        protected void onPostExecute(String json) {  // json is the JSON String received
            super.onPostExecute(json);
            loading.dismiss();
            int cartItemCount = 0;

            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
                JSONObject c = result.getJSONObject(0);
                cartItemCount = Integer.parseInt(c.getString(Config.KEY_CART_ITEM_COUNT));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            String[][] arrayResult = new String[cartItemCount + 1][4];
            String[] arrayTag = {Config.KEY_CART_ORDER_ITEM, Config.KEY_CART_STORE,
                    Config.KEY_CART_QUANTITY, Config.KEY_CART_PRICE};
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

                for (int j = 1; j <= cartItemCount; j++) {
                    JSONObject c = result.getJSONObject(j);
                    for (int i = 0; i < 4; i++) {
                        arrayResult[j][i] = c.getString(arrayTag[i]);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            int totalPrice = 0;
            String itemName = "";
            String itemPrice = "";

            for (int i = 1; i <= cartItemCount; i++) {
                String order_item = arrayResult[i][0];
                String store = arrayResult[i][1];
                int quantity = Integer.parseInt(arrayResult[i][2]);
                String price = arrayResult[i][3];

                totalPrice = totalPrice + (quantity * Integer.parseInt(price));

                if (order_item.equals("Chotu Maharaja Burger(with Paneer)")) {
                    order_item = "Chotu Maharaja\nBurger(with Paneer)";
                    price = price + "\n";
                } else if (order_item.equals("Chotu Maharaja Burger(without Paneer)")) {
                    order_item = "Chotu Maharaja\nBurger(without Paneer)";
                    price = price + "\n";
                }

                itemName = itemName + order_item + "\n\n";
                itemPrice = itemPrice + quantity + "x" + price + "\n\n";

            }
            billItemName.setText(itemName);
            billItemPrice.setText(itemPrice);
            billTotalPrice.setText(String.valueOf(totalPrice));
            SharedPreferences.Editor editor = sharedPreferences2.edit();
            editor.putString(Config.tempTotalPrice, String.valueOf(totalPrice));
            editor.apply();
        }

        @Override
        protected String doInBackground(Void... v) {
            HashMap<String, String> params = new HashMap<>();
            params.put(Config.KEY_CUSTOMER_PHONE, phone);

            RequestHandler rh = new RequestHandler();
            return rh.sendPostRequest(Config.URL_GET_CART_DETAILS, params);
        }
    }

}
