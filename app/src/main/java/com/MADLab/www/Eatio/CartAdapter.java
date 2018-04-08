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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private Context mContext;
    List<CartItem> cartItemList;
    SharedPreferences sharedPreferences;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView orderItem;
        public TextView storeName;
        public TextView orderPrice;
        public TextView orderQuantity;
        public Button removeOrderButton;

        public MyViewHolder(View view) {
            super(view);
            orderItem = (TextView) view.findViewById(R.id.cart_card_order_item);
            storeName = (TextView) view.findViewById(R.id.cart_card_store_name);
            orderQuantity = (TextView) view.findViewById(R.id.cart_card_quantity);
            orderPrice = (TextView) view.findViewById(R.id.cart_card_price);
            removeOrderButton = (Button) view.findViewById(R.id.cart_card_remove_button);
        }
    }

    public CartAdapter(Context mContext, List<CartItem> cartItemList) {
        this.mContext = mContext;
        this.cartItemList = cartItemList;
    }

    public boolean isInternetConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CartItem cartItem = cartItemList.get(position);
        final String order = cartItem.getOrderItem();
        final String store = cartItem.getStore();
        final String quantity = String.valueOf(cartItem.getQuantity());
        final String price = String.valueOf(cartItem.getPrice());
        String s = "\u20B9" + price;

        sharedPreferences = mContext.getSharedPreferences(Config.sharedLoginPREFERENCES, Context.MODE_PRIVATE);

        holder.orderItem.setText(order);
        holder.storeName.setText(store);
        holder.orderQuantity.setText(quantity);
        holder.orderPrice.setText(s);
        holder.removeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = sharedPreferences.getString(Config.sharedPhone, "");

                if (isInternetConnected()) {
                    if (phone.equals("")) {
                        DatabaseHandler db = new DatabaseHandler(mContext);
                        db.removeFromCart(order, store, quantity, price);
                        Toast.makeText(mContext, "Item removed from Cart", Toast.LENGTH_SHORT).show();
                    } else {
                        RemoveCartItem removeCartItem = new RemoveCartItem(phone, order, store, quantity, price);
                        removeCartItem.execute();
                    }
                    cartItemList.remove(position);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "Check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }


    private class RemoveCartItem extends AsyncTask<Void, Void, String> {
        private ProgressDialog loading;
        String order;
        String store;
        String quantity;
        String price;
        String phone;

        RemoveCartItem(final String Cphone, final String Corder, final String Cstore, final String Cquantity,
                       final String Cprice) {
            order = Corder;
            store = Cstore;
            quantity = Cquantity;
            price = Cprice;
            phone = Cphone;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(mContext, "Removing From Cart", "Wait...",
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
            params.put(Config.KEY_CART_PHONE, phone);
            params.put(Config.KEY_CART_ORDER_ITEM, order);
            params.put(Config.KEY_CART_STORE, store);
            params.put(Config.KEY_CART_QUANTITY, quantity);
            params.put(Config.KEY_CART_PRICE, price);

            RequestHandler rh = new RequestHandler();
            return rh.sendPostRequest(Config.URL_REMOVE_FROM_CART, params);
        }
    }
}
