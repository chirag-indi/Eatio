package com.MADLab.www.Eatio;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    public MenuActivity() {
    }

    private String menuType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        menuType = intent.getStringExtra(Config.menuItemTag);

        Bundle bundle = new Bundle();
        bundle.putString(Config.menuItemTag, menuType);

        Fragment fragment = null;
        Class fragmentClass = MenuStoreTypeFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_order_menu, fragment, "menu");
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (isInternetConnected()) {
            startActivity(new Intent(this, CartActivity.class));
        }
        else {
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isInternetConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void onJLTTStoreButton(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(Config.menuItemTag, menuType);
        bundle.putString(Config.menuStoreTag, Config.menuStoreNameJLTT);

        Fragment fragment = null;
        Class fragmentClass = OrderMenuFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_order_menu, fragment, "menu");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void onFoodAddaStoreButton(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(Config.menuItemTag, menuType);
        bundle.putString(Config.menuStoreTag, Config.menuStoreNameFoodAdda);

        Fragment fragment = null;
        Class fragmentClass = OrderMenuFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_order_menu, fragment, "menu");
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
