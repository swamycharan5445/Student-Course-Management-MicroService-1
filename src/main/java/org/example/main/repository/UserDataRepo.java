package org.example.main.repository;

import org.example.main.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDataRepo extends JpaRepository<UserData, Long>
{
    Optional<UserData> findByUsername(String username);
}
