package com.example.quang.gallerytrain.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.media.ExifInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.example.quang.gallerytrain.R;
import com.example.quang.gallerytrain.utils.OnSwipeTouchListener;
import com.google.android.material.timepicker.TimeFormat;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;

public class DetailsImage extends AppCompatActivity {
    TextView nameImage;
    TextView dateImage;
    TextView timeImage;
    TextView sizeImage;
    TextView pathImage;
    ConstraintLayout constraintLayout;
    TextView deviceImage;
    TextView detailDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_image);
        Intent intent = getIntent();
        String path= intent.getStringExtra("PATH");
        String name= intent.getStringExtra("NAME");
        File fileImage= new File(path);
        long fileSizeInBytes = fileImage.length();
        double fileSize= (double)fileSizeInBytes / 1024;
        nameImage= findViewById(R.id.name);
        nameImage.setText(name);
        dateImage= findViewById(R.id.date);
        timeImage=findViewById(R.id.time);
        pathImage= findViewById(R.id.pathImage);
        constraintLayout=findViewById(R.id.layoutDetail);
        pathImage.setText(path);
        sizeImage= findViewById(R.id.size);
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(intent.getLongExtra("TIMESTAMP",0));
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        String time = DateFormat.format("kk:mm", cal).toString();
        dateImage.setText(date);
        timeImage.setText(time);
        if(fileSize>1000){
            fileSize=fileSize/1024;
            sizeImage.setText(new DecimalFormat("#.0#").format(fileSize)+"Mb");
        }
        else
            sizeImage.setText(new DecimalFormat("#.0#").format(fileSize)+"Kb");
        constraintLayout.setOnTouchListener(new OnSwipeTouchListener(this){
            public void onSwipeTop() {

            }
            public void onSwipeBottom() {
                finish();
                overridePendingTransition( R.anim.slide_in_down,R.anim.slide_out_down);
            }
        });
    }
}
