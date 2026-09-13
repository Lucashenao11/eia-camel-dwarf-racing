package com.example.camel_dwarf_racing_api.service;

import com.example.camel_dwarf_racing_api.dto.AuthResponseDto;
import com.example.camel_dwarf_racing_api.dto.LoginRequestDto;
import com.example.camel_dwarf_racing_api.dto.RegisterRequestDto;
import com.example.camel_dwarf_racing_api.exception.DuplicateUsernameException;
import com.example.camel_dwarf_racing_api.model.Role;
import com.example.camel_dwarf_racing_api.model.User;
import com.example.camel_dwarf_racing_api.repository.UserRepository;
import com.example.camel_dwarf_racing_api.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                        JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponseDto register(RegisterRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new DuplicateUsernameException(requestDto.getUsername());
        }

        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRole(Role.VIEWER); // self-registration always creates a VIEWER

        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        return new AuthResponseDto(token, user.getUsername(), user.getRole().name());
    }

    public AuthResponseDto login(LoginRequestDto requestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword()));

        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(); // safe: authenticate() above already confirmed this user exists

        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        return new AuthResponseDto(token, user.getUsername(), user.getRole().name());
    }
}