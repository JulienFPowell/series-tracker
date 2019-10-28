package com.mple.seriestracker.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mple.seriestracker.R;
import com.mple.seriestracker.ShowTracker;
import com.mple.seriestracker.api.episodate.Episodate;
import com.mple.seriestracker.api.episodate.entities.SearchResult;
import com.mple.seriestracker.api.episodate.entities.ShowSearchResult;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class ShowSearchActivity extends AppCompatActivity {

    public static boolean destroyed = true;

    SearchView searchView;
    RecyclerView recyclerView;

    Episodate episodate;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        destroyed = false;
        setContentView(R.layout.show_search);
        episodate = Episodate.INSTANCE;
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                //Requests the background task to search for desired query, when pressed enter
                new RetrieveSearchTask().execute(searchView.getQuery().toString());
                return false;
            }

            //Because this is a free api, we should not call a result on each character
            //As the api is already fairly slow (due to insane volume of requests)
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private class ShowInfo{
        private long showID;
        private String showName;
        private String showImage;
        private String showYear;
        private String showGenres;
    }

    //The custom Recycler view adapter
    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

        private List<ShowInfo> itemList;
        private Context context;

        private RecyclerViewAdapter(Context context, List<ShowInfo> itemList) {
            this.itemList = itemList;
            this.context = context;
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layoutView = LayoutInflater.from(context).inflate(R.layout.show_search_tile, parent,false);
            return (new RecyclerViewHolder(layoutView));
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            ShowInfo showInfo = itemList.get(position);
            Picasso.get().load(showInfo.showImage).into(holder.imageViewShow);
            holder.textShowName.setText(showInfo.showName);
            holder.textShowStatus.setText(showInfo.showGenres);
            holder.textShowYear.setText(showInfo.showYear);
            if(ShowTracker.INSTANCE.tvShowCache.containsKey(showInfo.showID)){ //Show is already added
                holder.buttonAddShow.setVisibility(View.INVISIBLE);  //Hide the add button
            }

            holder.buttonAddShow.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("showID",showInfo.showID);
                    intent.putExtra("showName",showInfo.showName);
                    intent.putExtra("showImage",showInfo.showImage);

                    //Tell the HomeScreenActivity we have a new item to add
                    //This calls onResult in the main Activity
                    setResult(HomeScreenActivity.NEW_SHOW_REQUEST_RESULT_CODE,intent);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return this.itemList.size();
        }
    }

    //Responsible for holding all objects within the show_search.xml
    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewShow;
        TextView textShowName;
        TextView textShowStatus;
        TextView textShowYear;
        Button buttonAddShow;

        private RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewShow = itemView.findViewById(R.id.imageViewSearchResult);
            textShowName = itemView.findViewById(R.id.textViewSearchResult);
            textShowStatus = itemView.findViewById(R.id.textShowStatusSearchResult);
            textShowYear = itemView.findViewById(R.id.textYearGroupSearchResult);
            buttonAddShow = itemView.findViewById(R.id.textAddSearchResult);

        }
    }

    class RetrieveSearchTask extends AsyncTask<String,Void, SearchResult> {
        RecyclerView recyclerView;
        @Override
        protected SearchResult doInBackground(String... strings) {
            try {
                Response<SearchResult> searchResults = episodate.search().textQuery(strings[0],1)
                        .execute();
                if(searchResults.isSuccessful()){
                    return searchResults.body();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(SearchResult searchResults) {
            recyclerView = findViewById(R.id.recyclerView);
            List<ShowInfo> list = new ArrayList<>();
            for (ShowSearchResult searchResult : searchResults.tv_shows) {
                ShowInfo showInfo = new ShowInfo();
                showInfo.showImage = searchResult.image_thumbnail_path;
                showInfo.showName = searchResult.name;
                showInfo.showYear = searchResult.start_date + "";
                showInfo.showGenres = searchResult.status;
                showInfo.showID = searchResult.id;
                list.add(showInfo);
            }
            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(),list);
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyed = true;
    }
}
