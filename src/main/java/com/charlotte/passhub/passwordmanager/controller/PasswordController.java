package com.charlotte.passhub.passwordmanager.controller;

import com.charlotte.passhub.passwordmanager.model.Password;
import com.charlotte.passhub.passwordmanager.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passwords")
public class PasswordController {

    @Autowired
    private PasswordService passwordService;

    @GetMapping
    @PreAuthorize("hasRole('USER')") // Apenas usuários autenticados com a role USER podem acessar
    public List<Password> getAllPasswords(Authentication authentication) {
        String username = authentication.getName(); // Obtém o nome do usuário autenticado
        return passwordService.findByUser(username); // Retorna as senhas do usuário
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')") // Apenas usuários autenticados com a role USER podem acessar
    public Password addPassword(@RequestBody Password password, Authentication authentication) {
        String username = authentication.getName(); // Obtém o nome do usuário autenticado
        password.setUser(username); // Associa a senha ao usuário
        return passwordService.save(password);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')") // Apenas usuários autenticados com a role USER podem acessar
    public Password getPasswordById(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName(); // Obtém o nome do usuário autenticado
        return passwordService.findByIdAndUser(id, username); // Retorna a senha apenas se pertencer ao usuário
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')") // Apenas usuários autenticados com a role USER podem acessar
    public Password updatePassword(@PathVariable Long id, @RequestBody Password password, Authentication authentication) {
        String username = authentication.getName(); // Obtém o nome do usuário autenticado
        password.setId(id);
        password.setUser(username); // Atualiza a senha apenas se pertencer ao usuário
        return passwordService.save(password);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')") // Apenas usuários autenticados com a role USER podem acessar
    public void deletePassword(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName(); // Obtém o nome do usuário autenticado
        passwordService.deleteByIdAndUser(id, username); // Exclui a senha apenas se pertencer ao usuário
    }
}