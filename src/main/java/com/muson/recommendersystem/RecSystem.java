package com.muson.recommendersystem;

import com.muson.SongsAndGenres.Song;
import com.muson.SongsAndGenres.SongRepo;
import com.muson.playlists.Playlist;
import com.muson.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Random;


public class RecSystem {
    public void RecommendRandomSongs(Playlist playlist, UserService userService)
    {
        ArrayList<Song> songs = userService.getRandomSongs(playlist.getMax_size());
        playlist.getSongs().addAll(songs);
    }

    public void RecommendRandomFavouriteSongs(Playlist playlist, UserService userService)
    {
        ArrayList<Song> songs = userService.getRandomFavSongs(playlist.getMax_size());
        playlist.getSongs().addAll(songs);
    }

    public void FillDailyPlaylist(Playlist playlist, UserService userService)
    {
        //TODO: implement this method
    }
}
