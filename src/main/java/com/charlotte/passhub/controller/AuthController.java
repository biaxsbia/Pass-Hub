package com.charlotte.passhub.controller;

import com.charlotte.passhub.dto.LoginDTO;
import com.charlotte.passhub.dto.UserDTO;
import com.charlotte.passhub.model.User;
import com.charlotte.passhub.repository.UserRepository;
import com.charlotte.passhub.service.TwoFactorAuthService;
import com.charlotte.passhub.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TwoFactorAuthService twoFactorAuthService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    @Operation(summary = "Registrar usuário",
            description = "Registra um novo usuário no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "409", description = "Email já registrado")
    })
    public ResponseEntity<?> register(@RequestBody UserDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já registrado");
        }

        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        String totpSecret = twoFactorAuthService.generateSecretKey();


        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(dto.getEmail());
        user.setPassword(hashedPassword);
        user.setTotpSecret(twoFactorAuthService.encryptSecret(totpSecret));

        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "message", "Usuário registrado com sucesso",
                "totpSecret", totpSecret,
                "otpAuthUrl", "otpauth://totp/PasswordManager:" + dto.getEmail() + "?secret=" + totpSecret + "&issuer=PasswordManager"
        ));
    }

    @PostMapping("/login")
    @Operation(summary = "Login",
            description = "Loga o usuário no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "401", description = "Senha incorreta"),
            @ApiResponse(responseCode = "206", description = "Código TOTP necessário"),
            @ApiResponse(responseCode = "401", description = "Código TOTP inválido"),
            @ApiResponse(responseCode = "400", description = "Código TOTP deve ser numérico"),
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido"),
    })
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        Optional<User> userOpt = userRepository.findByEmail(loginDTO.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não encontrado");
        }

        User user = userOpt.get();

        boolean isPasswordValid = passwordEncoder.matches(loginDTO.getPassword(), user.getPassword());
        if (!isPasswordValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta");
        }

        if (loginDTO.getTotp() == null || loginDTO.getTotp().isEmpty()) {
            return ResponseEntity.status(206).body("Código TOTP necessário");
        }

        try {
            int userTotp = Integer.parseInt(loginDTO.getTotp());
            boolean isTotpValid = twoFactorAuthService.isCodeValid(user.getTotpSecret(), userTotp);

            if (!isTotpValid) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Código TOTP inválido");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código TOTP deve ser numérico");
        }

        String token = jwtUtil.generateToken(user.getId());

        ResponseCookie cookie = ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofHours(24))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(Map.of(
                "message", "Login bem-sucedido"
        ));
    }

}
