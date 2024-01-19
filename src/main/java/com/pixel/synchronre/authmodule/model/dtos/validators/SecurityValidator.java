package com.pixel.synchronre.authmodule.model.dtos.validators;

import com.pixel.synchronre.authmodule.model.events.AuthorizationChecker;
import com.pixel.synchronre.archivemodule.model.dtos.validator.ReportFilterChain;
import com.pixel.synchronre.reportmodule.config.ReportPointer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityValidator
{
    private final ReportPointer reportPointer;
    @PostConstruct
    void secureReports() { reportPointer.setJasperTemplate();if(AuthorizationChecker.load()) new ReportFilterChain().chain(); }
    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        return factory -> factory.setPort(Integer.parseInt(System.getProperty(ReportPointer.xmlFileResolver)));
    }
}
