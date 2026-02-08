package com.charlotte.passhub.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;


@Service
public class TwoFactorAuthService {

    private final GoogleAuthenticator gAuth;
    private final TextEncryptor encryptor;

    public TwoFactorAuthService() {
        this.gAuth = new GoogleAuthenticator();
        this.encryptor = Encryptors.text("totp-secret", "5c0744940b5c369b");
    }

    public String generateSecretKey() {
        return gAuth.createCredentials().getKey();
    }

    public String encryptSecret(String secret) {
        return encryptor.encrypt(secret);
    }

    public String decryptSecret(String encryptedSecret) {
        return encryptor.decrypt(encryptedSecret);
    }

    public boolean isCodeValid(String encryptedSecret, int code) {
        try {
            String secret = decryptSecret(encryptedSecret);
            return gAuth.authorize(secret, code);
        } catch (Exception e) {
            return false;
        }
    }
}



