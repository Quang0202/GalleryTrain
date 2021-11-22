package com.example.quang.gallerytrain.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormatSymbols;
import java.util.*;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

import com.example.quang.gallerytrain.R;
import com.example.quang.gallerytrain.activitys.ImageBrower;
import com.example.quang.gallerytrain.activitys.MainActivity;
import com.example.quang.gallerytrain.models.Albums;
import com.example.quang.gallerytrain.models.Groups;
import com.example.quang.gallerytrain.models.Images;
import com.example.quang.gallerytrain.utils.GetImages;
import com.example.quang.gallerytrain.adapters.GroupViewAdapter;
import com.example.quang.gallerytrain.adapters.PicHolder;
import com.example.quang.gallerytrain.utils.itemClickListener;

import java.util.ArrayList;
import java.util.Collections;

public class FragmentPictures extends Fragment implements itemClickListener {
    private RecyclerView imageRecycler;
    private static int firstVisibleInListview;
    private ArrayList<Images> allpictures;
    private ProgressBar load;
    private ImageView listBy;
    private GetImages getImages;
    private Context applicationContext ;
    private CardView head;
    boolean allowRefresh;
    int opTionList=0;
    public FragmentPictures() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pictures, container, false);
        allowRefresh=false;
        allpictures = new ArrayList<>();
        getImages= new GetImages();
        listBy=view.findViewById(R.id.list_by);
        setHasOptionsMenu(true);
        applicationContext= MainActivity.getContextOfApplication();
        imageRecycler = view.findViewById(R.id.recycler);
        head=view.findViewById(R.id.head);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(applicationContext);
        ArrayList<Albums> albums = getImages.getPicturePaths();
        load = view.findViewById(R.id.loader);
        if(allpictures.isEmpty()) {
            allpictures = getImages.getAllImages();
            load.setVisibility(View.VISIBLE);
            Collections.sort(allpictures, new Comparator<Images>() {
                @Override
                public int compare(Images images, Images t1) {
                    if (images.getTimeTamp() > t1.getTimeTamp()) {
                        return -1;
                    } else if (images.getTimeTamp() < t1.getTimeTamp()) {
                        return 1;
                    }
                    return 0;
                }
            });
            ArrayList<Groups> groups= new ArrayList<>();
            groups=createGroups(allpictures);
            imageRecycler.setAdapter(new GroupViewAdapter(applicationContext,groups,this));
            imageRecycler.setLayoutManager(layoutManager);
            firstVisibleInListview = layoutManager.findFirstVisibleItemPosition();
            load.setVisibility(View.GONE);
        }
        listBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
        return view;
    }
    public void showPopupMenu(View view) {
        android.widget.PopupMenu popup = new PopupMenu(applicationContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.list_by_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.week:
                        opTionList=0;
                        onResume();
                        return true;
                    case R.id.month:
                        opTionList=1;
                        onResume();
                        return true;
                    case R.id.year:
                        opTionList=3;
                        onResume();
                        return true;
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        imageRecycler.getAdapter().notifyDataSetChanged();
        if(allowRefresh){
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
        allowRefresh=true;
    }

    @Override
    public void onPicClicked(PicHolder holder, int position, ArrayList<Images> pics) {
        Intent intent= new Intent(getActivity(), ImageBrower.class);
        Bundle args= new Bundle();
        args.putSerializable("IMAGES",pics);
        intent.putExtra("BUNDLE",args);
        intent.putExtra("POSITION",position);
        startActivity(intent);
    }

    @Override
    public void onPicClicked(String pictureFolderPath, String folderName) {

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
    public boolean checkGroup(long time1, long time2,int mounth1, int mounth2, int year1, int year2){
        if(opTionList==0){
            return time1-time2<604800000&& mounth1==mounth2;
        }
        else if(opTionList==1){
            return mounth1==mounth2&& year1==year2;
        }else{
            return year1==year2;
        }
    }
   public ArrayList<Groups> createGroups(ArrayList<Images> allpictures){
       ArrayList<Groups> groups=new ArrayList<>();
       ArrayList<Images>temp=new ArrayList<>();
       Groups group= new Groups();
       String labelDate1="";
       String labelDate2="";
       long step=0;
       int numMonth1=0;
       int numMonth2=0;
       boolean flag=false;
       int year=Calendar.getInstance().get(Calendar.YEAR);
       int numYear1=0;
       int numYear2=0;
       for(Images image:allpictures){
           if(temp.isEmpty()){
               temp.add(image);
               step=image.getTimeTamp();
               Calendar cal = Calendar.getInstance(Locale.ENGLISH);
               cal.setTimeInMillis(image.getTimeTamp());
               int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
               numMonth1=cal.get(Calendar.MONTH);
               numYear1=cal.get(Calendar.YEAR);
               labelDate1 = String.valueOf(dayOfMonth);
               labelDate2="";
           }
           else{
               Calendar cal = Calendar.getInstance(Locale.ENGLISH);
               cal.setTimeInMillis(image.getTimeTamp());
               numMonth2=cal.get(Calendar.MONTH);
               numYear2=cal.get(Calendar.YEAR);
               if(checkGroup(step,image.getTimeTamp(),numMonth1,numMonth2,numYear1,numYear2)){
                   temp.add(image);
                   cal.setTimeInMillis(image.getTimeTamp());
                   int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                   labelDate2 = String.valueOf(dayOfMonth);
               }
               else{
                   group.setImages(temp);
                   String month=getMonthForInt(numMonth1);
                   temp = new ArrayList<>();
                   if(opTionList==0) {
                       if (numYear1 != year) {
                           group.setMonth(month + ", " + numYear1);
                       } else {
                           group.setMonth(month);
                       }
                       if (labelDate1.compareTo(labelDate2) == 0 || labelDate2.compareTo("") == 0) {
                           group.setDays(labelDate1 + ",");
                       } else {
                           group.setDays(labelDate2 + " ~ " + labelDate1 + ",");
                       }
                   }
                   else if(opTionList==1){
                       if (numYear1 != year) {
                           group.setMonth(month + ", " + numYear1);
                       } else {
                           group.setMonth(month);
                       }
                       group.setDays("");
                   }
                   else{
                       group.setMonth(""+numYear1);
                       group.setDays("");
                   }
                   groups.add(group);
                   group=new Groups();
                   temp.add(image);
                   step=image.getTimeTamp();
                   cal.setTimeInMillis(image.getTimeTamp());
                   int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                   numMonth1=numMonth2;
                   numYear1=numYear2;
                   labelDate1 = String.valueOf(dayOfMonth);
                   labelDate2="";
               }
           }
       }
       group.setImages(temp);
       String month=getMonthForInt(numMonth1);
       temp = new ArrayList<>();
       if(opTionList==0) {
           if (numYear1 != year) {
               group.setMonth(month + ", " + numYear1);
           } else {
               group.setMonth(month);
           }
           if (labelDate1.compareTo(labelDate2) == 0 || labelDate2.compareTo("") == 0) {
               group.setDays(labelDate1 + ",");
           } else {
               group.setDays(labelDate2 + " ~ " + labelDate1 + ",");
           }
       }
       else if(opTionList==1){
           if (numYear1 != year) {
               group.setMonth(month + ", " + numYear1);
           } else {
               group.setMonth(month);
           }
           group.setDays("");
       }
       else{
           group.setMonth(""+numYear1);
           group.setDays("");
       }
       groups.add(group);
       return groups;
   }

}
