package com.muson.repo;

import com.muson.SongsAndGenres.Song;
import com.muson.domain.MusUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface UserRepo extends JpaRepository<MusUser, Long> {
    MusUser findByUsername(String username);
    MusUser getByUsername(String username);
    @Query(value = "select favourite_songs_id from mus_user_favourite_songs TABLESAMPLE SYSTEM_ROWS(:number) where mus_user_id = :user_id", nativeQuery = true)
    ArrayList<Integer> getRandomFavSongs(@Param("user_id") long user_id, @Param("number") int number);//returns IDs of songs that user liked
    @Query(value = "SELECT reltuples AS estimate FROM pg_class WHERE relname = 'mus_user_favourite_songs'", nativeQuery = true)
    int getApproximateNumberOfRows();//fast
    @Query(value = "SELECT count(*) FROM mus_user_favourite_songs", nativeQuery = true)
    int getExactNumberOfRows();//slow

    @Query(value = "SELECT id from songs where song like :songName", nativeQuery = true)
    List<Integer> SearchSongs(@Param("songName") String songName);

    @Query(value = "SELECT id from artists where artist like :artistName", nativeQuery = true)
    List<Integer> SearchArtists(@Param("artistName") String artistName);
}
