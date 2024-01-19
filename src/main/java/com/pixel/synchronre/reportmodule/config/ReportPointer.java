package com.pixel.synchronre.reportmodule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReportPointer
{
    @Value("${server.port}")
    private String jasperProps;
    public static String xmlFileResolver = "server.port";
    public String setJasperTemplate()
    {
        System.setProperty(xmlFileResolver, String.valueOf(jasperProps));
        return String.valueOf(jasperProps);
    }
}
