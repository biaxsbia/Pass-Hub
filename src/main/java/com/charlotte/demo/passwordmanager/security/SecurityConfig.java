import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desabilita CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/passwords/**").authenticated() // Exige autenticação para /passwords/**
                        .anyRequest().permitAll() // Permite acesso a qualquer outra requisição
                )
                .httpBasic(); // Usa autenticação básica (usuário/senha)

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Cria um usuário em memória
        UserDetails user = User.builder()
                .username("admin") // Nome de usuário
                .password(passwordEncoder().encode("admin123")) // Senha codificada
                .roles("USER") // Papel do usuário
                .build();

        return new InMemoryUserDetailsManager(user); // Retorna o gerenciador de usuários em memória
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Configura o codificador de senhas
    }
}