package com.mple.seriestracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mple.seriestracker.api.Trakt;
import com.squareup.picasso.Picasso;
import com.uwetrottmann.trakt5.entities.SearchResult;
import com.uwetrottmann.trakt5.enums.Type;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class ShowSearchActivity extends AppCompatActivity {

    public static boolean destroyed = true;

    SearchView searchView;
    RecyclerView recyclerView;

    Trakt trakt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        destroyed = false;
        setContentView(R.layout.show_search);
        trakt = Trakt.INSTANCE;
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                new RetrieveSearchTask(searchView.getQuery().toString()).execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    public class ShowInfo{
        public String showName;
        public String showImage;
        public String showYear;
        public String showGenres;
    }

    //The custom Recycler view adapter
    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

        private List<ShowInfo> itemList;
        private Context context;

        public RecyclerViewAdapter(Context context, List<ShowInfo> itemList) {
            this.itemList = itemList;
            this.context = context;
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layoutView = LayoutInflater.from(context).inflate(R.layout.show_search_tile, parent,false);
            RecyclerViewHolder rcv = new RecyclerViewHolder(layoutView);
            return rcv;
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            ShowInfo showInfo = itemList.get(position);
            Picasso.get().load(showInfo.showImage).into(holder.imageViewShow);
            holder.textShowName.setText(showInfo.showName);
            holder.textShowStatus.setText(showInfo.showGenres);
            holder.textShowYear.setText(showInfo.showYear);
        }

        @Override
        public int getItemCount() {
            return this.itemList.size();
        }
    }

    //Responsible for holding all objects within the show_search.xml
    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageViewShow;
        TextView textShowName;
        TextView textShowStatus;
        TextView textShowYear;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewShow = itemView.findViewById(R.id.imageViewSearchResult);
            textShowName = itemView.findViewById(R.id.textViewSearchResult);
            textShowStatus = itemView.findViewById(R.id.textShowStatusSearchResult);
            textShowYear = itemView.findViewById(R.id.textYearGroupSearchResult);
        }

        @Override
        public void onClick(View view) { //Might use this or just keep the button

        }
    }

    //Might be useful for other things, Picasso made it redundant atm tho
    class ImageLoaderTask extends AsyncTask<String,Void, Bitmap>{
        RecyclerViewHolder vh;

        ImageLoaderTask(RecyclerViewHolder vh){
            this.vh = vh;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                return Picasso.get().load(strings[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap != null){
                vh.imageViewShow.setImageBitmap(bitmap);
            }
        }
    }
    //Handles with the search in the background.
    class RetrieveSearchTask extends AsyncTask<String,Void,List<SearchResult>>{
        String query;
        RecyclerView recyclerView;
        public RetrieveSearchTask(String query){
            this.query = query;
        }
        @Override
        protected List<SearchResult> doInBackground(String... strings) {
            try {
                Response<List<SearchResult>> searchResults = trakt.getSearch().textQuery(Type.SHOW,query,null,null,null,null,null,null,null,null,1)
                        .execute();
                if(searchResults.isSuccessful()){//Requires oAuth if not, however this search doesn't require oAuth
                    return searchResults.body();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        //Temporary, will probably change API services.
        @Override
        protected void onPostExecute(List<SearchResult> searchResults) {
           recyclerView = findViewById(R.id.recyclerView);
           List<ShowInfo> list = new ArrayList<>();
            for (SearchResult searchResult : searchResults) {
                ShowInfo showInfo = new ShowInfo();
                showInfo.showImage = "http://static.tvmaze.com/uploads/images/medium_portrait/211/528026.jpg"; //Temporary photo
//                showInfo.showImage = searchResult.show.ids.imdb; //Get the image from OMDB api and parse it
                showInfo.showName = searchResult.show.title;
                showInfo.showYear = searchResult.show.year.toString();
                String genres = "";
                if(searchResult.show.genres != null){
                    for(int i=0;i<searchResult.show.genres.size();i++){
                        String formatChars = ", ";
                        if(i == searchResult.show.genres.size()){
                            formatChars = "";
                        }
                        genres+=searchResult.show.genres.get(i) + formatChars;
                    }
                }
                showInfo.showGenres = genres;
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
