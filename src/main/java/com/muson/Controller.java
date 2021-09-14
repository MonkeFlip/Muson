package com.muson;

import com.muson.Authorization.User;
import com.muson.Authorization.UserDAO;
import com.muson.SongsAndGenres.Genre;
import com.muson.SongsAndGenres.GenreDAO;
import com.muson.SongsAndGenres.Song;
import com.muson.SongsAndGenres.SongDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private SongDAO songDAO;//performs all DB operations related to table "songs"

    @Autowired
    private GenreDAO genreDAO;//performs all DB operations related to table "genres"

    //Examples
    @GetMapping("/playSong")
    public Song returnSongInfo(@RequestParam(value = "songName", defaultValue = "default song") String songName, @RequestParam(value = "artistName", defaultValue = "default song") String artistName)
    {
        return songDAO.findById((artistName + songName).hashCode());
    }

    @GetMapping("/showAllGenres")
    public List<Genre> showGenres()
    {
        return genreDAO.getAllBy();
    }

    @GetMapping ("/registration")
    public ResponseEntity<User> registration(@RequestParam(value ="login", defaultValue ="0") String login,  @RequestParam (value="password", defaultValue ="0") String password, @RequestParam(value="email", defaultValue ="0") String email){
        User user=new User(login, password, email);
//        userDAO.save(user); //there is no DB now...
        return new ResponseEntity<User>(user,HttpStatus.OK);
    }

    @GetMapping("/showAllSongs")
    public List<Song> show()
    {
        return songDAO.getAllByOrderByIdAsc();
    }



}
