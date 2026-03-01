package com.charlotte.passhub.service;

import com.charlotte.passhub.model.Password;
import com.charlotte.passhub.model.User;
import com.charlotte.passhub.repository.PasswordRepository;
import com.charlotte.passhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class PasswordService {

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private UserRepository userRepository;

    @org.springframework.beans.factory.annotation.Value("${app.encryption.password}")
    private String encryptionPassword;

    @org.springframework.beans.factory.annotation.Value("${app.encryption.salt}")
    private String encryptionSalt;

    private TextEncryptor getEncryptor() {
        return Encryptors.text(encryptionPassword, encryptionSalt);
    }

    public List<Password> findAll() {
        User user = getAuthenticatedUser();
        return passwordRepository.findByUserId(user.getId());
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User) {
            return (User) auth.getPrincipal();
        }
        throw new RuntimeException("Usuário não autenticado");
    }

    public Password save(Password password) {
        User user = getAuthenticatedUser();
        password.setUserId(user.getId());

        if (password.getId() == null) {
            password.setId(UUID.randomUUID().toString());
            password.setCreatedAt(Instant.now().toString());
        }
        password.setUpdatedAt(Instant.now().toString());

        if (password.getEncryptedPassword() != null) {
            password.setEncryptedPassword(getEncryptor().encrypt(password.getEncryptedPassword()));
        }

        return passwordRepository.save(password);
    }

    public void deleteById(String id) {
        User user = getAuthenticatedUser();
        Password password = passwordRepository.findById(id).orElse(null);
        if (password != null && password.getUserId().equals(user.getId())) {
            passwordRepository.deleteById(id);
        } else {
            throw new RuntimeException("Acesso negado ou senha não encontrada");
        }
    }

    public Password findById(String id) {
        User user = getAuthenticatedUser();
        Password password = passwordRepository.findById(id).orElse(null);

        if (password == null || !password.getUserId().equals(user.getId())) {
            throw new RuntimeException("Acesso negado ou senha não encontrada");
        }

        if (password.getEncryptedPassword() != null) {
            try {
                password.setEncryptedPassword(getEncryptor().decrypt(password.getEncryptedPassword()));
            } catch (Exception e) {
                // Se não conseguir descriptografar, mantém o original
            }
        }
        return password;
    }
}