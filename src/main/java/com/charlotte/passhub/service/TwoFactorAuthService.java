package com.charlotte.passhub.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;


@Service
public class TwoFactorAuthService {

    private final GoogleAuthenticator gAuth;

    @Value("${app.totp.encryption.password}")
    private String totpEncryptionPassword;

    @Value("${app.totp.encryption.salt}")
    private String totpEncryptionSalt;

    public TwoFactorAuthService() {
        this.gAuth = new GoogleAuthenticator();
    }

    /**
     * O TextEncryptor deve ser criado sob demanda ou após a injeção das dependências.
     * Se for criado no construtor, os valores de @Value ainda serão nulos.
     */
    private TextEncryptor getEncryptor() {
        return Encryptors.text(totpEncryptionPassword, totpEncryptionSalt);
    }

    public String generateSecretKey() {
        return gAuth.createCredentials().getKey();
    }

    public String encryptSecret(String secret) {
        return getEncryptor().encrypt(secret);
    }

    public String decryptSecret(String encryptedSecret) {
        return getEncryptor().decrypt(encryptedSecret);
    }

    public boolean isCodeValid(String encryptedSecret, int code) {
        try {
            // Descriptografa o segredo antes de validar o código TOTP
            String secret = decryptSecret(encryptedSecret);
            return gAuth.authorize(secret, code);
        } catch (Exception e) {
            // Log de erro pode ser útil aqui para depuração
            return false;
        }
    }
}