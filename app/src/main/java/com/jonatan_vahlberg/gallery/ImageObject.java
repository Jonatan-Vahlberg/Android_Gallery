package com.jonatan_vahlberg.gallery;

import android.graphics.Bitmap;

public class ImageObject {

    private String title;
    private String dateCreated;
    private Bitmap imageScaledDown;
    private long ID;
    private long parentFolder;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Bitmap getImageScaledDown() {
        return imageScaledDown;
    }

    public void setImageScaledDown(Bitmap imageScaledDown) {
        this.imageScaledDown = imageScaledDown;
    }

    public ImageObject(String title, String dateCreated){
        this.title = title;
        this.dateCreated = dateCreated;
    }
    public ImageObject(String title, String dateCreated,Bitmap image){
        this.title = title;
        this.dateCreated = dateCreated;
        this.imageScaledDown = image;
    }



}
