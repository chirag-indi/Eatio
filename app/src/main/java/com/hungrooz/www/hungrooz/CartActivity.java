package com.hungrooz.www.hungrooz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    public static Activity activity;

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<CartItem> cartList;
    private boolean isCartEmpty = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activity = this;

        Button fab = (Button) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetConnected()) {
                    if (!isCartEmpty) {
                        startActivity(new Intent(getBaseContext(), ConfirmOrderActivity.class));
                    } else {
                        Toast.makeText(getBaseContext(), "Cart is Empty", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.cart_recycler_view);

        cartList = new ArrayList<>();
        adapter = new CartAdapter(this, cartList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.sharedLoginPREFERENCES,
                Context.MODE_PRIVATE);
        String phone = sharedPreferences.getString(Config.sharedPhone, "");
        if (phone.equals("")) {
            prepareGuestCart();
        } else {
            prepareCart(phone);
        }
    }

    private void prepareGuestCart() {
        DatabaseHandler db = new DatabaseHandler(this);
        String[][] orderCart = db.readFromCartDB();

        int n = db.getCartItemCount();
        if (n > 0) {
            isCartEmpty = false;
        }

        for (int i = 0; i < n; i++) {
            String order_item = orderCart[i][0];
            String store = orderCart[i][1];
            int quantity = Integer.parseInt(orderCart[i][2]);
            int price = Integer.parseInt(orderCart[i][3]);

            CartItem cartItem = new CartItem(order_item, store, quantity, price);
            cartList.add(cartItem);
            adapter.notifyDataSetChanged();
        }

    }

    private void prepareCart(String phone) {
        GetCartDetails getCartDetails = new GetCartDetails(phone);
        getCartDetails.execute();
    }

    public boolean isInternetConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private class GetCartDetails extends AsyncTask<Void, Void, String> {
        private ProgressDialog loading;
        private String phone;

        private GetCartDetails(final String Cphone) {
            phone = Cphone;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(CartActivity.this, "Fetching...", "Wait...",
                    false, false);
        }

        @Override
        protected void onPostExecute(String json) {  // json is the JSON String received
            super.onPostExecute(json);
            loading.dismiss();
            int cartIemCount = 0;

            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
                JSONObject c = result.getJSONObject(0);
                cartIemCount = Integer.parseInt(c.getString(Config.KEY_CART_ITEM_COUNT));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            String[][] arrayResult = new String[cartIemCount + 1][4];
            String[] arrayTag = {Config.KEY_CART_ORDER_ITEM, Config.KEY_CART_STORE,
                    Config.KEY_CART_QUANTITY, Config.KEY_CART_PRICE};
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

                for (int j = 1; j <= cartIemCount; j++) {
                    JSONObject c = result.getJSONObject(j);
                    for (int i = 0; i < 4; i++) {
                        arrayResult[j][i] = c.getString(arrayTag[i]);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (cartIemCount == 0) {
                Toast.makeText(getBaseContext(), "There Are No Items in Cart", Toast.LENGTH_SHORT).show();
            } else {
                isCartEmpty = false;
                for (int i = 1; i <= cartIemCount; i++) {

                    int quantity = Integer.parseInt(arrayResult[i][2]);
                    int price = Integer.parseInt(arrayResult[i][3]);

                    CartItem cartItem = new CartItem(arrayResult[i][0], arrayResult[i][1],
                            quantity, price);
                    cartList.add(cartItem);
                    adapter.notifyDataSetChanged();
                }
            }
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
