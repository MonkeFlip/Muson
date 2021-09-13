package com.muson.SongsAndGenres;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GenreDAO extends CrudRepository<Genre, Integer> {
    List<Genre> getAllBy();
}
