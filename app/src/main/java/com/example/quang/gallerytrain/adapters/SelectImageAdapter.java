package com.example.quang.gallerytrain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.quang.gallerytrain.R;
import com.example.quang.gallerytrain.models.Images;
import com.example.quang.gallerytrain.utils.itemClickListener;

import java.util.ArrayList;

import static androidx.core.view.ViewCompat.setTransitionName;

public class SelectImageAdapter extends RecyclerView.Adapter<SelectImageAdapter.ViewHolder> {
    private ArrayList<Images> pictureList;
    private String folderName;
    private Context pictureContx;
    private itemClickListener picListerner ;
    public SelectImageAdapter(Context mContext, ArrayList<Images> pictureList, String folderName, itemClickListener picListerner) {
        this.pictureContx = mContext;
        this.pictureList = pictureList;
        this.folderName=folderName;
        this.picListerner=picListerner;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(pictureContx);
        View view = inflater.inflate(R.layout.select_image_item, parent, false);
        SelectImageAdapter.ViewHolder viewHolder = new SelectImageAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Images image = pictureList.get(position);

        Glide.with(pictureContx)
                .load(image.getImagePath())
                .apply(new RequestOptions().centerCrop())
                .into(holder.picture);

        setTransitionName(holder.picture, String.valueOf(position) + "_image");
        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( holder.selected.getVisibility()==View.GONE) {
                    holder.selected.setVisibility(View.VISIBLE);
                    picListerner.onpicClicked(pictureList.get(position),true);
                }else{
                    holder.selected.setVisibility(View.GONE);
                    picListerner.onpicClicked(pictureList.get(position),false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture;
        public ImageView selected;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            picture= itemView.findViewById(R.id.image);
            selected= itemView.findViewById(R.id.selected);
        }
    }
}
