package com.muson.service;



import com.muson.SongsAndGenres.Song;
import com.muson.SongsAndGenres.SongRepo;
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

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final SongRepo songRepo;
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
        log.info("Adding favourite song {} to user {}", songId, username);
        MusUser musUser = userRepo.findByUsername(username);
        Song song = songRepo.findById(songId);
        if (!musUser.getFavouriteSongs().contains(song))
        {
            musUser.getFavouriteSongs().add(song);
        }
        return new ArrayList<Song>(musUser.getFavouriteSongs());
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
    public ArrayList<Song> getRandomFavSongs(int number) {
        ArrayList<Song> songs = new ArrayList<Song>();
        for(int id: userRepo.getRandomFavSongs(number))
        {
            songs.add(songRepo.findById(id));
        }
        return songs;
    }

    @Override
    public List<MusUser> getUsers() {
        log.info("Fetching all users");
        return userRepo.findAll();
    }
}
