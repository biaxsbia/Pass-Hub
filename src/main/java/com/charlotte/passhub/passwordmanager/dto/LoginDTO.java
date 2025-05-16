package com.charlotte.passhub.passwordmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class LoginDTO {
    @Email(message = "Email inválido")
    private String email;

    @NotNull(message = "Senha não pode ser nula")
    private String password;

    private String totp;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTotp() {
        return totp;
    }

    public void setTotp(String totp) {
        this.totp = totp;
    }
}

