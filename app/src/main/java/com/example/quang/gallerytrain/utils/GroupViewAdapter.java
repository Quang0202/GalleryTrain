package com.example.quang.gallerytrain.utils;

import android.content.Context;
import android.os.Build;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quang.gallerytrain.R;
import com.example.quang.gallerytrain.activitys.MainActivity;
import com.example.quang.gallerytrain.fragment.Fragment_Image_Browser;
import com.example.quang.gallerytrain.models.Groups;
import com.example.quang.gallerytrain.models.Images;

import java.util.ArrayList;

public class GroupViewAdapter extends RecyclerView.Adapter<GroupViewAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<Groups>groups;
    private RecyclerView.RecycledViewPool recycledViewPool= new RecyclerView.RecycledViewPool();
    private final itemClickListener picListerner;
    public GroupViewAdapter(Context mContext, ArrayList<Groups> groups,itemClickListener picListerner) {
        this.mContext = mContext;
        this.groups = groups;
        this.picListerner=picListerner;
    }


    @NonNull
    @Override
    public GroupViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.group_image_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewAdapter.ViewHolder holder, int position) {
        Groups group= groups.get(position);
        holder.mlableDay.setText(group.getDays());
        holder.mlableMonth.setText(group.getMonth());
        //LinearLayoutManager layoutManager= new LinearLayoutManager(holder.imageList.getContext(),LinearLayoutManager.HORIZONTAL,false);
        imageAdapter image= new imageAdapter(group.getImages(),mContext,picListerner);
        //holder.imageList.setLayoutManager(layoutManager);
        holder.imageList.addItemDecoration(new MarginDecoration(mContext));
        holder.imageList.hasFixedSize();
        holder.imageList.setAdapter(image);
        //holder.imageList.setRecycledViewPool(recycledViewPool);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mlableDay;
        private TextView mlableMonth;
        private RecyclerView imageList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mlableDay = itemView.findViewById(R.id.label_day);
            mlableMonth=itemView.findViewById(R.id.lable_month);
            imageList=itemView.findViewById(R.id.autoFitRecycler);

        }

    }
}
