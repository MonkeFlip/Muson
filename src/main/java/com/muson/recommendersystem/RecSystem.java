package com.muson.recommendersystem;

import com.muson.SongsAndGenres.SongRepo;
import com.muson.playlists.Playlist;
import com.muson.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;


public class RecSystem {
    public void RecommendRandomSongs(Playlist playlist, UserService userService)
    {
        //TODO: songs in playlist must be unique
        Random random = new Random();
        for(int i = playlist.getSongs().size(); i < playlist.getMax_size(); i++)
        {
            playlist.AddSong(userService.getRandomById());
        }
    }

    public void RecommendRandomFavouriteSongs(Playlist playlist, UserService userService)
    {
        //TODO: songs in playlist must be unique
        Random random = new Random();
        for(int i = playlist.getSongs().size(); i < playlist.getMax_size(); i++)
        {
            playlist.AddSong(userService.getRandomFavById());
        }
    }
}
