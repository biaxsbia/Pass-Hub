package com.charlotte.passhub.passwordmanager.repository;

import com.charlotte.passhub.passwordmanager.model.Password;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordRepository extends JpaRepository<Password, Long> {
    List<Password> findByUserId(Long userId);

}
