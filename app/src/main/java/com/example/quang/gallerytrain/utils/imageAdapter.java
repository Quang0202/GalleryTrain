package com.example.quang.gallerytrain.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.quang.gallerytrain.models.Images;
import com.example.quang.gallerytrain.R;

import java.util.ArrayList;

import static androidx.core.view.ViewCompat.setTransitionName;

public class imageAdapter extends RecyclerView.Adapter<PicHolder> {
    private ArrayList<Images> pictureList;
    private Context pictureContx;
    private final itemClickListener picListerner;
    public imageAdapter(ArrayList<Images> pictureList, Context pictureContx,itemClickListener picListerner) {
        this.pictureList = pictureList;
        this.pictureContx = pictureContx;
        this.picListerner = picListerner;
    }
    @NonNull
    @Override
    public PicHolder onCreateViewHolder(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View cell = inflater.inflate(R.layout.pic_holder_item, container, false);
        return new PicHolder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull final PicHolder holder, final int position) {
        final Images image = pictureList.get(position);
        Glide.with(pictureContx)
                .load(image.getImageUri())
                .apply(new RequestOptions().centerCrop())
                .into(holder.picture);
        setTransitionName(holder.picture, String.valueOf(position) + "_image");
        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picListerner.onPicClicked(holder,position, pictureList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }
}
