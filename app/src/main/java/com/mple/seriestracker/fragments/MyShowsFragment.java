package com.mple.seriestracker.fragments;

import android.content.Context;
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

import com.mple.seriestracker.R;
import com.mple.seriestracker.ShowInfo;
import com.mple.seriestracker.database.EpisodeTrackDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyShowsFragment extends Fragment {

    List<ShowInfo> mShowsList = new ArrayList<>();
    boolean[] selected;
    GridView mGridView;
    GridViewAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myshows_fragment,container,false);
        setRetainInstance(true);
        mGridView = view.findViewById(R.id.mainGridView);
        mGridView.setNumColumns(3);
        selected = new boolean[mShowsList.size()];
        mAdapter = new GridViewAdapter(getContext(),mShowsList);
        mGridView.setAdapter(mAdapter);
        return view;
    }

    public void addShow(ShowInfo showInfo){
        mShowsList.add(showInfo);
        selected = new boolean[mShowsList.size()]; //update size of
        updateAdapter();
    }

    //Used for deleting the show from the adapter.
    public void removeShow(long showID){
        mShowsList.removeIf(info -> info.id == showID);
        updateAdapter();
        EpisodeTrackDatabase.INSTANCE.deleteShow(showID);
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
                    if(selected[id]){
                        checkBox.setSelected(false);
                        selected[id] = false;
                        checkBox.setVisibility(View.INVISIBLE);
                    }else{
                        checkBox.setSelected(true);
                        selected[id] = true;
                        checkBox.setVisibility(View.VISIBLE);
                    }
                    vh.checkBox.setChecked(selected[i]);
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