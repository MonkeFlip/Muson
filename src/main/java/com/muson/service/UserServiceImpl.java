package com.muson.service;



import com.muson.SongsAndGenres.*;
import com.muson.domain.MusUser;
import com.muson.domain.Role;
import com.muson.repo.RoleRepo;
import com.muson.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final SongRepo songRepo;
    private final GenreDAO genreDAO;
    private final ArtistRepo artistRepo;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MusUser user = userRepo.findByUsername(username);
        if (user == null){
            log.error("User not found in the database.");
            throw new UsernameNotFoundException("User not found in the database.");
        }else{
            log.info("User found in the database: {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role ->
        {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public MusUser saveUser(MusUser user) {
        log.info("Saving new user {} to the database.", user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database.", role.getName());
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", roleName, username);
        MusUser musUser = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
        musUser.getRoles().add(role);
    }

    @Override
    public ArrayList<Song> addFavSongToUser(String username, int songId) {
        log.info("Like song {} user {}", songId, username);
        MusUser musUser = userRepo.findByUsername(username);
        Song song = songRepo.findById(songId);
        if (!musUser.getFavouriteSongs().contains(song))
        {
            musUser.getFavouriteSongs().add(song);
            musUser.getDislikedSongs().remove(song);
        }
        return new ArrayList<Song>(musUser.getFavouriteSongs());
    }

    @Override
    public ArrayList<Song> addDislikedSongToUser(String username, int songId) {
        log.info("Dislike song {} user {}", songId, username);
        MusUser musUser = userRepo.findByUsername(username);
        Song song = songRepo.findById(songId);
        if (!musUser.getDislikedSongs().contains(song))
        {
            musUser.getDislikedSongs().add(song);
            musUser.getFavouriteSongs().remove(song);
        }
        return new ArrayList<Song>(musUser.getDislikedSongs());
    }


    @Override
    public MusUser getUser(String username) {
        log.info("Fetching user {}", username);
        return userRepo.findByUsername(username);
    }

    @Override
    public ArrayList<Song> getRandomSongs(int number) {
        return songRepo.getRandomSongs(number);
    }

    @Override
    public ArrayList<Song> getRandomFavSongs(String username, int number) {
        ArrayList<Song> songs = new ArrayList<Song>();
        MusUser musUser = userRepo.findByUsername(username);
        Random random = new Random();
        int index;
        if (musUser.getFavouriteSongs().size() > 0)
        {
            for (int i = 0; i < number; i++)
            {
                index = random.nextInt(musUser.getFavouriteSongs().size());
                if (!songs.contains((Song)musUser.getFavouriteSongs().toArray()[index]))
                {
                    songs.add((Song)musUser.getFavouriteSongs().toArray()[index]);
                }
            }
        }
        return songs;
    }

    @Override
    public List<MusUser> getUsers() {
        log.info("Fetching all users");
        return userRepo.findAll();
    }

    @Override
    public List<Song> getAllSongsByGenre(String genre) {
        return songRepo.findAllByGenre(genre);
    }

    @Override
    public List<Song> getAllSongsByArtist(String artist) {
        return songRepo.findAllByArtist(artist);
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreDAO.getAllBy();
    }

    @Override
    public List<Artist> getAllArtists() {
        return artistRepo.getAllBy();
    }

    @Override
    public ArrayList<Song> getLikedSongs(String username) {
        MusUser user = userRepo.getByUsername(username);
        ArrayList<Song> songs = new ArrayList<>();
        for (Song song:user.getFavouriteSongs()
             ) {
            songs.add(song);
        }
        return songs;
    }

    @Override
    public ArrayList<Artist> getLikedArtists(String username) {
        MusUser user = userRepo.getByUsername(username);
        ArrayList<Artist> artists = new ArrayList<>();
        for (Artist artist:user.getLikedArtists()
        ) {
            artists.add(artist);
        }
        return artists;
    }

    @Override
    public ArrayList<Artist> LikeArtist(String username, int id) {
        MusUser musUser = userRepo.findByUsername(username);
        Artist artist = artistRepo.findById(id);
        if (!musUser.getLikedArtists().contains(artist))
        {
            musUser.getLikedArtists().add(artist);
            musUser.getDislikedArtists().remove(artist);
        }
        return new ArrayList<Artist>(musUser.getLikedArtists());
    }

    @Override
    public ArrayList<Artist> DislikeArtist(String username, int id) {
        MusUser musUser = userRepo.findByUsername(username);
        Artist artist = artistRepo.findById(id);
        if (!musUser.getDislikedArtists().contains(artist))
        {
            musUser.getDislikedArtists().add(artist);
            musUser.getLikedArtists().remove(artist);
        }
        return new ArrayList<Artist>(musUser.getDislikedArtists());
    }

    @Override
    public List<Song> SearchSongs(String value) {
        ArrayList<Song> result = new ArrayList<Song>();
        for (int id :userRepo.SearchSongs(value + "%"))
        {
            result.add(songRepo.findById(id));
        }

        return result;
    }

    @Override
    public List<Song> SearchArtists(String value) {
        List<Song> songs = new ArrayList<Song>();
        ArrayList<Artist> result = new ArrayList<Artist>();
        for (int id :userRepo.SearchArtists(value + "%"))
        {
            result.add(artistRepo.findById(id));
        }
        List<Song> artistSongs = new ArrayList<Song>();
        for(Artist i: result){
            System.out.println(i.getArtist());
            artistSongs=getAllSongsByArtist(i.getArtist());
            songs.addAll(artistSongs);
        }
        for(Song i: songs){
            System.out.println(i);
        }
        return songs;
    }
}
