package com.example.camel_dwarf_racing_api.config;

import com.example.camel_dwarf_racing_api.security.CustomAccessDeniedHandler;
import com.example.camel_dwarf_racing_api.security.CustomAuthenticationEntryPoint;
import com.example.camel_dwarf_racing_api.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter,
                           CustomAuthenticationEntryPoint authenticationEntryPoint,
                           CustomAccessDeniedHandler accessDeniedHandler,
                           UserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler))
            .authorizeHttpRequests(auth -> auth
                    // Public
                    .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()

                    // Audit log — admin only
                    .requestMatchers("/api/audit-logs/**").hasRole("ADMINISTRATOR")

                    // Competitors / Teams — write: admin only, read: admin + organizer
                    .requestMatchers(HttpMethod.GET, "/api/competitors/**", "/api/teams/**")
                        .hasAnyRole("ADMINISTRATOR", "RACE_ORGANIZER")
                    .requestMatchers("/api/competitors/**", "/api/teams/**")
                        .hasRole("ADMINISTRATOR")

                    // Races, Results, Standings — public reads
                    .requestMatchers(HttpMethod.GET, "/api/races/**", "/api/results/**", "/api/standings/**")
                        .hasAnyRole("ADMINISTRATOR", "RACE_ORGANIZER", "VIEWER")

                    // Registrations — reads restricted to admin/organizer (not "public info")
                    .requestMatchers(HttpMethod.GET, "/api/registrations/**")
                        .hasAnyRole("ADMINISTRATOR", "RACE_ORGANIZER")

                    // Races, Registrations, Results — writes: admin + organizer
                    .requestMatchers("/api/races/**", "/api/registrations/**", "/api/results/**")
                        .hasAnyRole("ADMINISTRATOR", "RACE_ORGANIZER")

                    // Anything authenticated (e.g. /api/auth/profile)
                    .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}