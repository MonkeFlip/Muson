package com.muson.SongsAndGenres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SongRepo extends JpaRepository<Song, Integer> {
    List<Song> findAll();
    Song findById(int id);
    List<Song> findAllByGenre(String genre);
    List<Song> findAllByArtist(String artist);
}
