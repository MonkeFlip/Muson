package com.muson;

import com.muson.SongsAndGenres.Song;
import com.muson.SongsAndGenres.SongDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller {
    @Autowired
    SongDAO songDAO;//performs all DB operations related to table "songs"
}
