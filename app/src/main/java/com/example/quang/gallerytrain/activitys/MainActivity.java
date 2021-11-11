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
import android.view.Window;
import android.view.WindowManager;
import com.example.quang.gallerytrain.R;
import com.example.quang.gallerytrain.fragment.Fragment_Albums;
import com.example.quang.gallerytrain.fragment.Fragment_Pictures;
import com.example.quang.gallerytrain.fragment.Fragment_Shared;
import com.example.quang.gallerytrain.fragment.Fragment_Stories;
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
        changeStatusBarColor();

    }
    Fragment_Pictures fragment_pictures= new Fragment_Pictures();
    Fragment_Albums fragment_albums = new Fragment_Albums();
    Fragment_Stories fragment_stories = new Fragment_Stories();
    Fragment_Shared fragment_shared=new Fragment_Shared();
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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void changeStatusBarColor()
    {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.black));

    }
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
}
