package com.pixel.synchronre.config;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

@Configuration
public class BeansConfig
{
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
