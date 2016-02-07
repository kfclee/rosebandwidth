package edu.rosehulman.jungckjp_leekf.rosebandwidth.activities;


import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;

import edu.rosehulman.jungckjp_leekf.rosebandwidth.R;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.activities.LoginActivity;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.adapters.DeviceAdapter;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.fragments.AlarmsFragment;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.fragments.DevicesFragment;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.fragments.SettingsFragment;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.fragments.UsageFragment;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.utils.API;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.utils.Constants;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private API mAPI;
    private FloatingActionButton mFab;

//    private Adapter mCurrentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setVisibility(View.GONE);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_container, new UsageFragment(), "Visible Fragment");
            ft.commit();
        }

        String username = "";

        if (getIntent().getExtras() != null) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("password",getIntent().getStringExtra("password")).commit();
            username = getIntent().getStringExtra(Constants.USERNAME);
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString(Constants.USERNAME, username).commit();
        }

        try {
            mAPI = API.createNew(this);
            mAPI.setCurrentUser(username);
            mAPI.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FloatingActionButton getFab() {
        return mFab;
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
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_refresh){
            mAPI.getData();
//            getCurrentFragment();
            return false;
        }

        else if(id == R.id.action_logout){
            onLogout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment switchTo = null;

        switch(item.getItemId()) {
            case R.id.nav_home:
                switchTo = new UsageFragment();
                break;
            case R.id.nav_devices:
                switchTo = new DevicesFragment();
                break;
            case R.id.nav_alerts:
                switchTo = new AlarmsFragment();
                break;
            case R.id.nav_settings:
                switchTo = new SettingsFragment();
                break;
            case R.id.nav_logout:
                onLogout();
                break;
            default:
                break;
        };

        if(switchTo != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, switchTo, "Visible Fragment");
            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                getSupportFragmentManager().popBackStackImmediate();
            }

            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public Fragment getCurrentFragment(){
        return getSupportFragmentManager().findFragmentByTag("Visible Fragment");
    }

    public void onLogout(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(intent);
        finish();
    }

}
