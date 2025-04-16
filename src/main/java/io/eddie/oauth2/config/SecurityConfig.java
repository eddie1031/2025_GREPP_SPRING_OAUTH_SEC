package io.eddie.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(formLogin -> formLogin.disable())
                .oauth2Login(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/login", "/sign-up")
                            .anonymous()
                        .requestMatchers("/user/**")
                            .hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/admin/**")
                            .hasAnyAuthority("ADMIN")
                        .anyRequest()
                            .authenticated();
                })
                .build();
    }

}
