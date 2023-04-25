package com.pixel.synchronre.reportmodule.service.implementation;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasperReportConfig {

    @Value("${jasper.report.location}")
    private String reportLocation;

    @Bean
    public JasperReport jasperReport() throws Exception {
        String reportPath = reportLocation + "/Note_Cession.jrxml";
        JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);
        return jasperReport;
    }
}
