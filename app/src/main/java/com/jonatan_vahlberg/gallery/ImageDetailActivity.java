package com.jonatan_vahlberg.gallery;

import android.hardware.display.DisplayManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ImageDetailActivity extends AppCompatActivity {
    private ImageView detailImage;
    private TextView detail_title;
    private View detail_topBar;
    private ImageButton detailDelete;
    private TextView topBarText;
    public int list_index;

    private float x1, x2, xCurrent;
    private final  int SWIPE_MIN = 150;
    private boolean left;
    private float alpha = 1.0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        detailImage = findViewById(R.id.detail_image);
        detail_title = findViewById(R.id.detail_title);
        detail_topBar = findViewById(R.id.detail_top_bar);
        topBarText = detail_topBar.findViewById(R.id.detail_menu_title);
        detailDelete = detail_topBar.findViewById(R.id.detail_menu_delete);

        if (getIntent().hasExtra(Globals.INDEX_INTENT)) {
            list_index = getIntent().getIntExtra(Globals.INDEX_INTENT, 0);
            load();
        }

        detailDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.shared.addToDeleteList(Singleton.shared.getFromList(list_index).getId());
                Singleton.shared.showDeleteDialogue(ImageDetailActivity.this);
            }
        });

    }

    public void load(){
        topBarText.setText((list_index+1)+" / "+Singleton.shared.getListSize());
        ImageObject imageObject = Singleton.shared.getFromList(list_index);
        Glide.with(this)
                .load(Globals.IMAGE_DIRECTORY_PATH + "/" + imageObject.getTitle())
                .fitCenter()
                .into(detailImage);
        detail_title.setText(imageObject.getTitle());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if(deltaX > SWIPE_MIN){
                    if((list_index - 1) < 0){
                        list_index = 0;
                    }
                    else{
                        list_index--;
                    }

                    load();
                }
                else if(deltaX < (SWIPE_MIN*-1) ){
                    if((list_index+1) >= Singleton.shared.getListSize()){
                        list_index = Singleton.shared.getListSize() -1;
                    }
                    else{
                        list_index++;
                    }
                    load();
                }
                detailImage.setAlpha(1.0f);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("movement", "onTouchEvent: MOVING");
                if((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE){


                    xCurrent = event.getX();
                    if(xCurrent < x1){
                        if(!left){
                            x1 = xCurrent;
                        }
                        left = true;
                    }
                    else{
                        if(left){
                            x1 = xCurrent;
                        }
                        left = false;
                    }
                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    float deltaAbs;
                    float procentile = 0.0f;
                    if(left){
                        deltaAbs = Math.abs( 0 - x1);
                        procentile = xCurrent / deltaAbs;
                    }
                    else{
                        //Take the procentile and reverse it to get correct value.
                        deltaAbs = Math.abs((xCurrent - x1));
                        procentile = 1f -(deltaAbs / metrics.widthPixels);

                    }




                    detailImage.setAlpha(procentile);

                }
        }


        return super.onTouchEvent(event);


    }
}
