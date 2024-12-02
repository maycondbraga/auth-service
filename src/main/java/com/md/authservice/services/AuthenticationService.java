package com.md.authservice.services;

import com.md.authservice.dtos.LoginUserDto;
import com.md.authservice.dtos.RegisterUserDto;
import com.md.authservice.entities.Role;
import com.md.authservice.entities.RoleEnum;
import com.md.authservice.entities.User;
import com.md.authservice.repositories.RoleRepository;
import com.md.authservice.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User signup(RegisterUserDto registerUserDto) throws Exception {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);

        User user = new User()
                .setFullName(registerUserDto.getFullName())
                .setEmail(registerUserDto.getEmail())
                .setPassword(passwordEncoder.encode(registerUserDto.getPassword()))
                .setRole(optionalRole.orElseThrow(() -> new Exception("User role not defined")));

        return userRepository.save(user);
    }

    public User login(LoginUserDto loginUserDto) throws UsernameNotFoundException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword())
        );

        return userRepository.findByEmail(loginUserDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
