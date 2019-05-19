package com.jonatan_vahlberg.gallery;

public class ImageObject {

    private String title;
    private String dateCreated;
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

    public ImageObject(String title, String dateCreated){
        this.title = title;
        this.dateCreated = dateCreated;
    }


}
