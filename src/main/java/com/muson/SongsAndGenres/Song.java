package com.muson.SongsAndGenres;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

@Entity
@Table(name = "songs")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;//id = hashcode(artist + song)
    String song;
    String artist;
    String genre;
    String directory;

    //constructors
    public Song(){}

    public Song(String songName, String artistName) {
        this.song = songName;
        this.artist = artistName;
        this.id = new String(artistName + songName).hashCode();
    }

    public Song(String songName, String artistName, String genre, String directory, int id) {
        this.song = songName;
        this.artist = artistName;
        this.genre = genre;
        this.directory = directory;
        this.id = id;
    }

    //Getters & Setters
    public void setSong(String songName) {
        this.song = songName;
    }

    public void setArtist(String artistName) {
        this.artist = artistName;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSong() {
        return song;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public String getDirectory() {
        return directory;
    }

    public int getId() {
        return id;
    }
}
