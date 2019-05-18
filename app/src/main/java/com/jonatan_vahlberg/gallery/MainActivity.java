package com.jonatan_vahlberg.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayout topMenu;
    ImageButton cameraBtn, folderBtn, modeBtn;
    private ArrayList<ImageObject> mList = new ArrayList<>();

    private static final String STATE_GRIDMODE = "gridMode";
    private static final String STATE_LIST = "listState";
    private Parcelable listState;
    //private int currentRecyclePosition = 0;
    private boolean gridMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState != null){
            recoverSavedInstances(savedInstanceState);
        }
        mList.add(new ImageObject("First","19"));
        mList.add(new ImageObject("Second","19"));
        mList.add(new ImageObject("Third","19"));
        mList.add(new ImageObject("Fourth","19"));

        mList.add(new ImageObject("First","19"));
        mList.add(new ImageObject("Second","19"));
        mList.add(new ImageObject("Third","19"));
        mList.add(new ImageObject("Fourth","19"));

        mList.add(new ImageObject("First","19"));
        mList.add(new ImageObject("Second","19"));
        mList.add(new ImageObject("Third","19"));
        mList.add(new ImageObject("Fourth","19"));

        mList.add(new ImageObject("First","19"));
        mList.add(new ImageObject("Second","19"));
        mList.add(new ImageObject("Third","19"));
        mList.add(new ImageObject("Fourth","19"));

        mList.add(new ImageObject("First","19"));
        mList.add(new ImageObject("Second","19"));
        mList.add(new ImageObject("Third","19"));
        mList.add(new ImageObject("Fourth","19"));

        mList.add(new ImageObject("First","19"));
        mList.add(new ImageObject("Second","19"));
        mList.add(new ImageObject("Third","19"));
        mList.add(new ImageObject("Fourth","19"));
        setupLayout();


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
        recyclerView = findViewById(R.id.main_recycler_view);
        if (gridMode) {
            setupRecyclerViewGrid();
        } else {
            setupRecyclerViewList();
        }
        recyclerView.getLayoutManager().onRestoreInstanceState(listState);

    }

    private void setupRecyclerViewGrid() {
        Display display = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        int cols = (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180)? 3:4;
        RecyclerView.Adapter adapter = new RecyclerViewAdapter(this,mList,gridMode);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new GridLayoutManager(this,cols,GridLayoutManager.VERTICAL,false));
    }
    private void setupRecyclerViewList() {
        RecyclerView.Adapter adapter = new RecyclerViewAdapter(this,mList,gridMode);
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


}
