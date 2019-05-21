package com.jonatan_vahlberg.gallery;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class Singleton {


    final public static  Singleton shared = new Singleton();
    private ArrayList<ImageObject> list;
    private  Singleton(){
        list = intiialFill();
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
        ArrayList<ImageObject> list = new ArrayList<>();
        File directory = new File(Globals.IMAGE_DIRECTORY_PATH);
        FilenameFilter fileFilter = (new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jpg");
            }
        });
        File[] files = directory.listFiles(fileFilter);
        for(File file : files){
            Log.d("FileSRead", "readInFileNames: "+file.getName());
            Bitmap image = BitmapFactory.decodeFile(file.getAbsolutePath());
            list.add(new ImageObject(file.getName(),"",image));

        }
        return list;
    }

    public Bitmap scaleDownBitmap(Bitmap bitmap, int newHeight, Context context){
        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h = (int)(newHeight * densityMultiplier);
        int w = (int)(h* bitmap.getWidth()/(double) bitmap.getHeight());
        bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
        return bitmap;
    }
}
