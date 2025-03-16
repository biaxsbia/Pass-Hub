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

    public List<Password> findByUser(String user) {
        List<Password> passwords = passwordRepository.findByUser(user);
        passwords.forEach(password -> {
            password.setEncryptedPassword(encryptor.decrypt(password.getEncryptedPassword()));
        });
        return passwords;
    }

    public Password save(Password password) {
        password.setEncryptedPassword(encryptor.encrypt(password.getEncryptedPassword()));
        return passwordRepository.save(password);
    }

    public void deleteByIdAndUser(Long id, String user) {
        passwordRepository.deleteByIdAndUser(id, user);
    }

    public Password findByIdAndUser(Long id, String user) {
        Password password = passwordRepository.findByIdAndUser(id, user).orElse(null);
        if (password != null) {
            password.setEncryptedPassword(encryptor.decrypt(password.getEncryptedPassword()));
        }
        return password;
    }
}