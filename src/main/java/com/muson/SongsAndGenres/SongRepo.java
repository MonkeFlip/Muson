package com.muson.SongsAndGenres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SongRepo extends JpaRepository<Song, Integer> {
    List<Song> getAllByOrderByIdAsc();
    Song findById(int id);
    List<Song> findAllByGenre(String genre);
    List<Song> findAllByArtist(String artist);
}
