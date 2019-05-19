package com.jonatan_vahlberg.gallery;

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
    public void addToList(ImageObject object){
        list.add(object);
    }

    public  void removeFromList(int position){
        list.remove(position);
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
            list.add(new ImageObject(file.getName(),""));
        }
        return list;
    }
}
