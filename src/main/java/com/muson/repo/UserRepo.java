package com.muson.repo;

import com.muson.domain.MusUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<MusUser, Long> {
    MusUser findByUsername(String username);
}
