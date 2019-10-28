package com.mple.seriestracker.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mple.seriestracker.Countdown;
import com.mple.seriestracker.R;
import com.mple.seriestracker.ShowInfo;
import com.mple.seriestracker.ShowTracker;
import com.mple.seriestracker.activity.HomeScreenActivity;
import com.mple.seriestracker.database.EpisodeTrackDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyShowsFragment extends Fragment {

    List<ShowInfo> mShowsList = new ArrayList<>();

    public List<Integer> getSelectedItems() {
        return mSelectedItems;
    }

    public List<Integer> mSelectedItems = new ArrayList<>();
    GridView mGridView;
    GridViewAdapter mAdapter;

    boolean started = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myshows_fragment,container,false);
        setRetainInstance(true);
        mGridView = view.findViewById(R.id.mainGridView);
        mGridView.setNumColumns(3);
        mAdapter = new GridViewAdapter(getContext(),mShowsList);
        mGridView.setAdapter(mAdapter);
        loadSettings();
        return view;
    }

    public void loadSettings(){
        if(started) return;
        started = true;
        //Loads settings from database
       // new LoadShowsTask().execute();
    }

    class LoadShowsTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            ShowInfo[] showData =  EpisodeTrackDatabase.INSTANCE.getAllShows();
            getActivity().runOnUiThread(() ->{
                for (ShowInfo show : showData) {
                    addShow(show);
                }
            });
            return null;
        }
    }


    public void deleteSelected(){
        if(mSelectedItems.size() == 0) return; //ensure we have items to delete
        ShowInfo[] showInfos = getSelectedShows();
        for (ShowInfo showInfo : showInfos) {
            if(mShowsList.removeIf(n -> n.id == showInfo.id)){
                EpisodeTrackDatabase.INSTANCE.deleteShow(showInfo.id);
                ShowTracker.INSTANCE.tvShowCache.remove(showInfo.id);

            }
        }
        mSelectedItems.clear();
        HomeScreenActivity.deleteButton.hide();
        mAdapter.notifyDataSetChanged();
    }

    public ShowInfo[] getSelectedShows(){
        ShowInfo[] showInfos = new ShowInfo[mSelectedItems.size()];
        for (int i = 0; i < mSelectedItems.size(); i++) {
            showInfos[i] = mShowsList.get(mSelectedItems.get(i));
        }
        return showInfos;
    }



    public void addShow(ShowInfo showInfo){
        mShowsList.add(showInfo);
//        selected = new boolean[mShowsList.size()]; //update size of
        updateAdapter();
    }

    private void updateAdapter(){
        mAdapter.notifyDataSetChanged();
    }

    public class GridViewAdapter extends BaseAdapter {

        private Context context;

        List<ShowInfo> mShowsList;

        class ViewHolder{
            int position;
            ImageView imageView;
            CheckBox checkBox;
        }

        public GridViewAdapter(Context context, List<ShowInfo> showsList){
            this.context = context;
            this.mShowsList = showsList;
        }

        @Override
        public int getCount() {
            return mShowsList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder vh;
            if(view == null){
                view = LayoutInflater.from(context).inflate(R.layout.show_tile,null); //gets the inflator from our context and sets the view with our custom xml file
                vh = new ViewHolder();
                vh.imageView = view.findViewById(R.id.showTileImageView); //sets image view
                vh.checkBox = view.findViewById(R.id.itemCheckBox);
                //Sets appropriate tags
                vh.imageView.setTag(vh);
                view.setTag(vh);
            }else{
                vh = (GridViewAdapter.ViewHolder) view.getTag();
            }
            vh.checkBox.setId(i);
            vh.imageView.setId(i);
            vh.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    CheckBox checkBox = vh.checkBox;
                    int id = checkBox.getId();
                    if(mSelectedItems.contains(id)){
                        checkBox.setSelected(false);
                        mSelectedItems.removeIf(n -> n == id);
                        checkBox.setVisibility(View.INVISIBLE);
                    }else{
                        checkBox.setSelected(true);
                        mSelectedItems.add(id);
                        checkBox.setVisibility(View.VISIBLE);
                    }
                    vh.checkBox.setChecked(mSelectedItems.contains(i));

                    if(mSelectedItems.size() > 0){
                        HomeScreenActivity.deleteButton.show();
                    }else{
                        HomeScreenActivity.deleteButton.hide();
                    }
                    return false;
                }
            });
            vh.position = i;
            ShowInfo showInfo = mShowsList.get(i);
            Picasso.get().load(showInfo.imagePath).into(vh.imageView); //Picasso deals with caching and background threading automatically
            return view; //return the view
        }
    }
}