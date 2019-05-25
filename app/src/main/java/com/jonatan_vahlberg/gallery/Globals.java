package com.jonatan_vahlberg.gallery;

import android.os.Environment;

public class Globals {

    private static final String DIRECTORY_NAME = "/Gallery";
    public static final String IMAGE_DIRECTORY_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+DIRECTORY_NAME;
    public static final String INDEX_INTENT = "INDEX";

}
