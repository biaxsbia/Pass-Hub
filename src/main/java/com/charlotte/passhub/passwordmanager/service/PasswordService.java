package com.charlotte.passhub.passwordmanager.service;

import com.charlotte.passhub.passwordmanager.model.Password;
import com.charlotte.passhub.passwordmanager.model.User;
import com.charlotte.passhub.passwordmanager.repository.PasswordRepository;
import com.charlotte.passhub.passwordmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordService {

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private UserRepository userRepository;


    private final TextEncryptor encryptor = Encryptors.text("password", "5c0744940b5c369b");

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
        if (password.getUser() == null || password.getUser().getId() == null) {
            throw new IllegalArgumentException("Usuário deve ser informado");
        }

        User user = userRepository.findById(password.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        password.setUser(user);

        if (password.getId() != null) {
            Password existing = passwordRepository.findById(password.getId()).orElse(null);
            if (existing != null && password.getEncryptedPassword().equals(existing.getEncryptedPassword())) {
                return passwordRepository.save(password);
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
