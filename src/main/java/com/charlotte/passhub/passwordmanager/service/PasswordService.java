package com.charlotte.passhub.passwordmanager.service;

import com.charlotte.passhub.passwordmanager.model.Password;
import com.charlotte.passhub.passwordmanager.repository.PasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordService {

    @Autowired
    private PasswordRepository passwordRepository;

    private final TextEncryptor encryptor = Encryptors.text("password", "5c0744940b5c369b");

    public List<Password> findAll() {
        return passwordRepository.findAll();
    }

    public Password save(Password password) {
        if (password.getId() != null) {
            Password existing = passwordRepository.findById(password.getId()).orElse(null);
            if (existing != null) {
                if (password.getEncryptedPassword().equals(existing.getEncryptedPassword())) {
                    return passwordRepository.save(password);
                }
            }
        }
        password.setEncryptedPassword(encryptor.encrypt(password.getEncryptedPassword()));
        return passwordRepository.save(password);
    }

    public void deleteById(Long id) {
        passwordRepository.deleteById(id);
    }

    public Password findById(Long id) {
        Password password = passwordRepository.findById(id).orElse(null);
        if (password != null) {
            password.setEncryptedPassword(encryptor.decrypt(password.getEncryptedPassword()));
        }
        return password;
    }
}
