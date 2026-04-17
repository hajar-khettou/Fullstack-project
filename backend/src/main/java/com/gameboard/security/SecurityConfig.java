package com.gameboard.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        return new InMemoryUserDetailsManager(
            User.withUsername("user").password(encoder.encode("user")).roles("USER").build(),
            User.withUsername("editor").password(encoder.encode("editor")).roles("EDITOR").build(),
            User.withUsername("admin").password(encoder.encode("admin")).roles("WEBMASTER").build()
        );
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/api/games/pending").hasRole("WEBMASTER")
                .requestMatchers(HttpMethod.GET, "/api/games/my-proposals").hasAnyRole("EDITOR", "WEBMASTER")
                .requestMatchers(HttpMethod.GET, "/api/games", "/api/games/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/games/{id}/ratings").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/games").hasAnyRole("EDITOR", "WEBMASTER")
                .requestMatchers(HttpMethod.POST, "/api/games/bgg/**").hasRole("WEBMASTER")
                .requestMatchers(HttpMethod.POST, "/api/games/{id}/ratings").hasAnyRole("USER", "EDITOR", "WEBMASTER")
                .requestMatchers(HttpMethod.PATCH, "/api/games/{id}/status").hasRole("WEBMASTER")
                .requestMatchers(HttpMethod.PUT, "/api/games/{id}").hasAnyRole("EDITOR", "WEBMASTER")
                .requestMatchers(HttpMethod.DELETE, "/api/games/{id}").hasAnyRole("EDITOR", "WEBMASTER")
                .requestMatchers("/actuator/**", "/h2-console/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
