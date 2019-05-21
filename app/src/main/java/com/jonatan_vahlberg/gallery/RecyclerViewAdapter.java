package com.jonatan_vahlberg.gallery;

import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private Boolean mGridMode;


    //constructor
    public RecyclerViewAdapter(Context context,boolean gridMode){
        mContext = context;
        mGridMode = gridMode;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder holder;
        if(i == 0){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_grid_item,viewGroup,false);
            holder = new GridViewHolder(view);
            return holder;
        }
        else if(i == 1){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_list_item,viewGroup,false);
            holder = new ListViewHolder(view);
            return  holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final int INDEX = i;
        ImageObject imageObject = Singleton.shared.getFromList(i);
        if(viewHolder instanceof GridViewHolder){
            ((GridViewHolder) viewHolder).image.setImageBitmap(imageObject.getImageScaledDown());
            ((GridViewHolder) viewHolder).image.setImageBitmap(BitmapFactory.decodeFile(Globals.IMAGE_DIRECTORY_PATH+"/"+imageObject.getTitle()));

        }
        else if(viewHolder instanceof  ListViewHolder){
            ((ListViewHolder) viewHolder).title.setText(imageObject.getTitle());
            ((ListViewHolder) viewHolder).image.setImageBitmap(imageObject.getImageScaledDown());
            //((ListViewHolder) viewHolder).image.setImageBitmap(BitmapFactory.decodeFile(Globals.IMAGE_DIRECTORY_PATH+"/"+imageObject.getTitle()));
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),ImageDetailActivity.class);
                intent.putExtra("View Detail",Singleton.shared.getFromList(INDEX).getTitle());
            }
        });
    }

    private String truncateTitle(String title) {
        if(title.length() > 15){
            return title.substring(0,9)+"...";
        }
        return title;
    }

    @Override
    public int getItemCount() {
        return Singleton.shared.getListSize();
    }

    @Override
    public int getItemViewType(int position) {

        return mGridMode? 0:1;
    }

    //Viewholder used for Adapter Creation
    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        LinearLayout layout;

        public ListViewHolder(View itemView){
            super(itemView);
            image =  itemView.findViewById(R.id.list_image);
            title =  itemView.findViewById(R.id.list_image_title);
            layout =  itemView.findViewById(R.id.list_image_layout);
        }
    }

    public class GridViewHolder extends  RecyclerView.ViewHolder{
        ImageView image;
        TextView title;
        CardView layout;

        public GridViewHolder(View itemView){
            super(itemView);
            image =  itemView.findViewById(R.id.grid_image);
            title =  itemView.findViewById(R.id.grid_image_title);
            layout =  itemView.findViewById(R.id.grid_image_layout);
        }

    }
}
