package com.md.authservice.controllers;

import com.md.authservice.dtos.AuthTokenDto;
import com.md.authservice.dtos.LoginUserDto;
import com.md.authservice.dtos.RegisterUserDto;
import com.md.authservice.entities.User;
import com.md.authservice.services.AuthenticationService;
import com.md.authservice.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) throws Exception {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthTokenDto> authenticate(@RequestBody LoginUserDto loginUserDto) throws UsernameNotFoundException {
        User authenticatedUser = authenticationService.login(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        AuthTokenDto authTokenDto = new AuthTokenDto().setToken(jwtToken).setExpiresIn(jwtService.getJwtExpiration());

        return ResponseEntity.ok(authTokenDto);
    }
}
