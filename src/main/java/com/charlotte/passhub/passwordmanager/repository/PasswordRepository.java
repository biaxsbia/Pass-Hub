package com.charlotte.passhub.passwordmanager.repository;

import com.charlotte.passhub.passwordmanager.model.Password;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PasswordRepository extends JpaRepository<Password, Long> {
    List<Password> findByUser(String user);
    Optional<Password> findByIdAndUser(Long id, String user);
    void deleteByIdAndUser(Long id, String user);
}