package com.example.musicplayerv2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.example.musicplayerv2.Model.AudioFile;
import com.example.musicplayerv2.Model.Playlist;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    public static final int REQUEST_CODE = 1;
    static ArrayList<AudioFile> audioFiles;
    static ArrayList<Playlist> playlists;
    static ArrayList<String> titles;

    private MovementStatusReceiver movementStatusChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playlists = new ArrayList<>();
        titles = new ArrayList<>();
        permission();

        movementStatusChangeReceiver = new MovementStatusReceiver();
        IntentFilter intentFilter = new IntentFilter("com.movement.broadcast.soundBroadcast");
        if(intentFilter != null){
            registerReceiver(movementStatusChangeReceiver, intentFilter);
        }

    }

    private void permission() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }else{
            audioFiles = getAllAudio(this);
            Playlist playlist = new Playlist(audioFiles, "All Songs");
            playlists.add(playlist);
            titles.add("All Songs");
            initViewPager();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //permission granted
                audioFiles = getAllAudio(this);
                Playlist playlist = new Playlist(audioFiles, "All Songs");
                playlists.add(playlist);
                titles.add("All Songs");
                initViewPager();
            }else{
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }
    }

    private void initViewPager() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new SongsFragment(), "Songs");
        viewPagerAdapter.addFragments(new PlaylistsFragment(), "Playlists");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;
        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        void addFragments(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    public static ArrayList<AudioFile> getAllAudio(Context context){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        ArrayList<AudioFile> tempAudioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST
        };
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = context.getContentResolver().query(uri, projection, MediaStore.Audio.Media.IS_MUSIC + " != 0", null, sortOrder);
        if(cursor != null){
            while(cursor.moveToNext()){
                if(cursor.getString(3).contains("mp3")) {
                    String album = cursor.getString(0);
                    String title = cursor.getString(1);
                    String duration = cursor.getString(2);
                    String path = cursor.getString(3);
                    String artist = cursor.getString(4);
                    retriever.setDataSource(path);
                    byte[] art = retriever.getEmbeddedPicture();
                    AudioFile audioFile = new AudioFile(path, title, artist, album, duration, art);
                    tempAudioList.add(audioFile);
                }
            }
            cursor.close();
            retriever.release();
        }
        return tempAudioList;
    }
}