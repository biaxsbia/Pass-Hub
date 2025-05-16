package com.charlotte.passhub.passwordmanager.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TwoFactorAuthService {
    private final GoogleAuthenticator gAuth;

    public TwoFactorAuthService() {
        // Use configuração padrão
        this.gAuth = new GoogleAuthenticator();
    }

    public String generateSecretKey() {
        return gAuth.createCredentials().getKey();
    }

    public boolean isCodeValid(String secret, int code) {
        try {
            return gAuth.authorize(secret, code);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}


