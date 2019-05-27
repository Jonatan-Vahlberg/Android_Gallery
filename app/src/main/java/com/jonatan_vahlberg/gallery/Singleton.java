package com.jonatan_vahlberg.gallery;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.UUID;

public class Singleton {
    private boolean willDelete;
    private Dialog deleteDialogue;
    final public static  Singleton shared = new Singleton();
    private ArrayList<ImageObject> list = new ArrayList<>();
    private ArrayList<Long> itemsToBeDeleted = new ArrayList<>();

    private final String DIRECTORY_NAME = "/Gallery";
    final String IMAGE_DIRECTORY_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+DIRECTORY_NAME;
    final String INDEX_INTENT = "INDEX";
    boolean DELETION_MODE = false;
    private  Singleton(){

    }

    void toggleDeletionMode(boolean toggle){
        DELETION_MODE = toggle;
        if (!DELETION_MODE){
            itemsToBeDeleted = new ArrayList<>();
        }
    }

    void showDeleteDialogue(final Context context){
        deleteDialogue = new Dialog(context);
        deleteDialogue.setContentView(R.layout.delete_popup);
        Button yesBtn,noBtn;

        yesBtn = deleteDialogue.findViewById(R.id.delete_alert_yes);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItems();
                deleteDialogue.dismiss();
                willDelete = true;
                toggleDeletionMode(false);
                if(context instanceof MainActivity){
                    ((MainActivity) context).renderMenus();
                    ((MainActivity) context).notifyDatasetChanged();

                }
                else if(context instanceof ImageDetailActivity){
                    if(list.size() < 1){
                        ((ImageDetailActivity) context).finish();
                        return;
                    }
                    int index = ((ImageDetailActivity) context).list_index;
                    if(index > list.size()-1){
                        ((ImageDetailActivity) context).list_index = list.size() -1;
                    }
                    ((ImageDetailActivity) context).load();
                }
            }
        });
        noBtn = deleteDialogue.findViewById(R.id.delete_alert_no);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialogue.dismiss();
                if(context instanceof  ImageDetailActivity) toggleDeletionMode(false);
            }
        });
        deleteDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deleteDialogue.show();
    }

    private void deleteItems(){

        for(int i = 0; i < list.size(); i++){
            ImageObject object = list.get(i);
            for(Long id : itemsToBeDeleted){
                if(object.getId() == id){
                    File file = new File(IMAGE_DIRECTORY_PATH+"/"+object.getTitle());
                    boolean deleted = file.delete();
                    if(deleted) list.remove(i);
                }
            }
        }
    }

    void load(Context context){
        list = new ArrayList<>();
        File directory = new File(IMAGE_DIRECTORY_PATH);
        FilenameFilter fileFilter = (new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jpg");
            }
        });
        File[] files = directory.listFiles(fileFilter);
        for(File file : files) {

            Glide.with(context)
                    .load(IMAGE_DIRECTORY_PATH+"/"+file.getName())
                    .fitCenter()
                    .preload(200,200);

            list.add(new ImageObject(file.getName(), UUID.randomUUID().getMostSignificantBits()));

        }
    }

    ImageObject getFromList(int position){ return list.get(position);}

    void addToList(ImageObject object){
        list.add(object);
    }

    void removeFromList(int position){
        list.remove(position);
    }

    int getListSize(){
        return  list.size();
    }

    private ArrayList<ImageObject> intiialFill(){
        return null;
    }

    void addToDeleteList(Long id){
        itemsToBeDeleted.add(id);
    }
    void removeFromDeleteList(Long id){
        itemsToBeDeleted.remove(id);
    }

    boolean itemDeleteState(int i){
        for(Long id : itemsToBeDeleted){
            if(id == list.get(i).getId()){
                return true;
            }
        }
        return false;
    }
    int getDeleteListSize(){
        return itemsToBeDeleted.size();
    }
}
