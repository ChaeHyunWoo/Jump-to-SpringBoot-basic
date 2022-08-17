package com.mysite.ssb.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// SiteUser의 PK의 타입은 Long
public interface UserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByusername(String username);
}
