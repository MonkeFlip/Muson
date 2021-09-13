package com.muson.SongsAndGenres;

import javax.persistence.*;

@Entity
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;//id = hashcode(genre)
    String genre;

    //Constructors
    public Genre() {
    }

    //Getters & Setters


    public int getId() {
        return id;
    }

    public String getGenre() {
        return genre;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
