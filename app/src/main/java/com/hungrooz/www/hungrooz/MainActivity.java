package com.hungrooz.www.hungrooz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fragment fragment = null;
        Class fragmentClass = HomeFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_main, fragment, "home");
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedpreferences = getSharedPreferences(Config.sharedLoginPREFERENCES,
                Context.MODE_PRIVATE);
        String phone = sharedpreferences.getString(Config.sharedPhone, "");
        String name = sharedpreferences.getString(Config.sharedName, "");

        View hView = navigationView.getHeaderView(0);
        TextView nhPhone = (TextView) hView.findViewById(R.id.nav_header_phone);
        TextView nhName = (TextView) hView.findViewById(R.id.nav_header_name);
        if (phone.equals("")) {
            nhName.setText(getString(R.string.guest_user_name));
            nhPhone.setText("");
            hideNavItem();
        } else {
            nhName.setText(name);
            nhPhone.setText(phone);
        }
    }

    private void hideNavItem() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_profile).setVisible(false);
        nav_Menu.findItem(R.id.nav_your_orders).setVisible(false);
        nav_Menu.findItem(R.id.nav_sign_out).setVisible(false);
    }

    public boolean isInternetConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            if (isInternetConnected()) {
                startActivity(new Intent(this, CartActivity.class));
            } else {
                Toast.makeText(this, "Check your internet connection", Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String TAG = "";

        Fragment fragment = null;
        Class fragmentClass = HomeFragment.class;

        if (id == R.id.nav_home) {
            fragmentClass = HomeFragment.class;
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (id == R.id.nav_track) {

        } else if (id == R.id.nav_your_orders) {
            fragmentClass = YourOrdersFragment.class;
        } else if (id == R.id.nav_feedback) {

        } else if (id == R.id.nav_about) {
            fragmentClass = AboutFragment.class;
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_sign_out) {
            SharedPreferences sharedpreferences = getSharedPreferences(Config.sharedLoginPREFERENCES,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(this, UserTypeActivity.class));
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        if (id == R.id.nav_home || id == R.id.nav_profile || id == R.id.nav_sign_out) {
            setTitle(R.string.app_name);
        } else {
            setTitle(item.getTitle());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    // **********  Home Fragment  **********

    public void onBurgerButton(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra(Config.menuItemTag, Config.menuBurger);
        startActivity(intent);
    }

    public void onSandwichButton(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra(Config.menuItemTag, Config.menuSandwich);
        startActivity(intent);
    }

    public void onMaggiButton(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra(Config.menuItemTag, Config.menuMaggi);
        startActivity(intent);
    }

    public void onPastaButton(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra(Config.menuItemTag, Config.menuPasta);
        startActivity(intent);
    }

    public void onMacroniButton(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra(Config.menuItemTag, Config.menuMacroni);
        startActivity(intent);
    }

    public void onNoodlesButton(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra(Config.menuItemTag, Config.menuNoodles);
        startActivity(intent);
    }


    //********** About Fragment **********
    public void onDeveloperClick(View view) {
        composeEmail("aayushamann@gmail.com", "Hungrooz");
    }

    public void composeEmail(String address, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + address)); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
