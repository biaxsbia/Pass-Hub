package com.charlotte.passhub.passwordmanager.controller;

import com.charlotte.passhub.passwordmanager.dto.LoginDTO;
import com.charlotte.passhub.passwordmanager.dto.UserDTO;
import com.charlotte.passhub.passwordmanager.model.User;
import com.charlotte.passhub.passwordmanager.repository.UserRepository;
import com.charlotte.passhub.passwordmanager.service.TwoFactorAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TwoFactorAuthService twoFactorAuthService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já registrado");
        }

        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        String totpSecret = twoFactorAuthService.generateSecretKey();

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(hashedPassword);
        user.setTotpSecret(totpSecret);

        userRepository.save(user);

        // Retorne o segredo para o frontend gerar o QR Code
        return ResponseEntity.ok(Map.of(
                "message", "Usuário registrado",
                "totpSecret", totpSecret,
                "otpAuthUrl", "otpauth://totp/PasswordManager:" + dto.getEmail() + "?secret=" + totpSecret + "&issuer=PasswordManager"
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        Optional<User> userOpt = userRepository.findByEmail(loginDTO.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não encontrado");
        }

        User user = userOpt.get();
        boolean isPasswordValid = passwordEncoder.matches(loginDTO.getPassword(), user.getPassword());

        if (!isPasswordValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta");
        }

        try {
            int userTotp = Integer.parseInt(loginDTO.getTotp());

            // Logs para debug
            System.out.println("=== DEBUG TOTP ===");
            System.out.println("Secret: " + user.getTotpSecret());
            System.out.println("Código fornecido: " + userTotp);
            System.out.println("Tempo atual (ms): " + System.currentTimeMillis());

            boolean isTotpValid = twoFactorAuthService.isCodeValid(user.getTotpSecret(), userTotp);

            if (!isTotpValid) {
                System.out.println("Código TOTP inválido para o secret fornecido");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Código TOTP inválido");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código TOTP deve ser numérico");
        }

        return ResponseEntity.ok("Login bem-sucedido");
    }
}
