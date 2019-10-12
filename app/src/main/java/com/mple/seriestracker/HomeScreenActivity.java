package com.mple.seriestracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.mple.seriestracker.fragments.CountdownFragment;
import com.mple.seriestracker.fragments.SectionsPagerAdapter;
import com.mple.seriestracker.fragments.MyShowsFragment;


public class HomeScreenActivity extends AppCompatActivity {

    static final int NEW_SHOW_REQUEST_CODE = 1;
    static final int FILE_PERMISSION_RREQUEST_CODE = 1;
    static final int NEW_SHOW_REQUEST_RESULT_CODE = 1;

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    TabLayout mTabs;

    MyShowsFragment mMyShowsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sets all date time stuff to correct sync
        AndroidThreeTen.init(this);
        mMyShowsFragment = new MyShowsFragment();
        //Initialize fragments
        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        mViewPager = findViewById(R.id.view_pager);
        setupViewPager(mViewPager);
        mTabs = findViewById(R.id.tabs);
        mTabs.setupWithViewPager(mViewPager);


        //Initialize floating menu button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSearchIntent();
            }
        });
    }

    //Responsible for setting up the fragments for each tab
    private void setupViewPager(ViewPager viewPager){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(this,getSupportFragmentManager());
        adapter.addFragment(mMyShowsFragment,"My Shows");
        adapter.addFragment(new CountdownFragment(),"Countdowns");
        viewPager.setAdapter(adapter);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_SHOW_REQUEST_CODE && resultCode == NEW_SHOW_REQUEST_RESULT_CODE) {
            ShowInfo showInfo = new ShowInfo(); //Creates a new object
            showInfo.id = data.getLongExtra("showID",0);
            showInfo.imagePath = data.getStringExtra("showImage");
            showInfo.name = data.getStringExtra("showName");
            mMyShowsFragment.addShow(showInfo); //Adds it to the fragment, fragment will then automatically update it
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case FILE_PERMISSION_RREQUEST_CODE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Todo Finish this, if we will be implementing saving
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    //Prevents the app opening multiple search intents, if spammed
    void startSearchIntent(){
        if(!ShowSearchActivity.destroyed) return;
        Intent intent = new Intent(getApplicationContext(),ShowSearchActivity.class);
        startActivityForResult(intent,NEW_SHOW_REQUEST_CODE);
    }

    //Will be used for writing saved data, later on to keep track of what shows are saved
    boolean hasFilePermissions(){
        return (Build.VERSION.SDK_INT > 22 && ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED);
    }

    void askForPermission(){
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, FILE_PERMISSION_RREQUEST_CODE);
    }
}
