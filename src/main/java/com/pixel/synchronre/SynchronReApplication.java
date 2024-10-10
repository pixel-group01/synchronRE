package com.pixel.synchronre;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

@SpringBootApplication
public class SynchronReApplication
{
    public static void main(String[] args) {
        SpringApplication.run(SynchronReApplication.class, args);
    }
    @Bean
    public DecimalFormat decimalFormat()
    {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' '); // Set white space as grouping separator
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);

        return decimalFormat;
    }

    @Bean
    public AuditorAware<String> auditorProvider(IJwtService jwtService)
    {
        return new AuditorAwareImpl(jwtService);
    }
}
