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
        GoogleAuthenticatorConfig config = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
                .setTimeStepSizeInMillis(TimeUnit.SECONDS.toMillis(30))
                .setWindowSize(5)  // Janela maior (5 steps = 2.5 minutos)
                .setNumberOfScratchCodes(0)
                .setCodeDigits(6)
                .build();
        this.gAuth = new GoogleAuthenticator(config);
    }

    public String generateSecretKey() {
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }

    public boolean isCodeValid(String secret, int code) {
        try {
            // Verifica em uma janela maior (5 steps = 2.5 minutos)
            return gAuth.authorize(secret, code, 5);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

