package com.example.quang.gallerytrain.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
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
import com.example.quang.gallerytrain.models.Images;
import com.example.quang.gallerytrain.utils.itemClickListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import static androidx.core.view.ViewCompat.setTransitionName;

public class SelectAlbumAdapter  extends  RecyclerView.Adapter<SelectAlbumAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<Albums> albums;
    private Images image;
    private RecyclerView.RecycledViewPool recycledViewPool= new RecyclerView.RecycledViewPool();
    private final itemClickListener picListerner;
    public SelectAlbumAdapter(Context mContext, ArrayList<Albums> albums, itemClickListener picListerner, Images image) {
        this.mContext = mContext;
        this.albums = albums;
        this.picListerner=picListerner;
        this.image=image;
    }


    @NonNull
    @Override
    public SelectAlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.select_album_item, parent, false);
        SelectAlbumAdapter.ViewHolder viewHolder = new SelectAlbumAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectAlbumAdapter.ViewHolder holder, int position) {
        final Albums album = albums.get(position);
        holder.name.setText(album.getAlbumName());
        holder.numPic.setText(String.valueOf(album.getNumberOfPics()));
        Glide.with(mContext)
                .load(album.getFirstPic())
                .apply(new RequestOptions().centerCrop())
                .into(holder.imageView);

        setTransitionName(holder.imageView, String.valueOf(position) + "_image");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final File forder = new File(album.getPath());
                final File imageFile = new File(image.getImagePath());
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Creating album");
                String[] selection = {"Move", "Copy"};
                builder.setItems(selection, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (forder.exists() && imageFile.exists()) {
                                    try {
                                        copyOrMoveFile(imageFile, forder, true);
                                        dialog.dismiss();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            case 1:
                                if (forder.exists() && imageFile.exists()) {
                                    try {
                                        copyOrMoveFile(imageFile, forder, false);
                                        dialog.dismiss();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                        }
                    }
                });
                builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
            }
        });
    }
    private void copyOrMoveFile(File file, File dir, boolean isCopy) throws IOException {
        File newFile = new File(dir, file.getName());
        FileChannel outChannel = null;
        FileChannel inputChannel = null;
        try {
            outChannel = new FileOutputStream(newFile).getChannel();
            inputChannel = new FileInputStream(file).getChannel();
            outChannel.transferFrom(inputChannel, 0, inputChannel.size());
            Log.d("move to file","ok");
            if(!isCopy)
                file.delete();
        } finally {
            if (inputChannel != null) inputChannel.close();
            if (outChannel != null) outChannel.close();
        }
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView name;
        private TextView numPic;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image);
            name=itemView.findViewById(R.id.text_name);
            numPic=itemView.findViewById(R.id.text_size);
            cardView=itemView.findViewById(R.id.card_item);
        }

    }
}
