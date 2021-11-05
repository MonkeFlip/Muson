package com.muson.service;



import com.muson.SongsAndGenres.Artist;
import com.muson.SongsAndGenres.Genre;
import com.muson.SongsAndGenres.Song;
import com.muson.domain.MusUser;
import com.muson.domain.Role;

import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

public interface UserService {
    MusUser saveUser(MusUser user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    ArrayList<Song> addFavSongToUser(String username, int songId);
    ArrayList<Song> addDislikedSongToUser(String username, int songId);
    MusUser getUser(String username);
    ArrayList<Song> getRandomSongs(int number);
    ArrayList<Song> getRandomFavSongs(int number);
    List<MusUser> getUsers();
    List<Song> getAllSongsByGenre(String genre);
    List<Song> getAllSongsByArtist(String artist);
    List<Genre> getAllGenres();
    List<Artist> getAllArtists();
}
