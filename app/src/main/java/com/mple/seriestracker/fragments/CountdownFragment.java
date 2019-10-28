package com.mple.seriestracker.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mple.seriestracker.Countdown;
import com.mple.seriestracker.R;
import com.mple.seriestracker.ShowInfo;
import com.mple.seriestracker.ShowTracker;
import com.mple.seriestracker.TvShow;
import com.mple.seriestracker.util.CountdownUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CountdownFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private List<TvShow> mTvShowList = new ArrayList<>();
    private RecyclerViewAdapter mRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.countdown_fragment,container,false);
        setRetainInstance(true);

        mRecyclerView = view.findViewById(R.id.recyclerViewCountdown);
        mRecyclerViewAdapter = new RecyclerViewAdapter(getContext(), mTvShowList);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setRetainInstance(true);
        return view;
    }

    public void addCountdown(long showID){
        //Add it to the list
        mTvShowList.add(ShowTracker.INSTANCE.getTvShow(showID));
        //Add it to the cache, to know it's in the list
        ShowTracker.INSTANCE.calenderCache.add(showID);
        //Tell adapter to refresh changes
        mRecyclerViewAdapter.notifyDataSetChanged();
        //sortByTime(); // re-sort by time
        //removeShow(showID);
    }

    //Sort each item by time, one with least time should go to the top
    private void sortByTime(){
        mTvShowList.sort(new Comparator<TvShow>() {
            @Override
            public int compare(TvShow tvShow, TvShow t1) {
                if(tvShow.getCountdown() == null || t1.getCountdown() == null) return 0;
                return tvShow.getCountdown().getAirDate().isBefore(t1.getCountdown().getAirDate()) ? -1 : 1;
            }
        });
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    //Used for deleting the show from the adapter.
    public void removeShow(ShowInfo show){
        if(mTvShowList.removeIf(n -> n.getId() == show.id)){
            ShowTracker.INSTANCE.calenderCache.remove(show.id);
        }
        mRecyclerViewAdapter.notifyDataSetChanged();
    }



    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{

        Context context;

        List<TvShow> tvShowsList;

        private RecyclerViewAdapter(Context context, List<TvShow> tvShowsList){
            this.context = context;
            this.tvShowsList = tvShowsList;
        }

        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View layoutView = LayoutInflater.from(context).inflate(R.layout.countdown_tile,parent,false);
            return new RecyclerViewHolder(layoutView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
            Handler handler=new Handler();
            handler.post(new Runnable(){
                @Override
                public void run() {
                    //populate the textviews with the messages
                    if(position >= tvShowsList.size()) {
                        return;
                    }
                    TvShow tvShow = tvShowsList.get(position);
                    holder.textViewTitle.setText(tvShow.getName());
                    Countdown countdown = tvShow.getCountdown(); //need to get the new countdown each loop
                    holder.textViewEpisodeInfo.setText(String.format("%s - S%sE%s",countdown.getName(),countdown.getSeason(),countdown.getEpisode()));
                    String text = countdown.getCountdownFormat(context);

                    if(text.equals("")){
                       Countdown newCountdown =  CountdownUtil.getUpcomingAiringEp(tvShow.getEpisodes(),countdown.getEpisode(),countdown.getSeason());
                       if(newCountdown != null){
                           tvShow.setCountdown(newCountdown);
//                           sortByTime();
                       }else{
                           //remove from the view
                           tvShowsList.remove(position);
                           //Resort
//                           sortByTime();
                           notifyDataSetChanged();
                           return; //End handler
                       }
                    }else{
                        holder.textViewAirTime.setText(countdown.getCountdownFormat(context));
                    }
                    handler.postDelayed(this,50);
                }
            });
        }
        @Override
        public int getItemCount() {
            return mTvShowList.size();
        }
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle;
        TextView textViewEpisodeInfo;
        TextView textViewAirTime;
        CardView cardView;

        private RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewShowTitleCountdown);
            textViewEpisodeInfo = itemView.findViewById(R.id.textViewEpisodeInfoCountdown);
            textViewAirTime = itemView.findViewById(R.id.textViewAirTimeCountdown);
            cardView = itemView.findViewById(R.id.cardViewCountdown);
            cardView.setMinimumWidth(Resources.getSystem().getDisplayMetrics().widthPixels - 50);
        }
    }
}