
package br.com.mcpfarma.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    
@Bean
SecurityFilterChain securityFilterChain(HttpSecurity http)
        throws Exception {

    return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/actuator/health").permitAll()


                    // Catálogo público temporariamente bloqueado
                    .requestMatchers("/api/v1/catalogo-publico/**").denyAll()

                    // MCP bloqueado até configurar autenticação específica
                    // .requestMatchers("/api/v1/mcp/**").denyAll()
                    .requestMatchers("/api/v1/mcp", "/api/v1/mcp/**").denyAll()

                    // APIs administrativas
                    .requestMatchers("/api/v1/produtos/**").hasRole("ADMIN")
                    .requestMatchers("/api/v1/importacoes/**").hasRole("ADMIN")

                    // Demais endpoints internos permanecem restritos
                    .requestMatchers("/api/v1/**").hasRole("ADMIN")

                    // Bloqueia qualquer outra rota
                    .anyRequest().denyAll()
            )
            .httpBasic(Customizer.withDefaults())
            .build();
}


    
    @Bean
    UserDetailsService userDetailsService(
        PasswordEncoder passwordEncoder) {

        String username = System.getenv("MCP_FARMA_ADMIN_USER");
        String password = System.getenv("MCP_FARMA_ADMIN_PASSWORD");

        if (username == null || username.isBlank()) {
            throw new IllegalStateException(
                    "Variável MCP_FARMA_ADMIN_USER não configurada"
            );
        }

        if (password == null || password.isBlank()) {
            throw new IllegalStateException(
                    "Variável MCP_FARMA_ADMIN_PASSWORD não configurada"
            );
        }

        var usuario = User.withUsername(username)
                .password(passwordEncoder.encode(password))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(usuario);
}


    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories
                .createDelegatingPasswordEncoder();
    }
}
