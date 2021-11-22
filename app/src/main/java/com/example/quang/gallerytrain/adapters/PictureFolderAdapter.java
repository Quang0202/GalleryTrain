package com.example.quang.gallerytrain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.quang.gallerytrain.R;
import com.example.quang.gallerytrain.models.Albums;
import com.example.quang.gallerytrain.utils.itemClickListener;

import java.util.ArrayList;

/**
 * Author CodeBoy722
 *
 * An adapter for populating RecyclerView with items representing folders that contain images
 */
public class PictureFolderAdapter extends RecyclerView.Adapter<PictureFolderAdapter.FolderHolder>{

    private ArrayList<Albums> folders;
    private Context folderContx;
    private itemClickListener listenToClick;

    /**
     *
     * @param folders An ArrayList of String that represents paths to folders on the external storage that contain pictures
     * @param folderContx The Activity or fragment Context
     * @param listen interFace for communication between adapter and fragment or activity
     */
    public PictureFolderAdapter(ArrayList<Albums> folders, Context folderContx, itemClickListener listen) {
        this.folders = folders;
        this.folderContx = folderContx;
        this.listenToClick = listen;
    }

    @NonNull
    @Override
    public FolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cell = inflater.inflate(R.layout.picture_album_item, parent, false);
        return new FolderHolder(cell);

    }

    @Override
    public void onBindViewHolder(@NonNull FolderHolder holder, int position) {
        final Albums folder = folders.get(position);

        Glide.with(folderContx)
                .load(folder.getFirstPic())
                .apply(new RequestOptions().centerCrop())
                .into(holder.folderPic);

        //setting the number of images
            String text = "" + folder.getAlbumName();
            String folderSizeString = "" + folder.getNumberOfPics() + " Media";
            holder.folderSize.setText(folderSizeString);
            holder.folderName.setText(text);


        holder.folderPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenToClick.onPicClicked(folder.getPath(),folder.getAlbumName());
            }
        });
        holder.folderPic.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View view) {
                listenToClick.onPicLongClicked(folder.getPath(),folder.getAlbumName());
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return folders.size();
    }


    public class FolderHolder extends RecyclerView.ViewHolder{
        ImageView folderPic;
        TextView folderName;
        //set textview for foldersize
        TextView folderSize;

        CardView folderCard;

        public FolderHolder(@NonNull View itemView) {
            super(itemView);
            folderPic = itemView.findViewById(R.id.folderPic);
            folderName = itemView.findViewById(R.id.folderName);
            folderSize=itemView.findViewById(R.id.folderSize);
            folderCard = itemView.findViewById(R.id.folderCard);
        }
    }

}
