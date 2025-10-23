package com.isateca.packages.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.isateca.packages.roles.RoleEntity;
import com.isateca.packages.roles.RoleRepository;
import com.isateca.packages.users.UserEntity;
import com.isateca.packages.users.UserRepository;


@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        if (roleRepository.findByName("ADMIN").isEmpty()) {
            System.out.println("Creando rol ADMIN...");
            RoleEntity adminRole = new RoleEntity();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
            System.out.println("Rol ADMIN creado.");
        }

        if (roleRepository.findByName("USER").isEmpty()) {
            System.out.println("Creando rol USER...");
            RoleEntity userRole = new RoleEntity();
            userRole.setName("USER");
            roleRepository.save(userRole);
            System.out.println("Rol USER creado.");
        }

        if (userRepository.findByUsername("admin").isEmpty()) {
            System.out.println("Creando usuario admin...");

            RoleEntity adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Error: Rol ADMIN no encontrado."));

            UserEntity adminUser = new UserEntity();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setEmail("admin@isateca.com");
            adminUser.setFirstName("Administrador");
            adminUser.setLastname("DelSistema");
            adminUser.setRole(adminRole);

            userRepository.save(adminUser);
            System.out.println("Usuario admin creado.");
        }
    }
}