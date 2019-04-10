package com.upgrad.internshipapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements  QuestionsFragment.SendMessage{
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    TagsHelper tagsHelper;
    ArrayList<Tags> arrayList;
    String ctag="";
    MyAdapter adapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme2);
        setContentView(R.layout.activity_main);
        SharedPreferenceUtils sharedPreferenceUtils=new SharedPreferenceUtils(this,"auth");
        String tok=sharedPreferenceUtils.getStringValue("token","");
        String sel=sharedPreferenceUtils.getStringValue("sel","no");
        if(tok.equals("")){
            startActivity(new Intent(MainActivity.this ,Login.class));
            finish();
        }
        else if(sel.equals("no")){
            startActivity(new Intent(MainActivity.this ,UserInterest.class));
            finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolba);
        setSupportActionBar(toolbar);
        tagsHelper=new TagsHelper(MainActivity.this);
        arrayList=tagsHelper.getAllTags();
        viewPager = (ViewPager) findViewById(R.id.viewpager1);

        tabLayout = (TabLayout) findViewById(R.id.tabs1);
      //  if(isNetworkAvailable()) {
            tabLayout.addTab(tabLayout.newTab().setText("Home"));
            tabLayout.addTab(tabLayout.newTab().setText("Hot"));
            tabLayout.addTab(tabLayout.newTab().setText("Offline"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
      //  }


        tabLayout.setupWithViewPager(viewPager);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        final Menu menu = navigationView.getMenu();
        for(int i=0;i<arrayList.size();i++){
            if(arrayList.get(i).getSelected().equals("YES")){
                menu.add(arrayList.get(i).getName());
                if(ctag.equals("")){
                    ctag=arrayList.get(i).getName();
                }
            }
        }

        adapter = new MyAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount(),ctag);
        viewPager.setAdapter(adapter);

        final SharedPreferenceUtils sharedPreferenceUtils1=new SharedPreferenceUtils(MainActivity.this,"ques");
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                String a=menuItem.toString();
                //reload(a);
                Log.v("Menu sel",a);
                sharedPreferenceUtils1.setValue("tag",a);
                //Closing drawer on item click
                drawerLayout.closeDrawers();
                sendData(a);

                return true;

            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }
    protected boolean isNavDrawerOpen() {
        return drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
    protected void closeNavDrawer() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    @Override
    public void sendData(String message) {
        String tag = "android:switcher:" + R.id.viewpager1 + ":" + 1;
        QuestionsFragment f = (QuestionsFragment) getSupportFragmentManager().findFragmentByTag(tag);
        f.displayReceivedData(message,"activity");
        String tag2 = "android:switcher:" + R.id.viewpager1 + ":" + 0;
        QuestionsFragment f2 = (QuestionsFragment) getSupportFragmentManager().findFragmentByTag(tag2);
        f2.displayReceivedData(message,"hot");
    }
}
