package com.example.quang.gallerytrain.utils;

import java.util.ArrayList;
import com.example.quang.gallerytrain.models.Images;
public interface itemClickListener {
    void onPicClicked(PicHolder holder, int position, ArrayList<Images> pics);
    void onPicClicked(String imageAlbumPath,String albumName);
}
