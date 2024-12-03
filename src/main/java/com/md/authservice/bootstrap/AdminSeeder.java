package com.md.authservice.bootstrap;

import com.md.authservice.entities.Role;
import com.md.authservice.entities.RoleEnum;
import com.md.authservice.entities.User;
import com.md.authservice.repositories.RoleRepository;
import com.md.authservice.repositories.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.createSuperAdministrator();
    }

    private void createSuperAdministrator() {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.SUPER_ADMIN);
        Optional<User> optionalUser = userRepository.findByEmail("superadmin@gmail.com");

        if (optionalRole.isPresent() && optionalUser.isEmpty()) {
            User superAdmin = new User()
                    .setFullName("Super Admin")
                    .setEmail("superadmin@gmail.com")
                    .setPassword(passwordEncoder.encode("superadmin"))
                    .setRole(optionalRole.get());

            userRepository.save(superAdmin);
        }
    }
}
