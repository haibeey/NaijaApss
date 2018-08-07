package com.haibeey.android.naijaapps.mainpage;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.haibeey.android.naijaapps.DeveloperPage.DeveloperRegistration;
import com.haibeey.android.naijaapps.R;
import com.haibeey.android.naijaapps.SettingsActivity;
import com.haibeey.android.naijaapps.StaticNames;
import com.haibeey.android.naijaapps.UserReg.RegUser;
import com.haibeey.android.naijaapps.filechooser.FileChooserActivity;
import com.haibeey.android.naijaapps.models.DeskTopApp;
import com.haibeey.android.naijaapps.models.MobileApp;
import com.haibeey.android.naijaapps.uploads.UploadActivity;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private MainPageAdapter mainPageAdapter;
    private boolean BeingSearch;

    private MenuItemCompat.OnActionExpandListener SearchListener=new MenuItemCompat.OnActionExpandListener() {
        @Override
        public boolean onMenuItemActionExpand(MenuItem item) {
            return true;
        }

        @Override
        public boolean onMenuItemActionCollapse(MenuItem item) {
            onBackPressed();
            return true;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SetUpViewPager();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        FloatingActionButton floatingActionButton=findViewById(R.id.fab_main);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,UploadActivity.class);
                startActivity(intent);
            }
        });
        if(getDeveloperId()==null){
            floatingActionButton.setVisibility(View.GONE);
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    private void SetUpViewPager(){
        ViewPager viewpager=(ViewPager)findViewById(R.id.main_view_pager);
        mainPageAdapter=new MainPageAdapter(getSupportFragmentManager());
        viewpager.setAdapter(mainPageAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(BeingSearch){
            mainPageAdapter.getMobileApps().recyclerView.swapAdapter(mainPageAdapter
                    .getMobileApps().mobileAdapter,true);
            mainPageAdapter.getDesktopApps().recyclerView.swapAdapter(mainPageAdapter
                    .getDesktopApps().desktopAdapter,true);
            BeingSearch=false;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem item=menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) item.getActionView();
        MenuItemCompat.setOnActionExpandListener( item,SearchListener);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            onSearchRequested();
        } else if (id == R.id.nav_user_reg) {
            startAnotherActivity(RegUser.class);
        } else if (id == R.id.nav_developer_reg) {
            startAnotherActivity(DeveloperRegistration.class);
        } else if (id == R.id.nav_share) {
            Intent I=new Intent(Intent.ACTION_SEND);
            I.setType("text/plain");
            I.putExtra(Intent.EXTRA_TEXT,"Download Link");
            startActivity(I);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startAnotherActivity(Class cls){
        Intent intent =new Intent(this,cls);
        startActivity(intent);
    }

    private String getDeveloperId() {
        SharedPreferences sharedPreferences = getSharedPreferences(StaticNames.ApplicationId, MODE_PRIVATE);
        return sharedPreferences.getString(StaticNames.DeveloperId, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            setSearchUp(query);

        }
    }

    private void setSearchUp(String searchUp) {
        ArrayList<MobileApp> arrayListMobile=mainPageAdapter.getMobileApps().mobileAdapter.getDataItems();
        ArrayList<DeskTopApp> arrayListDesktop=mainPageAdapter.getDesktopApps().desktopAdapter.getDataItems();

        ArrayList<MobileApp> searchResultMobile=new ArrayList<>();
        ArrayList<DeskTopApp> searchResultDesktop=new ArrayList<>();

        Pattern pattern=Pattern.compile(searchUp);
        for(MobileApp mobileApp:arrayListMobile){
            if(pattern.matcher(mobileApp.getName()).find() ||
                    pattern.matcher(mobileApp.getUploaded_name()).find()){
                searchResultMobile.add(mobileApp);
            }
        }

        for(DeskTopApp deskTopApp:arrayListDesktop){
            if(pattern.matcher(deskTopApp.getName()).find() ||
                    pattern.matcher(deskTopApp.getUploaded_name()).find()){
                searchResultDesktop.add(deskTopApp);
            }
        }

        DesktopAdapter desktopAdapter=new DesktopAdapter(this);
        MobileAdapter mobileAdapter=new MobileAdapter(this);
        desktopAdapter.setDataItems(searchResultDesktop);
        mobileAdapter.setDataItems(searchResultMobile);

        mainPageAdapter.getDesktopApps().recyclerView.swapAdapter(desktopAdapter,true);
        mainPageAdapter.getMobileApps().recyclerView.swapAdapter(mobileAdapter,true);

        BeingSearch=true;
    }


}
