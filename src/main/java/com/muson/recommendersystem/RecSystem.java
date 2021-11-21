package com.muson.recommendersystem;

import com.muson.SongsAndGenres.Genre;
import com.muson.SongsAndGenres.Song;
import com.muson.SongsAndGenres.SongRepo;
import com.muson.playlists.Playlist;
import com.muson.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class RecSystem {
    public void RecommendRandomSongs(Playlist playlist, UserService userService)
    {
        ArrayList<Song> songs = userService.getRandomSongs(playlist.getMax_size());
        playlist.getSongs().addAll(songs);
    }

    public void RecommendRandomFavouriteSongs(Playlist playlist, UserService userService, String username)
    {
        ArrayList<Song> songs = userService.getRandomFavSongs(username, playlist.getMax_size());
        playlist.getSongs().addAll(songs);
    }

    public void FillDailyPlaylist(Playlist playlist, UserService userService, String username)
    {
        int size = playlist.getMax_size();
        int part = size/3;

        ArrayList<Song> fav_songs = userService.getRandomFavSongs(username, part);
        ArrayList<Song> rand_songs = userService.getRandomSongs(part * 2 - fav_songs.size());

        List<Genre> all_genres = userService.getAllGenres();
        Genre rand_genre = all_genres.get(ThreadLocalRandom.current().nextInt(0, all_genres.size()));
        List<Song> genre_songs = userService.getAllSongsByGenre(rand_genre.getGenre());
        ArrayList<Song> rand_genre_songs = new ArrayList<Song>();

        for (int  i=0; i<part; i++) {
            int randInt = ThreadLocalRandom.current().nextInt(0, genre_songs.size());
            rand_genre_songs.add(genre_songs.get(randInt));
        }

        for (int i=0; i<fav_songs.size(); i++) {
            playlist.AddSong(fav_songs.get(i));
        }

        for (int i=0; i<rand_songs.size(); i++) {
            playlist.AddSong(rand_songs.get(i));
        }

        for (int i=0; i<rand_genre_songs.size(); i++) {
            playlist.AddSong(rand_genre_songs.get(i));
        }
    }
}
