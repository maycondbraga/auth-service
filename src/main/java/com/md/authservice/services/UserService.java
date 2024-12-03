package com.md.authservice.services;

import com.md.authservice.entities.RoleEnum;
import com.md.authservice.entities.User;
import com.md.authservice.repositories.RoleRepository;
import com.md.authservice.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }

    public void updateUserRole(UUID userId, RoleEnum roleToUpdate) throws NoSuchElementException {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            user.setRole(roleRepository.findByName(roleToUpdate)
                    .orElseThrow(() -> new NoSuchElementException("No administrator role found")));

            userRepository.save(user);
        } else {
            throw new NoSuchElementException("User not found");
        }
    }
}
