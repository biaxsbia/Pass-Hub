package com.charlotte.passhub.passwordmanager.dto;
import lombok.Data;
import lombok.Getter;


@Getter
@Data
class LoginRequest {
    private String email;
    private String password;
    private int totpCode;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTotpCode(int totpCode) {
        this.totpCode = totpCode;
    }
}


