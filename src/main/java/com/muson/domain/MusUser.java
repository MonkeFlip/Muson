package com.muson.domain;

import com.muson.SongsAndGenres.Artist;
import com.muson.SongsAndGenres.Song;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
public class MusUser {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String username;
    private String password;
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Role> roles = new ArrayList<>();
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Song> favouriteSongs = new ArrayList<>();
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Song> dislikedSongs = new ArrayList<>();
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Artist> likedArtists = new ArrayList<>();
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Artist> dislikedArtists = new ArrayList<>();
}
