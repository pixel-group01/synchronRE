package com.pixel.synchronre.config;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor @Component
public class AuditorAwareImpl implements AuditorAware<String>
{
    private final IJwtService jwtService;
    @Override
    public Optional<String> getCurrentAuditor()
    {
        String extractedAuditor = jwtService != null ? jwtService.extractUsername() : "UNKNOWN";
        return Optional.of(extractedAuditor==null || extractedAuditor.equals("")? "UNKNOWN": extractedAuditor );
    }
}