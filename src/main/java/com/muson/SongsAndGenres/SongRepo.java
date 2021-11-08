package com.muson.SongsAndGenres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface SongRepo extends JpaRepository<Song, Integer> {
    Song findById(int id);
    @Query(value = "select * from songs TABLESAMPLE SYSTEM_ROWS(:number)", nativeQuery = true)//execute "CREATE EXTENSION tsm_system_rows;" in  postgres query console to use this method
    ArrayList<Song> getRandomSongs(@Param("number") int number);
    @Query(value = "SELECT reltuples AS estimate FROM pg_class WHERE relname = 'songs'", nativeQuery = true)
    int getApproximateNumberOfRows();//fast
    @Query(value = "SELECT count(*) FROM songs", nativeQuery = true)
    int getExactNumberOfRows();//slow

    @Query(value = "SELECT * FROM songs where :genreName = any (genre)", nativeQuery = true)
    List<Song> findAllByGenre(@Param("genreName") String genreName);
    List<Song> findAllByArtist(String artist);
}
