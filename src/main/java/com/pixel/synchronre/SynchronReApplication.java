package com.pixel.synchronre;

import com.pixel.synchronre.authmodule.controller.repositories.UserRepo;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import jakarta.persistence.Column;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
public class SynchronReApplication {

    public static void main(String[] args) {
        SpringApplication.run(SynchronReApplication.class, args);
    }

    @Bean
    public CommandLineRunner start(UserRepo userRepo, PasswordEncoder pe)
    {
        return args->{
            AppUser user = new AppUser(1l, "admin", "admin", null,
                    pe.encode("1234"), "admin@gmail.com", "1234",
                    true, true, null, LocalDateTime.now(),
                    LocalDateTime.now());
            userRepo.save(user);
        };
    }
}
