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
import com.mple.seriestracker.fragments.SectionsPagerAdapter;


public class HomeScreenActivity extends AppCompatActivity {

    static final int NEW_SHOW_REQUEST_CODE = 1;
    static final int FILE_PERMISSION_RREQUEST_CODE = 1;
    static final int NEW_SHOW_REQUEST_RESULT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidThreeTen.init(this);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSearchIntent();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_SHOW_REQUEST_CODE && resultCode == NEW_SHOW_REQUEST_RESULT_CODE) {
            //Add to main view
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
