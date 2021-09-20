package com.muson.service;



import com.muson.domain.MusUser;
import com.muson.domain.Role;

import java.util.List;

public interface UserService {
    MusUser saveUser(MusUser user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    void addFavSongToUser(String username, int songId);
    MusUser getUser(String username);
    List<MusUser>getUsers();
}
