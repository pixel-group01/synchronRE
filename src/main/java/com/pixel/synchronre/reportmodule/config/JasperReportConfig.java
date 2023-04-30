package com.pixel.synchronre.reportmodule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasperReportConfig {

    @Value("${report.location}")
    public String reportLocation;

    @Value("${report.location.images}")
    public String imagesLocation;

    @Value("${report.location.note.cession}")
    public String noteCession;

    @Value("${report.location.note.debit}")
    public String noteDebit;

    @Value("${report.location.note.credit}")
    public String noteCredit;

}
