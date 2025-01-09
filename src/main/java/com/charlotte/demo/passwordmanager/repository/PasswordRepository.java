package com.charlotte.demo.passwordmanager.repository;

import com.charlotte.demo.passwordmanager.model.Password;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRepository extends JpaRepository<Password, Long> {
}
