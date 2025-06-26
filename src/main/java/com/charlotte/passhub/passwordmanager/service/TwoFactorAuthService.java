package com.charlotte.passhub.passwordmanager.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class TwoFactorAuthService {

    Logger logger = LoggerFactory.getLogger(TwoFactorAuthService.class);
    private final GoogleAuthenticator gAuth;

    public TwoFactorAuthService() {
        this.gAuth = new GoogleAuthenticator();
    }

    public String generateSecretKey() {
        return gAuth.createCredentials().getKey();
    }

    public boolean isCodeValid(String secret, int code) {
        try {
            return gAuth.authorize(secret, code);
        } catch (Exception e) {
            logger.error("Erro");
            return false;
        }
    }
}


