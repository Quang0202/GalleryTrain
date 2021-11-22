package com.example.quang.gallerytrain.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.quang.gallerytrain.R;
import com.example.quang.gallerytrain.models.Albums;
import com.example.quang.gallerytrain.models.Images;
import com.example.quang.gallerytrain.utils.GetImages;
import com.example.quang.gallerytrain.adapters.PicHolder;
import com.example.quang.gallerytrain.utils.itemClickListener;
import com.example.quang.gallerytrain.adapters.SelectImageAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class CreateAlbum extends AppCompatActivity implements itemClickListener {
    private ArrayList<Images> allImages;
    private ArrayList<Images> picturesNewFolder;
    private GetImages getImages;
    private ImageView close;
    private ImageView select;
    private RecyclerView imageRecycler;
    private ProgressBar load;
    String folderName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(CreateAlbum.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (ContextCompat.checkSelfPermission(CreateAlbum.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        setContentView(R.layout.activity_create_album);
        imageRecycler=findViewById(R.id.recycler);
        picturesNewFolder= new ArrayList<>();
        close=findViewById(R.id.close);
        select=findViewById(R.id.check);
        load= findViewById(R.id.loader);
        Intent intent= getIntent();
        folderName=intent.getStringExtra("FOlDERNAME");
        allImages= new ArrayList<>();
        getImages= new GetImages();
        if(allImages.isEmpty()){
            load.setVisibility(View.VISIBLE);
            allImages=getImages.getAllImages();
            imageRecycler.setAdapter(new SelectImageAdapter(CreateAlbum.this,allImages,folderName, this));
            load.setVisibility(View.GONE);
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final File f = new File(Environment.getExternalStorageDirectory(), folderName);
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateAlbum.this);
                builder.setTitle("Creating album");
                String[] selection = {"Move", "Copy"};
                builder.setItems(selection, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (!f.exists()) {
                                    f.mkdirs();
                                    for(Images image:picturesNewFolder){
                                        File imageFile= new File(image.getImagePath());
                                        if(imageFile.exists()){
                                            try {
                                                copyOrMoveFile(imageFile,f,false);
                                                finish();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                                Toast.makeText(CreateAlbum.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                }
                                break;
                            case 1:
                                if (!f.exists()) {
                                    f.mkdirs();
                                    for(Images image:picturesNewFolder){
                                        File imageFile= new File(image.getImagePath());
                                        if(imageFile.exists()){
                                            try {
                                                copyOrMoveFile(imageFile,f,true);
                                                finish();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                                Toast.makeText(CreateAlbum.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
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
                if (picturesNewFolder.size()>0) {
                    AlertDialog dialog = builder.create();
                    dialog.setCancelable(false);
                    dialog.show();
                }
            }
        });
    }
    private void copyOrMoveFile(File file, File dir,boolean isCopy) throws IOException {
        File newFile = new File(dir, file.getName());
        FileChannel outChannel = null;
        FileChannel inputChannel = null;
        try {
            outChannel = new FileOutputStream(newFile).getChannel();
            inputChannel = new FileInputStream(file).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outChannel);
            inputChannel.close();
            if(!isCopy)
                file.delete();
        } finally {
            if (inputChannel != null) inputChannel.close();
            if (outChannel != null) outChannel.close();
        }
    }
    @Override
    public void onPicClicked(PicHolder holder, int position, ArrayList<Images> pics) {

    }

    @Override
    public void onPicClicked(String imageAlbumPath, String albumName) {

    }

    @Override
    public void onpicClicked(Images images, boolean isAdd) {
        if(isAdd){
            picturesNewFolder.add(images);
        }else{
            picturesNewFolder.remove(images);
        }
        if(picturesNewFolder.size()>0){
            select.setAlpha((float)1);
        }
        if(picturesNewFolder.size()==0){
            select.setAlpha((float) 0.5);
        }
    }

    @Override
    public void onpicClicked(int position, ArrayList<Albums> albums) {

    }

    @Override
    public void onPicLongClicked(String imageAlbumPath, String albumName) {

    }
}
