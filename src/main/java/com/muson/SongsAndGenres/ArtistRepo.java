package com.muson.SongsAndGenres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface ArtistRepo extends JpaRepository<Artist, Integer> {
    List<Artist> getAllBy();
    Artist findById(int id);
    Artist findByArtist(String artist);
}
