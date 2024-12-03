package com.md.authservice.controllers;

import com.md.authservice.entities.RoleEnum;
import com.md.authservice.entities.User;
import com.md.authservice.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}/role")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> updateUserRole(@PathVariable UUID userId, @RequestParam RoleEnum role) {
        userService.updateUserRole(userId, role);
        return ResponseEntity.ok().build();
    }
}
