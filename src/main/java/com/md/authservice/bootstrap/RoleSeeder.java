package com.md.authservice.bootstrap;

import com.md.authservice.entities.Role;
import com.md.authservice.entities.RoleEnum;
import com.md.authservice.repositories.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.loadRoles();
    }

    private void loadRoles() {
        RoleEnum[] roles = RoleEnum.values();

        for (RoleEnum role : roles) {
            Optional<Role> optionalRole = roleRepository.findByName(role);

            optionalRole.ifPresentOrElse(System.out::println, () -> {
                Role roleToAdd = new Role().setName(role).setDescription(role.description);

                roleRepository.save(roleToAdd);
            });
        }
    }
}
