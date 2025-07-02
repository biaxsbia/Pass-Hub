package com.charlotte.passhub.passwordmanager.dto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Data
class LoginRequest {
    private String email;
    private String password;
    private int totpCode;

}


