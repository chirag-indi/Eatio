package com.MADLab.www.Eatio;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class YourOrdersFragment extends Fragment {

    private ArrayAdapter<String> mYourOrdersAdapter;
    SharedPreferences sharedPreferences;

    public YourOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_your_orders, container, false);

        sharedPreferences = getContext().getSharedPreferences(Config.sharedLoginPREFERENCES,
                Context.MODE_PRIVATE);
        mYourOrdersAdapter =
                new ArrayAdapter<>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_your_orders, // The name of the layout ID.
                        R.id.list_item_your_orders_textView, // The ID of the text view to populate.
                        new ArrayList<String>());

        ListView listView = (ListView) rootView.findViewById(R.id.list_view_your_orders);
        listView.setAdapter(mYourOrdersAdapter);

        return rootView;
    }

    public void updateYourOrders() {
        String phone = sharedPreferences.getString(Config.sharedPhone, "");
        GetOrderDetails getOrderDetails = new GetOrderDetails(phone);
        getOrderDetails.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateYourOrders();
    }

    private class GetOrderDetails extends AsyncTask<Void, Void, String> {
        private ProgressDialog loading;
        private String phone;

        private GetOrderDetails (final String cphone) {
            phone = cphone;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(getContext(), "Connecting...", "Wait...",
                    false, false);
        }

        @Override
        protected void onPostExecute(String json) {  // json is the JSON String received
            super.onPostExecute(json);
            loading.dismiss();
            int orderCount = 0;

            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
                JSONObject c = result.getJSONObject(0);
                orderCount = Integer.parseInt(c.getString(Config.KEY_CART_ITEM_COUNT));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            String[][] arrayResult = new String[orderCount][4];
            String[] arrayTag = {Config.KEY_CONFIRM_ORDER_ORDER_ID, Config.KEY_CONFIRM_ORDER_TOTAL_PRICE,
                    Config.KEY_CONFIRM_ORDER_TIME};
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

                for (int j = 0; j < orderCount; j++) {
                    JSONObject c = result.getJSONObject(j+1);
                    for (int i = 0; i < 3; i++) {
                        arrayResult[j][i] = c.getString(arrayTag[i]);
                        Log.d("Value "+i, arrayResult[j][i]);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (orderCount == 0) {
                Toast.makeText(getContext(), "You haven't ordered yet", Toast.LENGTH_SHORT).show();
            } else {
                String[] result = new String[orderCount];
                for (int i = 0; i < orderCount; i++) {
                    result[i] = "\n\nORDER ID:    #" + arrayResult[i][0] + "\n\nTOTAL BILL:    \u20B9 " +
                            arrayResult[i][1] + "\n\nDATE:    " + arrayResult[i][2]+"\n";
                    Log.d("Orders", result[i]);
                }
                if (result!=null) {
                    mYourOrdersAdapter.clear();
                    for(String i: result) {
                        mYourOrdersAdapter.add(i);
                    }
                }
            }
        }

        @Override
        protected String doInBackground(Void... v) {
            HashMap<String, String> params = new HashMap<>();
            params.put(Config.KEY_CUSTOMER_PHONE, phone);

            RequestHandler rh = new RequestHandler();
            return rh.sendPostRequest(Config.URL_YOUR_ORDERS, params);
        }
    }
}
