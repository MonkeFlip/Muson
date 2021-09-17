package com.muson.Authorization;

import org.springframework.data.repository.CrudRepository;

public interface UserDAO extends CrudRepository<User, Integer> {
    User save(User user);
}
