package com.example.quang.gallerytrain.activitys;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.quang.gallerytrain.R;
import com.example.quang.gallerytrain.fragment.FragmentAlbums;
import com.example.quang.gallerytrain.fragment.FragmentPictures;
import com.example.quang.gallerytrain.fragment.FragmentShared;
import com.example.quang.gallerytrain.fragment.FragmentStories;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public static Context contextOfApplication;
    BottomNavigationView bottomNavigationView;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
        contextOfApplication = getApplicationContext();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.pictures);
       // changeStatusBarColor();

    }
    FragmentPictures fragment_pictures= new FragmentPictures();
    FragmentAlbums fragment_albums = new FragmentAlbums();
    FragmentStories fragment_stories = new FragmentStories();
    FragmentShared fragment_shared=new FragmentShared();
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.pictures:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, fragment_pictures).commit();
                return true;

            case R.id.albums:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, fragment_albums).commit();
                return true;

            case R.id.shared:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, fragment_shared).commit();
                return true;
            case R.id.stories:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, fragment_stories).commit();
                return true;
        }
        return false;
    }
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void changeStatusBarColor()
//    {
//        Window window = this.getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
//
//    }
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
}
