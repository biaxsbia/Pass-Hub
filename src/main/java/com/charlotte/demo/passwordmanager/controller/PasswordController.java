package com.charlotte.demo.passwordmanager.controller;

import com.charlotte.demo.passwordmanager.model.Password;
import com.charlotte.demo.passwordmanager.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passwords")
public class PasswordController {

    @Autowired
    private PasswordService passwordService;

    @GetMapping
    public List<Password> getAllPasswords() {
        return passwordService.findAll();
    }

    @PostMapping
    public Password addPassword(@RequestBody Password password) {
        return passwordService.save(password);
    }

    @GetMapping("/{id}")
    public Password getPasswordById(@PathVariable Long id) {
        return passwordService.findById(id);
    }

    @PutMapping("/{id}")
    public Password updatePassword(@PathVariable Long id, @RequestBody Password password) {
        password.setId(id);
        return passwordService.save(password);
    }

    @DeleteMapping("/{id}")
    public void deletePassword(@PathVariable Long id) {
        passwordService.deleteById(id);
    }
}
