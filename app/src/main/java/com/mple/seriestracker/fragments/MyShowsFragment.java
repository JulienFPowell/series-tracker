package com.mple.seriestracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mple.seriestracker.GridViewAdapter;
import com.mple.seriestracker.R;
import com.mple.seriestracker.ShowInfo;
import com.mple.seriestracker.database.EpisodeTrackDatabase;

import java.util.ArrayList;
import java.util.List;

public class MyShowsFragment extends Fragment {

    List<ShowInfo> mShowsList = new ArrayList<>();
    GridView mGridView;
    GridViewAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myshows_fragment,container,false);
        mGridView = view.findViewById(R.id.mainGridView);
        mGridView.setNumColumns(3);
        mAdapter = new GridViewAdapter(getContext(),mShowsList);
        mGridView.setAdapter(mAdapter);
        return view;
    }

    public void addShow(ShowInfo showInfo){
        mShowsList.add(showInfo);
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
}
