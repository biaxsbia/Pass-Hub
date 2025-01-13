package com.charlotte.passhub.passwordmanager.repository;

import com.charlotte.passhub.passwordmanager.model.Password;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRepository extends JpaRepository<Password, Long> {
}
