package com.mple.seriestracker.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
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
import com.mple.seriestracker.R;
import com.mple.seriestracker.ShowInfo;
import com.mple.seriestracker.ShowTracker;
import com.mple.seriestracker.TvShow;
import com.mple.seriestracker.api.episodate.Episodate;
import com.mple.seriestracker.api.episodate.entities.show.TvShowResult;
import com.mple.seriestracker.database.EpisodeTrackDatabase;
import com.mple.seriestracker.fragments.CountdownFragment;
import com.mple.seriestracker.fragments.MyShowsFragment;
import com.mple.seriestracker.fragments.SectionsPagerAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;


public class HomeScreenActivity extends AppCompatActivity {

    static final int NEW_SHOW_REQUEST_CODE = 1;
    static final int FILE_PERMISSION_RREQUEST_CODE = 1;
    static final int NEW_SHOW_REQUEST_RESULT_CODE = 1;

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    TabLayout mTabs;
    boolean started = false;

    //TODO sort the countdown tab based on time (sorta works but some weird threading stuff screwing it up)
    //All done after that
    public static FloatingActionButton deleteButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EpisodeTrackDatabase.setInstance(new EpisodeTrackDatabase(getApplicationContext()));

        //Sets all date time stuff to correct sync
        AndroidThreeTen.init(this);

        if (!hasFilePermissions()) {
            askForPermission();
        }

        //Initialize fragments
        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        mViewPager = findViewById(R.id.view_pager);
        setupViewPager(mViewPager);
        mTabs = findViewById(R.id.tabs);
        mTabs.setupWithViewPager(mViewPager);

        //Initialize floating menu button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((View view) -> startSearchIntent());

        //Initialize delete button
        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.hide();
        //Deletes all selected items from the "my shows" tab
        //TODO Also delete items from the countdown tab too
        deleteButton.setOnClickListener((View) -> {
            ShowInfo[] selectedShows =  ((MyShowsFragment)mSectionsPagerAdapter.getItem(0)).getSelectedShows();

            for (ShowInfo selectedShow: selectedShows) {
                ((CountdownFragment) mSectionsPagerAdapter.getItem(1)).removeShow(selectedShow);
            }
            ((MyShowsFragment)mSectionsPagerAdapter.getItem(0)).deleteSelected();
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        if(started) return;
        loadSettings();
        started = true;
    }

    //Responsible for setting up the fragments for each tab
    private void setupViewPager(ViewPager viewPager){
        mSectionsPagerAdapter.addFragment(new MyShowsFragment(),"My Shows");
        mSectionsPagerAdapter.addFragment(new CountdownFragment(),"Countdowns");
        viewPager.setAdapter(mSectionsPagerAdapter);
    }

    public void loadSettings(){
        //Loads settings from database
        new LoadShowsTask().execute();
    }

    //Adds a show to the "my shows" tab
    public void addShow(ShowInfo showInfo){
        new TvShowTask().execute(showInfo); //Background task to get info from the api
        ((MyShowsFragment)mSectionsPagerAdapter.getItem(0)).addShow(showInfo); //Adds it to the fragment, fragment will then automatically update it
        EpisodeTrackDatabase.INSTANCE.addShow(showInfo.name,showInfo.imagePath,showInfo.id);
    }

    public void addCountdown(long showID){
        if(ShowTracker.INSTANCE.calenderCache.contains(showID))return;
        ((CountdownFragment)mSectionsPagerAdapter.getItem(1)).addCountdown(showID);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_SHOW_REQUEST_CODE && resultCode == NEW_SHOW_REQUEST_RESULT_CODE) {
            //Create a new show, add it to the list and populate it
            ShowInfo showInfo = new ShowInfo(); //Creates a new object
            showInfo.id = data.getLongExtra("showID",0);
            showInfo.imagePath = data.getStringExtra("showImage");
            showInfo.name = data.getStringExtra("showName");
            addShow(showInfo);
        }
    }

    class LoadShowsTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            //Wait for fragment to load, before loading anything to it
            //Didn't have much time to fix it, so we went with a quick fix
            while(((MyShowsFragment) mSectionsPagerAdapter.getItem(0)).getAdapter() == null){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ShowInfo[] showData =  EpisodeTrackDatabase.INSTANCE.getAllShows();
            runOnUiThread(() ->{
                for (ShowInfo show : showData) {
                    addShow(show);
                }
            });
            return null;
        }
    }

    class TvShowTask extends AsyncTask<ShowInfo,Void, List<TvShowResult>> {
        //Responsible for obtaining info about each show
        //Automatically prompts on each show add/app initialization
        @Override
        protected List<TvShowResult> doInBackground(ShowInfo ... shows) {
            List<TvShowResult> tvShowResults = new ArrayList<>();
            for (ShowInfo show: shows) {
                try {
                    Response<com.mple.seriestracker.api.episodate.entities.show.TvShow> response = Episodate.INSTANCE
                            .show()
                            .textQuery(show.id + "")
                            .execute();

                    if(response.isSuccessful()){
                        tvShowResults.add(response.body().tvShow);
                    }
                } catch (IOException e) {}
            }
            return tvShowResults;
        }

        @Override
        protected void onPostExecute(List<TvShowResult> result) {
            for (TvShowResult tvShowResult : result) {
                TvShow tvShow = new TvShow(tvShowResult);
                ShowTracker.INSTANCE.addTvShow(tvShow);
                if(tvShow.getCountdown() != null){
                    addCountdown(tvShow.getId());
                }
            }
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
        return (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    void askForPermission(){
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, FILE_PERMISSION_RREQUEST_CODE);
    }
}