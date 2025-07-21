package com.FortuneDay.fortune.repository;

import com.FortuneDay.fortune.entity.User;
import com.FortuneDay.fortune.entity.Provider;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);
    
    Optional<User> findByProviderAndProviderId(Provider provider, String providerId);
}
