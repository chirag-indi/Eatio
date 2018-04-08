package com.MADLab.www.Eatio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Ayush894 on 25-04-2017.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {

    private Context mContext;
    List<MenuOrderItem> menuOrderItemList;

    private SharedPreferences sharedPreferences;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView orderItem;
        public TextView orderPrice;
        public TextView orderQuantity;
        public Button incrementButton;
        public Button decrementButton;
        public Button addToCartButton;

        public MyViewHolder(View view) {
            super(view);
            orderItem = (TextView) view.findViewById(R.id.card_order_item_textView);
            orderPrice = (TextView) view.findViewById(R.id.card_order_price_textView);
            orderQuantity = (TextView) view.findViewById(R.id.card_order_quantity_textView);
            incrementButton = (Button) view.findViewById(R.id.menu_increase_quantity_button);
            decrementButton = (Button) view.findViewById(R.id.menu_decrease_quantity_button);
            addToCartButton = (Button) view.findViewById(R.id.menu_add_to_cart_button);
        }
    }

    public MenuAdapter(Context mContext, List<MenuOrderItem> MenuOrderItemList) {
        this.mContext = mContext;
        this.menuOrderItemList = MenuOrderItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final MenuOrderItem menu = menuOrderItemList.get(position);
        holder.orderItem.setText(menu.getMenuItem());
        holder.orderQuantity.setText(String.valueOf(menu.getQuantity()));
        String s = "\u20B9" + menu.getPrice();
        holder.orderPrice.setText(s);

        holder.incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = Integer.parseInt(holder.orderQuantity.getText().toString());
                menu.setQuantity(++n);
                holder.orderQuantity.setText(String.valueOf(menu.getQuantity()));
            }
        });

        holder.decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = Integer.parseInt(holder.orderQuantity.getText().toString());
                menu.setQuantity(--n);
                holder.orderQuantity.setText(String.valueOf(menu.getQuantity()));
            }
        });

        sharedPreferences = mContext.getSharedPreferences(Config.sharedLoginPREFERENCES, Context.MODE_PRIVATE);

        holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = sharedPreferences.getString(Config.sharedPhone, "");
                String order = menu.getMenuItem();
                String store = menu.getStoreName();
                String quantity = String.valueOf(menu.getQuantity());
                String price = String.valueOf(menu.getPrice());

                if (isInternetConnected()) {
                    if (phone.equals("")) {
                        DatabaseHandler db = new DatabaseHandler(mContext);
                        db.addOrderToCartDB(order, store, quantity, price);
                        Toast.makeText(mContext, "Item added to Cart", Toast.LENGTH_SHORT).show();
                    } else {
                        addToCart(phone, order, store, quantity, price);
                    }
                } else {
                    Toast.makeText(mContext, "Check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuOrderItemList.size();
    }

    public boolean isInternetConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    private void addToCart(String Cphone, String Corder, String Cstore, String Cquantity, String Cprice) {
        AddItemToCart as = new AddItemToCart(Cphone, Corder, Cstore, Cquantity, Cprice);
        as.execute();
    }


    private class AddItemToCart extends AsyncTask<Void, Void, String> {
        private ProgressDialog loading;
        String phoneNo;
        String order;
        String store;
        String quantity;
        String price;

        AddItemToCart(final String CphoneNo, final String Corder, final String Cstore, final String Cquantity,
                      final String Cprice) {
            phoneNo = CphoneNo;
            order = Corder;
            store = Cstore;
            quantity = Cquantity;
            price = Cprice;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(mContext, "Adding to Cart", "Wait...",
                    false, false);
        }

        @Override
        protected void onPostExecute(String s) {  // s is the acknowledgment(echo) received from php
            super.onPostExecute(s);
            loading.dismiss();
            Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(Void... v) {

            // Details sent to php server
            HashMap<String, String> params = new HashMap<>();
            params.put(Config.KEY_CART_PHONE, phoneNo);
            params.put(Config.KEY_CART_ORDER_ITEM, order);
            params.put(Config.KEY_CART_STORE, store);
            params.put(Config.KEY_CART_QUANTITY, quantity);
            params.put(Config.KEY_CART_PRICE, price);

            RequestHandler rh = new RequestHandler();
            return rh.sendPostRequest(Config.URL_ADD_TO_CART, params);
        }
    }
}
