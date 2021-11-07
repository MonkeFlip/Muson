package com.muson;
import com.muson.SongsAndGenres.Genre;
import com.muson.SongsAndGenres.GenreDAO;
import com.muson.SongsAndGenres.Song;
import com.muson.SongsAndGenres.SongRepo;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin()
public class Controller {
    @Autowired
    private GenreDAO genreDAO;//performs all DB operations related to table "genres"
    private final SongRepo songRepo;//performs all DB operations related to table "songs"

    @GetMapping("/playSong")
    public Song returnSongInfo(@RequestParam(value = "songName", defaultValue = "default song") String songName, @RequestParam(value = "artistName", defaultValue = "default song") String artistName)
    {
        return songRepo.findById((artistName + songName).hashCode());
    }

    @GetMapping("/showAllGenres")
    public List<Genre> showGenres()
    {
        return genreDAO.getAllBy();
    }

    @GetMapping("/showAllSongs")
    @CrossOrigin()
    public List<Song> show()
    {
        System.out.println("In /showAllSongs.");
        return songRepo.findAll();
    }
    @GetMapping("/findByArtist")
    public List<Song> showByArtist(@RequestParam(value = "artist", defaultValue = "default song") String artist)
    {
        return songRepo.findAllByArtist(artist);
    }

    @GetMapping("/findByGenre")
    public List<Song> showByGenre(@RequestParam(value = "genre", defaultValue = "default song") String genre)
    {
        return songRepo.findAllByGenre(genre);
    }

    @GetMapping(value="/stream/{songName}", produces = {
            MediaType.APPLICATION_OCTET_STREAM_VALUE })
    @CrossOrigin()
    public ResponseEntity playAudio(HttpServletRequest request, HttpServletResponse response, @PathVariable("songName") String songName) throws FileNotFoundException {

//        System.out.println("[downloadRecipientFile]");
        var songVar=songRepo.findById(Integer.valueOf(songName));
        Song song = songVar.get();
        String file = song.getDirectory()+'/'+song.getSong();
//        file = file.replaceAll("%20", " ");
        long length = new File(file).length();
        System.out.println("stream ok"+ file);
        InputStreamResource inputStreamResource = new InputStreamResource( new FileInputStream(file));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentLength(length);
        httpHeaders.setCacheControl(CacheControl.noCache().getHeaderValue());
        return new ResponseEntity(inputStreamResource, httpHeaders, HttpStatus.OK);
    }
}
