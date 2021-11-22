package com.example.quang.gallerytrain.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quang.gallerytrain.R;
import com.example.quang.gallerytrain.activitys.CreateAlbum;
import com.example.quang.gallerytrain.activitys.MainActivity;
import com.example.quang.gallerytrain.activitys.ShowImagesAlbum;
import com.example.quang.gallerytrain.models.Albums;
import com.example.quang.gallerytrain.models.Images;
import com.example.quang.gallerytrain.utils.GetImages;
import com.example.quang.gallerytrain.utils.MarginDecoration;
import com.example.quang.gallerytrain.adapters.PicHolder;
import com.example.quang.gallerytrain.utils.itemClickListener;
import com.example.quang.gallerytrain.adapters.PictureFolderAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

public class FragmentAlbums extends Fragment implements itemClickListener {

    private OnFragmentInteractionListener mListener;
    RecyclerView albumRecycler;
    TextView empty;
    Context applicationContext ;
    GetImages getImages;
    boolean allowRefresh;
    ArrayList<Albums> albums= new ArrayList<>();
    private FloatingActionButton addAlbum;
    public FragmentAlbums() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view=inflater.inflate(R.layout.fragment_albums, container, false);
        empty =view.findViewById(R.id.empty);
        getImages= new GetImages();
        allowRefresh=false;
        addAlbum= view.findViewById(R.id.add_album);
        applicationContext= MainActivity.getContextOfApplication();
        albumRecycler = view.findViewById(R.id.albumRecycler);
        albumRecycler.addItemDecoration(new MarginDecoration(applicationContext));
        albumRecycler.hasFixedSize();
        albums = getImages.getPicturePaths();
        if(albums.isEmpty()){
            empty.setVisibility(View.VISIBLE);
        }else{
            RecyclerView.Adapter folderAdapter = new PictureFolderAdapter(albums,applicationContext,this);
            albumRecycler.setAdapter(folderAdapter);
        }
        addAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Add album");
                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final File f = new File(Environment.getExternalStorageDirectory(), input.getText().toString());
                        if(!f.exists()) {
                            Intent intent = new Intent(getActivity(), CreateAlbum.class);
                            intent.putExtra("FOlDERNAME", input.getText().toString());
                            startActivity(intent);
                        }else{
                            Toast.makeText(applicationContext, "Album exits!" , Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
         return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(allowRefresh){
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
        allowRefresh=true;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPicClicked(PicHolder holder, int position, ArrayList<Images> pics) {

    }

    @Override
    public void onPicClicked(String imageAlbumPath, String albumName) {
        Intent move = new Intent(applicationContext, ShowImagesAlbum.class);
        move.putExtra("folderPath",imageAlbumPath);
        move.putExtra("folderName",albumName);
        startActivity(move);
    }

    @Override
    public void onpicClicked(Images images, boolean isAdd) {

    }

    @Override
    public void onpicClicked(int position, ArrayList<Albums> albums) {

    }

    @Override
    public void onPicLongClicked(final String albumPath, final String albumName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete album "+ albumName+"?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                File folderFile= new File(albumPath);
                if (folderFile.exists()) {
                    ArrayList<Images> images= getImages.getImagesWithPath(albumPath);

                    for(Images image:images){
                        File imageFile= new File(image.getImagePath());
                        if (imageFile.delete()) {
                            Log.d("delete ok",image.getImagePath());
                        } else {
                            Log.d("delete error",image.getImagePath());
                        }
                    }
                    if (folderFile.delete()) {
                        Log.d("delete ok",albumPath);
                    } else {
                        Log.d("delete error",albumPath);
                    }
                }
                onResume();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
