package com.muson.repo;

import com.muson.SongsAndGenres.Song;
import com.muson.domain.MusUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface UserRepo extends JpaRepository<MusUser, Long> {
    MusUser findByUsername(String username);
    MusUser getByUsername(String username);
    @Query(value = "select favourite_songs_id from mus_user_favourite_songs TABLESAMPLE SYSTEM_ROWS(:number)", nativeQuery = true)
    ArrayList<Integer> getRandomFavSongs(@Param("number") int number);//returns IDs of songs that user liked
    @Query(value = "SELECT reltuples AS estimate FROM pg_class WHERE relname = 'mus_user_favourite_songs'", nativeQuery = true)
    int getApproximateNumberOfRows();//fast
    @Query(value = "SELECT count(*) FROM mus_user_favourite_songs", nativeQuery = true)
    int getExactNumberOfRows();//slow
}
