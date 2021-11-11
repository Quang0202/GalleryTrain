package com.example.quang.gallerytrain.fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.transition.Fade;
import android.util.Log;

import java.text.DateFormatSymbols;
import java.util.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.quang.gallerytrain.R;
import com.example.quang.gallerytrain.activitys.MainActivity;
import com.example.quang.gallerytrain.models.Albums;
import com.example.quang.gallerytrain.models.Groups;
import com.example.quang.gallerytrain.models.Images;
import com.example.quang.gallerytrain.utils.GetImages;
import com.example.quang.gallerytrain.utils.GroupViewAdapter;
import com.example.quang.gallerytrain.utils.MarginDecoration;
import com.example.quang.gallerytrain.utils.PicHolder;
import com.example.quang.gallerytrain.utils.imageAdapter;
import com.example.quang.gallerytrain.utils.itemClickListener;

import java.util.ArrayList;
import java.util.Collections;

public class Fragment_Pictures extends Fragment implements itemClickListener {
    RecyclerView imageRecycler;
    ArrayList<Images> allpictures;
    ProgressBar load;
    GetImages getImages;
    Context applicationContext ;
    public Fragment_Pictures() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pictures, container, false);
        allpictures = new ArrayList<>();
        getImages= new GetImages();
        applicationContext= MainActivity.getContextOfApplication();
        imageRecycler = view.findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(applicationContext);
        ArrayList<Albums> albums = getImages.getPicturePaths();
        load = view.findViewById(R.id.loader);
        if(allpictures.isEmpty()){
            for(Albums temp: albums){
                ArrayList<Images> pictures= getImages.getAllImages(temp.getPath());
                allpictures.addAll(pictures);
            }
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
            boolean flag=false;
            for(Images image:allpictures){
                if(temp.isEmpty()){
                    temp.add(image);
                    flag=true;
                    step=image.getTimeTamp();
                    cal.setTimeInMillis(image.getTimeTamp());
                    int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                    numMonth=cal.get(Calendar.MONTH);
                     labelDate1 = String.valueOf(dayOfMonth);
                }
                else{
                    cal.setTimeInMillis(image.getTimeTamp());
                    int numMonth2=cal.get(Calendar.MONTH);
                    flag=false;
                    if(step-image.getTimeTamp()<604800000 && numMonth==numMonth2){
                        temp.add(image);
                        cal.setTimeInMillis(image.getTimeTamp());
                        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                        labelDate2 = String.valueOf(dayOfMonth);
                    }
                    else{
                        group.setImages(temp);
                        String month=getMonthForInt(numMonth);
                        group.setMonth(month);
                        temp= new ArrayList<>();
                        if(labelDate1.compareTo(labelDate2)==0){
                            group.setDays(labelDate1+",");
                        }else{
                            group.setDays(labelDate2+" ~ "+labelDate1+",");
                        }
                        groups.add(group);
                        group=new Groups();
                        temp=new ArrayList<>();
                    }
                }
            }
            if(flag){
                group.setImages(temp);
                group.setDays(labelDate1);
                group.setMonth(getMonthForInt(numMonth));
                groups.add(group);
            }
            imageRecycler.setAdapter(new GroupViewAdapter(applicationContext,groups,this));
            imageRecycler.setLayoutManager(layoutManager);
            load.setVisibility(View.GONE);
        }else{

        }
        return view;
    }

    @Override
    public void onPicClicked(PicHolder holder, int position, ArrayList<Images> pics) {
        Fragment_Image_Browser browser = Fragment_Image_Browser.newInstance(pics,position,applicationContext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //browser.setEnterTransition(new Slide());
            //browser.setExitTransition(new Slide());
            browser.setEnterTransition(new Fade());
            browser.setExitTransition(new Fade());
        }
        getChildFragmentManager()
                .beginTransaction()
                .addSharedElement(holder.picture, position+"picture")
                .add(R.id.container_image, browser)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onPicClicked(String pictureFolderPath, String folderName) {

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
