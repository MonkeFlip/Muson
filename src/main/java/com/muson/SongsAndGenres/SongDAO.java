package com.muson.SongsAndGenres;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SongDAO extends CrudRepository<Song, Integer> {
    List<Song> findAllByOrderByIdAsc();
    Song findById(int id);
    List<Song> findAllByGenre(String genre);
    List<Song> findAllByArtist(String artist);
}
