package com.example.musicplayerv2.Model;

import java.util.ArrayList;

public class Playlist {
    private ArrayList<AudioFile> songs;
    private String title;

    public Playlist() {
    }

    public Playlist(ArrayList<AudioFile> songs, String title) {
        this.songs = songs;
        this.title = title;
    }

    public ArrayList<AudioFile> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<AudioFile> songs) {
        this.songs = songs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
