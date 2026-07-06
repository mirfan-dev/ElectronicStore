package com.lcwd.electronic.store;

import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.repositories.RoleRepository;
import com.lcwd.electronic.store.util.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


import java.util.UUID;

@SpringBootApplication
@EnableCaching
public class ElectronicStoreApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(ElectronicStoreApplication.class, args);
    }



    @Autowired
    private RoleRepository roleRepository;



    @Override
    public void run(String... args) {

        createRoleIfNotExists("ROLE_" + AppConstant.ADMIN_ROLE);
        createRoleIfNotExists("ROLE_" + AppConstant.GUEST_ROLE);
    }

    private void createRoleIfNotExists(String roleName) {

        roleRepository.findFirstByRoleName(roleName).ifPresentOrElse(
                role -> System.out.println(roleName + " already exists"),
                () -> {
                    Role role = new Role();
                    role.setRoleId(UUID.randomUUID().toString());
                    role.setRoleName(roleName);
                    roleRepository.save(role);
                }
        );
    }
}
