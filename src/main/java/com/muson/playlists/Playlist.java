package com.muson.playlists;

import com.muson.SongsAndGenres.Song;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    int current_song;
    int max_size;
    ArrayList<Song> songs;

    public Playlist(int max_size) {
        this.current_song = 0;
        songs = new ArrayList<Song>();
        this.max_size = max_size;
    }

    public Playlist() {
        this.current_song = 0;
        songs = new ArrayList<Song>();
        this.max_size = 60;
    }

    public void AddSong(Song song)
    {
        if (songs.size() < max_size) {
            songs.add(song);
        }
        //TODO: add exception throwing and handling
    }


    //Getters & Setters

    public int getMax_size() {
        return max_size;
    }

    public void setMax_size(int max_size) {
        this.max_size = max_size;
    }

    public int getCurrent_song() {
        return current_song;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setCurrent_song(int current_song) {
        this.current_song = current_song;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }
}
