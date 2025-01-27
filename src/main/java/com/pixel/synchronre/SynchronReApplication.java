package com.pixel.synchronre;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.config.AuditorAwareImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class SynchronReApplication
{
    public static void main(String[] args) {
        SpringApplication.run(SynchronReApplication.class, args);
    }
}
