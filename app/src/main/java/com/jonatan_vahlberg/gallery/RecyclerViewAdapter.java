package com.jonatan_vahlberg.gallery;

import android.content.Context;
import android.content.Intent;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public abstract class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Runnable {

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
        final RecyclerView.ViewHolder finalViewHolder = viewHolder;
        ImageObject imageObject = Singleton.shared.getFromList(i);
        if(viewHolder instanceof GridViewHolder){

            Glide.with(mContext)
                    .load(Globals.IMAGE_DIRECTORY_PATH+"/"+imageObject.getTitle())
                    .fitCenter()
                    .override(200,200)
                    .into(((GridViewHolder) viewHolder).image);

            ((GridViewHolder) viewHolder).title.setText(truncateTitle(imageObject.getTitle()));
            toggleItemInDeletedLayoutState(Singleton.shared.itemDeleteState(i),viewHolder.itemView,true);
        }
        else if(viewHolder instanceof  ListViewHolder){
            ((ListViewHolder) viewHolder).title.setText((imageObject.getTitle()));
            Glide.with(mContext)
                    .load(Globals.IMAGE_DIRECTORY_PATH+"/"+imageObject.getTitle())
                    .fitCenter()
                    .override(200,200)
                    .into(((ListViewHolder) viewHolder).image);
            toggleItemInDeletedLayoutState(Singleton.shared.itemDeleteState(i),viewHolder.itemView,false);

        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Singleton.shared.DELETION_MODE){
                    boolean grid = (finalViewHolder instanceof GridViewHolder);
                    ImageObject object = Singleton.shared.getFromList(INDEX);
                    if(!Singleton.shared.itemDeleteState(INDEX)){
                        Singleton.shared.addToDeleteList(object.getId());
                        toggleItemInDeletedLayoutState(Singleton.shared.itemDeleteState(INDEX),v,grid);
                    }
                    else{
                        Singleton.shared.removeFromDeleteList(object.getId());
                        toggleItemInDeletedLayoutState(Singleton.shared.itemDeleteState(INDEX),v,grid);
                    }
                    renderData();

                }
                else{
                    Intent intent = new Intent(v.getContext(),ImageDetailActivity.class);
                    intent.putExtra(Globals.INDEX_INTENT,INDEX);
                    mContext.startActivity(intent);
                }
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Singleton.shared.toggleDeletionMode(!Singleton.shared.DELETION_MODE);
                renderMenus();
                notifyDataSetChanged();
                return true;
            }
        });
    }



    private void toggleItemInDeletedLayoutState(boolean toBeDeleted,View itemView, boolean grid){
        int background = (toBeDeleted)? Color.parseColor("#369DCF"): Color.parseColor("#ffffff");
        RelativeLayout background_layout;
        if(grid) background_layout = itemView.findViewById(R.id.grid_background);
        else background_layout = itemView.findViewById(R.id.list_background);
        background_layout.setBackgroundColor(background);
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

    @Override
    public void run() {

    }

    //Viewholder used for Adapter Creation
    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        CardView layout;
        RelativeLayout background;

        public ListViewHolder(View itemView){
            super(itemView);
            image =  itemView.findViewById(R.id.list_image);
            title =  itemView.findViewById(R.id.list_image_title);
            layout =  itemView.findViewById(R.id.list_image_layout);
            background = itemView.findViewById(R.id.list_background);
        }
    }

    public class GridViewHolder extends  RecyclerView.ViewHolder{
        ImageView image;
        TextView title;
        CardView layout;
        RelativeLayout background;

        public GridViewHolder(View itemView){
            super(itemView);
            image =  itemView.findViewById(R.id.grid_image);
            title =  itemView.findViewById(R.id.grid_image_title);
            layout =  itemView.findViewById(R.id.grid_image_layout);
            background = itemView.findViewById(R.id.grid_background);
        }

    }

    public abstract void renderMenus();
    public abstract void renderData();
}
