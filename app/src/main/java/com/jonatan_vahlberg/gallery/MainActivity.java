package com.jonatan_vahlberg.gallery;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private LinearLayout topMenu;
    ImageButton cameraBtn, folderBtn, modeBtn;
    private ArrayList<ImageObject> mList = new ArrayList<>();

    //Static Finals for Image handling
    private static final String GALLERY_SUBFOLDER = "/Gallery";
    private static final String STATE_GRIDMODE = "gridMode";
    private static final String STATE_LIST = "listState";
    private static final int CAPTURE_IMAGE_REQUEST = 1;
    private static final int IMAGE_FROM_FOLDER_REQUSET = 2;
    private static final int CAMERA_PERMISSION_CODE = 100;

    private Parcelable listState;
    //private int currentRecyclePosition = 0;
    private boolean gridMode = true;
    private String currentFileName;

    @Override
    protected void onStart() {
        super.onStart();

        createImageDirectoryIfNeeded();

    }
    //runs at least once to create folder if folder is deleted runs again and creates it
    private void createImageDirectoryIfNeeded() {
        String galleryFolder = Globals.IMAGE_DIRECTORY_PATH;
        File f = new File(galleryFolder);
        if(!f.exists()){
            if(!f.mkdir()){
                Log.d("CRITICAL ERROR", "createImageDirectoryIfNeeded: ");
            }
        }
    }

    private void readInFileNames() {
//        mList = new ArrayList<>();
//        File directory = new File(Globals.IMAGE_DIRECTORY_PATH);
//        FilenameFilter fileFilter = (new FilenameFilter() {
//            @Override
//            public boolean accept(File dir, String name) {
//                return name.toLowerCase().endsWith(".jpg");
//            }
//        });
//        File[] files = directory.listFiles(fileFilter);
//        for(File file : files){
//            Log.d("FileSRead", "readInFileNames: "+file.getName());
//            mList.add(new ImageObject(file.getName(),""));
//        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState != null){
            recoverSavedInstances(savedInstanceState);
        }
        else{
            readInFileNames();
        }
        if(Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},CAMERA_PERMISSION_CODE);
        }
        //Singleton.shared.load(this);

        setupLayout();


    }

    @Override
    protected void onResume() {
        super.onResume();
        //readInFileNames();
        //adapter.notifyDataSetChanged();
    }

    private void recoverSavedInstances(Bundle savedInstance){
        gridMode = savedInstance.getBoolean(STATE_GRIDMODE,true);
        listState = savedInstance.getParcelable(STATE_LIST);
        //mList = (ArrayList<ImageObject>) savedInstance.getSerializable(STATE_LIST);
    }

    private void setupLayout(){
        topMenu = findViewById(R.id.activity_main_menu);
        modeBtn = topMenu.findViewById(R.id.menu_style_btn);
        modeBtn.setImageResource((gridMode) ? R.drawable.menu : R.drawable.list);
        modeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridMode = !gridMode;
                if(gridMode){
                    setupRecyclerViewGrid();
                    if(v instanceof ImageButton) ((ImageButton) v).setImageResource(R.drawable.menu);
                }
                else{
                    setupRecyclerViewList();
                    if(v instanceof ImageButton) ((ImageButton) v).setImageResource(R.drawable.list);

                }

            }
        });
        cameraBtn = topMenu.findViewById(R.id.menu_camera_btn);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });
        recyclerView = findViewById(R.id.main_recycler_view);
        if (gridMode) {
            setupRecyclerViewGrid();
        } else {
            setupRecyclerViewList();
        }
        recyclerView.getLayoutManager().onRestoreInstanceState(listState);

        folderBtn = topMenu.findViewById(R.id.menu_folder_btn);
        folderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

    }

    private void getImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //File photoFile;
        //photoFile = createPhotoFile();
        //String pathToFile = photoFile.getAbsolutePath();
        //if(photoFile != null){
            //Uri photoUri = FileProvider.getUriForFile(MainActivity.this,"com.jonatan_vahlberg.gallery.fileprovider", photoFile);
            //intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
            startActivityForResult(intent,IMAGE_FROM_FOLDER_REQUSET);
        //}
    }



    private void setupRecyclerViewGrid() {
        Display display = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        int cols = (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180)? 3:4;
        adapter = new RecyclerViewAdapter(this,gridMode);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new GridLayoutManager(this,cols,GridLayoutManager.VERTICAL,false));
    }
    private void setupRecyclerViewList() {
        adapter = new RecyclerViewAdapter(this,gridMode);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_GRIDMODE,gridMode);
        outState.putParcelable(STATE_LIST,recyclerView.getLayoutManager().onSaveInstanceState());
        //outState.putSerializable(STATE_LIST,mList);
    }

    private void captureImage() {

            Intent captureNewImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(captureNewImageIntent.resolveActivity(getPackageManager()) != null){
                File photoFile = null;
                photoFile =  createPhotoFile();

                if(photoFile != null){
                    String pathToFile = photoFile.getAbsolutePath();
                    Uri photoUri = FileProvider.getUriForFile(MainActivity.this,"com.jonatan_vahlberg.gallery.fileprovider", photoFile);
                    captureNewImageIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);

                    startActivityForResult(captureNewImageIntent,CAPTURE_IMAGE_REQUEST);
                }
            }


    }

    private File createPhotoFile() {
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        currentFileName = name;
        String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+GALLERY_SUBFOLDER;
        File storageDir = new File(dir) ;
        File image = null;
        try {
            image = File.createTempFile(name,".jpg",storageDir);
        } catch (IOException e) {
            Log.d("FILE_CREATION", "createPhotoFile: "+e);
        }
        return image;
    }

    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode == RESULT_OK){

            if(requestCode == CAPTURE_IMAGE_REQUEST){
                Singleton.shared.addToList(new ImageObject(currentFileName,""));
            }
            else if(requestCode == IMAGE_FROM_FOLDER_REQUSET){
                File intputFile = new File(data.getData().toString());
                File outputFile = new File(Globals.IMAGE_DIRECTORY_PATH+"/"+intputFile.getName()+".jpg");
                try{
                    InputStream inputStream = getContentResolver()
                            .openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(
                            outputFile);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();

                }catch (Exception e){
                }
                if(!(outputFile == null)){
                    Singleton.shared.addToList(new ImageObject(currentFileName,""));

                }
            }
            Singleton.shared.load(this);
            recyclerView.getAdapter().notifyDataSetChanged();
        }

    }
}
