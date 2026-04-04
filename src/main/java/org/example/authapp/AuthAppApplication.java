package org.example.authapp;

import org.example.authapp.Repositories.RoleRepository;
import org.example.authapp.config.AppConstants;
import org.example.authapp.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.UUID;

@SpringBootApplication
@EnableJpaAuditing
public class AuthAppApplication implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;
    public static void main(String[] args) {
        SpringApplication.run(AuthAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //we will create some default user//ADMIN    //GUEST
        roleRepository.findByName("ROLE_"+AppConstants.ADMIN_ROLE).ifPresentOrElse(role->{
            System.out.println("Admin role already exist"+role.getName());
        },()->{
            Role role=new Role();
            role.setName("ROLE_"+AppConstants.ADMIN_ROLE);
            role.setId(UUID.randomUUID());
            roleRepository.save(role);
        });
        roleRepository.findByName("ROLE_"+AppConstants.GUEST_ROLE).ifPresentOrElse(role->{
            System.out.println("Guest role already exists"+role.getName());
        },()->{
            Role role=new Role();
            role.setName("ROLE_"+AppConstants.GUEST_ROLE);
            role.setId(UUID.randomUUID());
            roleRepository.save(role);
        });
    }
}
