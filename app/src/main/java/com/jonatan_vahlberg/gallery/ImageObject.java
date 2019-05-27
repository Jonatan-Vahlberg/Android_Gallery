package com.jonatan_vahlberg.gallery;


public class ImageObject {

    private String title;
    private long id;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ImageObject(String title,long id){
        this.title = title;
        this.id = id;

    }



}
