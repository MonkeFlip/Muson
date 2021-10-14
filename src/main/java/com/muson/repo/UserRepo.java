package com.muson.repo;

import com.muson.SongsAndGenres.Song;
import com.muson.domain.MusUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface UserRepo extends JpaRepository<MusUser, Long> {
    MusUser findByUsername(String username);
    @Query(value = "select favourite_songs_id from mus_user_favourite_songs order by random() limit 1", nativeQuery = true)
    int getRandomFavSong();
    @Query(value = "select * from mus_user_favourite_songs TABLESAMPLE SYSTEM_ROWS(:number)", nativeQuery = true)
    ArrayList<Song> getRandomFavSongs(@Param("number") int number);
    @Query(value = "SELECT reltuples AS estimate FROM pg_class WHERE relname = 'mus_user_favourite_songs'", nativeQuery = true)
    int getApproximateNumberOfRows();//fast
    @Query(value = "SELECT count(*) FROM mus_user_favourite_songs", nativeQuery = true)
    int getExactNumberOfRows();//slow
}
