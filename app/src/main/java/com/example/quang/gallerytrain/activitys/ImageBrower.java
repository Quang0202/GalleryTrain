package com.example.quang.gallerytrain.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.quang.gallerytrain.R;
import com.example.quang.gallerytrain.models.Albums;
import com.example.quang.gallerytrain.models.Images;
import com.example.quang.gallerytrain.utils.GetImages;
import com.example.quang.gallerytrain.utils.OnSwipeTouchListener;
import com.example.quang.gallerytrain.adapters.PicHolder;
import com.example.quang.gallerytrain.adapters.SelectAlbumAdapter;
import com.example.quang.gallerytrain.utils.imageIndicatorListener;
import com.example.quang.gallerytrain.utils.itemClickListener;
import com.example.quang.gallerytrain.utils.recyclerViewPagerImageIndicator;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static androidx.core.view.ViewCompat.setTransitionName;

public class ImageBrower extends AppCompatActivity implements imageIndicatorListener,BottomNavigationView.OnNavigationItemSelectedListener, itemClickListener {
    private ArrayList<Images> allImages = new ArrayList<>();
    private int position;
    private Context animeContx;
    private FrameLayout frameLayout;
    private TextView header;
    private TextView headerTime;
    private ImageView back;
    private ImageView image;
    private CardView hearderCard;
    private ViewPager imagePager;
    private RecyclerView indicatorRecycler;
    private BottomNavigationView bottomImageMenu;
    private int viewVisibilityController;
    private  RecyclerView.Adapter indicatorAdapter;
    private int viewVisibilitylooper;
    private ImageBrower.ImagesPagerAdapter pagingImages;
    public static Dialog dialog;
    private Button dialogBtn;
    private BitmapFactory.Options options;
    private GetImages getImages;
    private int previousSelected = -1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_brower);
        if (ContextCompat.checkSelfPermission(ImageBrower.this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (ContextCompat.checkSelfPermission(ImageBrower.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        viewVisibilityController = 0;
        viewVisibilitylooper = 0;
        animeContx=this;
        Intent intent= getIntent();
        Bundle args= intent.getBundleExtra("BUNDLE");
        allImages=(ArrayList<Images>)args.getSerializable("IMAGES");
        position=intent.getIntExtra("POSITION",0);
        imagePager = findViewById(R.id.imagePager);
        pagingImages = new ImageBrower.ImagesPagerAdapter();
        imagePager.setAdapter(pagingImages);
        imagePager.setOffscreenPageLimit(3);
        imagePager.setCurrentItem(position);

        getImages= new GetImages();
        frameLayout=findViewById(R.id.imageLayout);
        header=findViewById(R.id.header_title);
        hearderCard=findViewById(R.id.head_card);
        headerTime=findViewById(R.id.header_time);
        back=findViewById(R.id.back);

        indicatorRecycler = findViewById(R.id.indicatorRecycler);
        indicatorRecycler.hasFixedSize();
        indicatorRecycler.setLayoutManager(new GridLayoutManager(this,1,RecyclerView.HORIZONTAL,false));
        indicatorAdapter = new recyclerViewPagerImageIndicator(allImages,this,this);
        indicatorRecycler.setAdapter(indicatorAdapter);

        bottomImageMenu=findViewById(R.id.menu_image_view);
        bottomImageMenu.setSelectedItemId(R.id.invisible);
        bottomImageMenu.setOnNavigationItemSelectedListener(this);

        allImages.get(position).setSelected(true);
        previousSelected = position;
        indicatorAdapter.notifyDataSetChanged();
        indicatorRecycler.scrollToPosition(position);
        imagePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(previousSelected != -1){
                    allImages.get(previousSelected).setSelected(false);
                    previousSelected = position;
                    allImages.get(position).setSelected(true);
                    indicatorRecycler.getAdapter().notifyDataSetChanged();
                    indicatorRecycler.scrollToPosition(position);
                }else{
                    previousSelected = position;
                    allImages.get(position).setSelected(true);
                    indicatorRecycler.getAdapter().notifyDataSetChanged();
                    indicatorRecycler.scrollToPosition(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onImageIndicatorClicked(int ImagePosition) {
        if(previousSelected != -1){
            allImages.get(previousSelected).setSelected(false);
            previousSelected = ImagePosition;
            indicatorRecycler.getAdapter().notifyDataSetChanged();
        }else{
            previousSelected = ImagePosition;
        }

        imagePager.setCurrentItem(ImagePosition);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                deleteImage(imagePager.getCurrentItem());
                return false;
            case R.id.detail:
                Intent intent= new Intent(animeContx,DetailsImage.class);
                intent.putExtra("PATH",allImages.get(position).getImagePath());
                intent.putExtra("NAME",allImages.get(position).getImageName());
                intent.putExtra("TIMESTAMP",allImages.get(position).getTimeTamp());
                intent.putExtra("SIZE",allImages.get(position).getImageSize());
                startActivity(intent);
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                return false;

            case R.id.move:
                showDialog(ImageBrower.this);
                return false;
            case R.id.share:
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(allImages.get(imagePager.getCurrentItem()).getImagePath(),options);
                options.inSampleSize = 4;
                options.inJustDecodeBounds=false;
                Bitmap bitmap = BitmapFactory.decodeFile(allImages.get(imagePager.getCurrentItem()).getImagePath());
                shareImageandText(bitmap);
                return false;
        }
        return false;
    }
    private void shareImageandText(Bitmap bitmap) {
        Uri uri = getmageToShare(bitmap);
        Intent intent = new Intent(Intent.ACTION_SEND);

        // putting uri of image to be shared
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        // adding text to share
        intent.putExtra(Intent.EXTRA_TEXT, "Sharing Image");

        // Add subject Here
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");

        // setting type to image
        intent.setType("image/png");

        // calling startactivity() to share
        startActivity(Intent.createChooser(intent, "Share Via"));
    }

    // Retrieving the url to share
    private Uri getmageToShare(Bitmap bitmap) {
        File imagefolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imagefolder.mkdirs();
            File file = new File(imagefolder, "shared_image.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(this, "com.anni.shareimage.fileprovider", file);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return uri;
    }
    public void showDialog(Activity activity){
        dialog = new Dialog(activity);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.select_album_dialog);
        dialogBtn = dialog.findViewById(R.id.btndialog);
        dialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        final RecyclerView recyclerView = dialog.findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<Albums> albums = getImages.getPicturePaths();
        recyclerView.setAdapter(new SelectAlbumAdapter(this,albums,this, allImages.get(imagePager.getCurrentItem())));
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);
        lp.width = 1000; //  width
        dialogWindow.setAttributes(lp);
        dialog.show();

    }
    public void deleteImage(final int imPosition){
        DialogInterface.OnClickListener dialogClickListener= new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case DialogInterface.BUTTON_POSITIVE:
                        File imageFile= new File(allImages.get(imPosition).getImagePath());
                        if (imageFile.exists()) {
                            if (imageFile.delete()) {
                               Log.d("delete ok",allImages.get(imPosition).getImagePath());
                            } else {
                                Log.d("delete error",allImages.get(imPosition).getImagePath());
                            }
                        }
                        previousSelected=imPosition;
                        if(allImages.size()==1){
                           finish();
                           return;
                        }
                        if(imPosition<allImages.size()-1) {
                            allImages.get(imPosition).setSelected(false);
                            allImages.get(imPosition+1).setSelected(true);
                            indicatorRecycler.scrollToPosition(position+1);
                            indicatorAdapter.notifyDataSetChanged();
                        }
                        else{
                            allImages.get(imPosition).setSelected(false);
                            allImages.get(imPosition-1).setSelected(true);
                            indicatorRecycler.scrollToPosition(imPosition-1);
                            indicatorAdapter.notifyDataSetChanged();
                            imagePager.setCurrentItem(imPosition-1);
                        }
                        pagingImages.removeAt(imPosition);
                        //allImages.remove(position);
                        indicatorAdapter.notifyItemRemoved(imPosition);
                        indicatorAdapter.notifyDataSetChanged();
//                        allImages.remove(position);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage("Delete this image?").setPositiveButton("Yes",dialogClickListener)
                .setNegativeButton("No",dialogClickListener).show();
    }

    @Override
    public void onPicClicked(PicHolder holder, int position, ArrayList<Images> pics) {

    }

    @Override
    public void onPicClicked(String imageAlbumPath, String albumName) {

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


    private class ImagesPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return allImages.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup containerCollection, final int position) {
            LayoutInflater layoutinflater = (LayoutInflater) containerCollection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutinflater.inflate(R.layout.picture_browser_pager,null);
            image = view.findViewById(R.id.image);

            setTransitionName(image, String.valueOf(position)+"picture");

            Images pic = allImages.get(position);
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(pic.getTimeTamp());
            String date = DateFormat.format("dd-MM-yyyy", cal).toString();
            String time = DateFormat.format("kk:mm", cal).toString();
            header.setText(date);
            headerTime.setText(time);
            Glide.with(animeContx)
                    .load(pic.getImagePath())
                    .apply(new RequestOptions().fitCenter())
                    .into(image);
            image.setOnTouchListener(new OnSwipeTouchListener(animeContx){
                public void onSwipeTop() {
                    Intent intentTop= new Intent(animeContx,DetailsImage.class);
                    intentTop.putExtra("PATH",allImages.get(position).getImagePath());
                    intentTop.putExtra("NAME",allImages.get(position).getImageName());
                    intentTop.putExtra("TIMESTAMP",allImages.get(position).getTimeTamp());
                    intentTop.putExtra("SIZE",allImages.get(position).getImageSize());
                    startActivity(intentTop);
                    overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                }
                public void onClick(){
                    if(indicatorRecycler.getVisibility() == View.GONE){
                        indicatorRecycler.setVisibility(View.VISIBLE);
                        hearderCard.setVisibility(View.VISIBLE);
                        frameLayout.setBackgroundColor(getResources().getColor(R.color.white));
                    }else{
                        indicatorRecycler.setVisibility(View.GONE);
                        frameLayout.setBackgroundColor(getResources().getColor(R.color.black));
                        hearderCard.setVisibility(View.GONE);
                    }
                    if(bottomImageMenu.getVisibility()==View.GONE){
                        bottomImageMenu.setVisibility(View.VISIBLE);
                    }else{
                        bottomImageMenu.setVisibility(View.GONE);
                    }
                }

                public void onSwipeBottom() {

                }
            });

            ((ViewPager) containerCollection).addView(view);
            return view;
        }
        public void removeAt(int position){
            allImages.remove(position);
            notifyDataSetChanged();
            return;
        }

        @Override
        public void destroyItem(ViewGroup containerCollection, int position, Object view) {
            ((ViewPager) containerCollection).removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == ((View) object);
        }
        @Override
        public int getItemPosition(Object object){
            return PagerAdapter.POSITION_NONE;
        }
    }

}
