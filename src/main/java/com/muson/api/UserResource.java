package com.muson.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muson.SongsAndGenres.Artist;
import com.muson.SongsAndGenres.Genre;
import com.muson.SongsAndGenres.Song;
import com.muson.SongsAndGenres.SongRepo;
import com.muson.domain.MusUser;
import com.muson.domain.Role;
import com.muson.playlists.Playlist;
import com.muson.recommendersystem.RecSystem;
import com.muson.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin()
public class UserResource {
    private final UserService userService;

    @PostMapping("/myLogin")
    @CrossOrigin()
    public String myLogin(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password)
    {
        String url = "http://localhost:8080/api/login?username=" + username + "&password=" + password;
        System.out.println("In myLogin " + url);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject(url, null, String.class);
        return result;
    }

    @GetMapping("/getAllByGenre")
    @CrossOrigin()
    public List<Song> getAllByGenre(@RequestParam(value = "genre") String genre)
    {
        System.out.println("in getAllByGenre.");
        return userService.getAllSongsByGenre(genre);
    }

    @GetMapping("/getArtistsSongs")
    @CrossOrigin()
    public List<Song> getAllByArtist(@RequestParam(value = "artist") String artist)
    {
        return userService.getAllSongsByArtist(artist);
    }

    @GetMapping("/getAllGenres")
    @CrossOrigin()
    public List<Genre> getAllGenres()
    {
        return userService.getAllGenres();
    }

    @GetMapping("/getAllArtists")
    @CrossOrigin()
    public List<Artist> getAllArtist()
    {
        return userService.getAllArtists();
    }

    @GetMapping("/getLikedSongs")
    @CrossOrigin()
    public ArrayList<Song> getLikedSongs(HttpServletRequest request)
    {
        System.out.println("In getLikedSongs()");
        return userService.getLikedSongs(request.getParameter("username"));
    }

    @GetMapping("/createRandomPlaylist")
    public ArrayList<Song> createRandomPlaylist()
    {
        Playlist playlist = new Playlist(10);
        RecSystem recSystem = new RecSystem();
        recSystem.RecommendRandomSongs(playlist, userService);
        return playlist.getSongs();
    }

    @GetMapping("/createRandomFavPlaylist")
    public ArrayList<Song> createRandomFavPlaylist(HttpServletRequest request)
    {
        Playlist playlist = new Playlist(10);
        RecSystem recSystem = new RecSystem();
        recSystem.RecommendRandomFavouriteSongs(playlist, userService, request.getParameter("username"));
        return playlist.getSongs();
    }

    @GetMapping("/createDailyPlaylist")
    public ArrayList<Song> createDailyPlaylist(HttpServletRequest request)
    {
        Playlist playlist = new Playlist(30);
        RecSystem recSystem = new RecSystem();
        recSystem.FillDailyPlaylist(playlist, userService, request.getParameter("username"));
        return playlist.getSongs();
    }

    @GetMapping("/users")
    public ResponseEntity<List<MusUser>>getUsers()
    {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("/identify")
    public String identify(HttpServletRequest request)
    {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        return username;
    }

    @PostMapping("/registration")
    public ResponseEntity<?>saveUser(HttpServletRequest request)
    {
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/registration")
                        .toUriString());
        MusUser mem_user = new MusUser(null, request.getParameter("name"), request.getParameter("username"), request.getParameter("password"), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        try
        {
            if (!UniquenessIdentifier.IsUserUnique(mem_user, userService))
            {
                throw new Exception();
            }
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body("This username has already taken by another user.");
        }
        userService.saveUser(mem_user);
        userService.addRoleToUser(mem_user.getUsername(), "ROLE_USER");
        return ResponseEntity.created(uri).body(mem_user);
    }

    @PostMapping("/song/dislike")
    public void addDislikedSongToUser(HttpServletRequest request)
    {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        userService.addDislikedSongToUser(username, Integer.parseInt(request.getParameter("id")));
    }

    @PostMapping("/song/like")
    @CrossOrigin()
    public void addFavouriteSongToUser(HttpServletRequest request)
    {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        userService.addFavSongToUser(username, Integer.parseInt(request.getParameter("id")));
    }


    @PostMapping("/role/save")
    public ResponseEntity<Role>saveRole(@RequestBody Role role)
    {
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/role/save")
                        .toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?>addRoleToUser(@RequestBody RoleToUserForm form)
    {
        userService.addRoleToUser(form.getUsername(), form.getRolename());
        return ResponseEntity.ok().build();
    }

    //TODO: Refresh token doesn't work.
    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                MusUser user = userService.getUser(username);
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }
        else {
            throw new RuntimeException("Refresh token is missing.");
        }
    }
}

class UniquenessIdentifier
{
    static boolean IsUserUnique(MusUser user, UserService userService)
    {
        if (userService.getUser(user.getUsername()) != null)
        {
            return false;
        }

        return true;
    }
}

@Data
class RoleToUserForm{
    private String username;
    private String rolename;
}

@Data
class SongToUserForm{
    private String username;
    private int songId;
}
