package com.jonatan_vahlberg.gallery;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.UUID;

public class Singleton {


    final public static  Singleton shared = new Singleton();
    private ArrayList<ImageObject> list = new ArrayList<>();
    private ArrayList<Long> itemsToBeDeleted = new ArrayList<>();
    public boolean DELETION_MODE = false;
    private  Singleton(){
        //list = intiialFill();

    }

    public void toggleDeletionMode(boolean toggle){
        DELETION_MODE = toggle;
        if (!DELETION_MODE){
            itemsToBeDeleted = new ArrayList<>();
        }
    }

    public void deleteItems(){

        for(int i = 0; i < list.size(); i++){
            ImageObject object = list.get(i);
            for(Long id : itemsToBeDeleted){
                if(object.getId() == id){
                    File file = new File(Globals.IMAGE_DIRECTORY_PATH+"/"+object.getTitle());
                    boolean deleted = file.delete();
                    if(deleted) list.remove(i);
                }
            }
        }
    }

    public void load(Context context){
        list = new ArrayList<>();
        File directory = new File(Globals.IMAGE_DIRECTORY_PATH);
        FilenameFilter fileFilter = (new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jpg");
            }
        });
        File[] files = directory.listFiles(fileFilter);
        for(File file : files) {

            Glide.with(context)
                    .load(Globals.IMAGE_DIRECTORY_PATH+"/"+file.getName())
                    .fitCenter()
                    .preload(200,200);

            list.add(new ImageObject(file.getName(), UUID.randomUUID().getMostSignificantBits()));

        }
    }

    public ImageObject getFromList(int position){ return list.get(position);}

    public void addToList(ImageObject object){
        list.add(object);
    }

    public  void removeFromList(int position){
        list.remove(position);
    }

    public int getListSize(){
        return  list.size();
    }

    private ArrayList<ImageObject> intiialFill(){
        return null;
    }

    public void addToDeleteList(Long id){
        itemsToBeDeleted.add(id);
    }
    public  void removeFromDeleteList(Long id){
        itemsToBeDeleted.remove(id);
    }

    public boolean itemDeleteState(int i){
        for(Long id : itemsToBeDeleted){
            if(id == list.get(i).getId()){
                return true;
            }
        }
        return false;
    }
    public int getDeleteListSize(){
        return itemsToBeDeleted.size();
    }

    public Bitmap scaleDownBitmap(Bitmap bitmap, int newHeight, Context context){
        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h = (int)(newHeight * densityMultiplier);
        int w = (int)(h* bitmap.getWidth()/(double) bitmap.getHeight());
        bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
        return bitmap;
    }

    public Bitmap decodeAndScale(File file){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = true;
        options.inSampleSize = 4;
        options.inDensity = 1200;
        options.inTargetDensity = 100 * options.inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),options);
        return bitmap;
    }
}
