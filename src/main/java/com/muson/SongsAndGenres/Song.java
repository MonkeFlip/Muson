package com.muson.SongsAndGenres;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
@Table(name = "songs")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;//id = hashcode(artist + song)

    String artist;
    String directory;
    String genre;
    String song;
}
