package com.example.quang.gallerytrain.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.quang.gallerytrain.R;
import com.example.quang.gallerytrain.models.Albums;
import com.example.quang.gallerytrain.models.Groups;
import com.example.quang.gallerytrain.models.Images;
import com.example.quang.gallerytrain.utils.GetImages;
import com.example.quang.gallerytrain.adapters.GroupViewAdapter;
import com.example.quang.gallerytrain.adapters.PicHolder;
import com.example.quang.gallerytrain.utils.itemClickListener;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class ShowImagesAlbum extends AppCompatActivity implements itemClickListener {
    RecyclerView imageRecycler;
    ArrayList<Images> allpictures;
    ArrayList<Images>allpicturesLocal;
    ProgressBar load;
    GetImages getImages;
    String foldePath;
    ImageView back;
    TextView folderName;
    boolean allowRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images_album);
        allpictures = new ArrayList<>();
        getImages= new GetImages();
        allowRefresh=false;
        load = findViewById(R.id.loader);
        imageRecycler=findViewById(R.id.imageRecycle);
        folderName = findViewById(R.id.foldername);
        back=findViewById(R.id.back);
        folderName.setText(getIntent().getStringExtra("folderName"));
        foldePath =  getIntent().getStringExtra("folderPath");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        allpictures = getImages.getImagesWithPath(foldePath);
        if(!allpictures.isEmpty()){
            load.setVisibility(View.VISIBLE);
            Collections.sort(allpictures, new Comparator<Images>() {
                @Override
                public int compare(Images images, Images t1) {
                    if(images.getTimeTamp()>t1.getTimeTamp()){
                        return -1;
                    }else if(images.getTimeTamp()<t1.getTimeTamp()){
                        return 1;
                    }
                    return 0;
                }
            });
            ArrayList<Groups> groups=new ArrayList<>();
            ArrayList<Images>temp=new ArrayList<>();
            Groups group= new Groups();
            String labelDate1="";
            String labelDate2="";
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            long step=0;
            int numMonth=0;
            int year=Calendar.getInstance().get(Calendar.YEAR);
            int numYear=0;
            boolean flag=false;
            for(Images image:allpictures){
                if(temp.isEmpty()){
                    temp.add(image);
                    step=image.getTimeTamp();
                    cal.setTimeInMillis(image.getTimeTamp());
                    int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                    numMonth=cal.get(Calendar.MONTH);
                    numYear=cal.get(Calendar.YEAR);
                    labelDate1 = String.valueOf(dayOfMonth);
                    labelDate2="";
                }
                else{
                    cal.setTimeInMillis(image.getTimeTamp());
                    int numMonth2=cal.get(Calendar.MONTH);
                    if(step-image.getTimeTamp()<604800000 && numMonth==numMonth2){
                        temp.add(image);
                        cal.setTimeInMillis(image.getTimeTamp());
                        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                        labelDate2 = String.valueOf(dayOfMonth);
                    }
                    else{
                        group.setImages(temp);
                        String month=getMonthForInt(numMonth);
                        if(numYear!=year){
                            group.setMonth(month+", "+numYear);
                        }else{
                            group.setMonth(month);
                        }
                        temp= new ArrayList<>();
                        if(labelDate1.compareTo(labelDate2)==0||labelDate2.compareTo("")==0){
                            group.setDays(labelDate1+",");
                        }else{
                            group.setDays(labelDate2+" ~ "+labelDate1+",");
                        }
                        groups.add(group);
                        group=new Groups();
                        temp.add(image);
                        step=image.getTimeTamp();
                        cal.setTimeInMillis(image.getTimeTamp());
                        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                        numMonth=cal.get(Calendar.MONTH);
                        numYear=cal.get(Calendar.YEAR);
                        labelDate1 = String.valueOf(dayOfMonth);
                        labelDate2="";
                    }
                }
            }
            group.setImages(temp);
            if(labelDate1.compareTo(labelDate2)==0||labelDate2.compareTo("")==0){
                group.setDays(labelDate1+",");
            }else{
                group.setDays(labelDate2+" ~ "+labelDate1+",");
            }
            String month=getMonthForInt(numMonth);
            if(numYear!=year){
                group.setMonth(month+", "+numYear);
            }else{
                group.setMonth(month);
            }
            groups.add(group);
            imageRecycler.setAdapter(new GroupViewAdapter(ShowImagesAlbum.this,groups,this));
            imageRecycler.setLayoutManager(layoutManager);
            load.setVisibility(View.GONE);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(allowRefresh){
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
        allowRefresh=true;

    }

    @Override
    public void onPicClicked(PicHolder holder, int position, ArrayList<Images> pics) {
        Intent intent= new Intent(this, ImageBrower.class);
        Bundle args= new Bundle();
        args.putSerializable("IMAGES",pics);
        intent.putExtra("BUNDLE",args);
        intent.putExtra("POSITION",position);
        startActivity(intent);

    }

    @Override
    public void onPicClicked(String imageAlbumPath, String albumName) {

    }

    @Override
    public void onpicClicked(Images images, boolean isAdd) {

    }

    @Override
    public void onpicClicked(int position, ArrayList<Albums> albums) {

    }

    @Override
    public void onPicLongClicked(String imageAlbumPath, String albumName) {

    }

    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }
}
