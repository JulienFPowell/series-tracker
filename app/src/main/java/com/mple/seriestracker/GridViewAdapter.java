package com.mple.seriestracker;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {

    private Context context;

    List<ShowInfo> mShowsList;

    class ViewHolder{
        int position;
        ImageView imageView;
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
            //Sets appropriate tags
            vh.imageView.setTag(vh);
            view.setTag(vh);
        }else{
            vh = (ViewHolder) view.getTag();
        }
        vh.position = i;
        ShowInfo showInfo = mShowsList.get(i);
        Picasso.get().load(showInfo.imagePath).into(vh.imageView); //Picasso deals with caching and background threading automatically
        return view; //return the view
    }

}