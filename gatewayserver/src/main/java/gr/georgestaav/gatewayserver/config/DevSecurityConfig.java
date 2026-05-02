package gr.georgestaav.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@Profile("dev")
public class DevSecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> exchanges
                .anyExchange().permitAll()
        );
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }
}