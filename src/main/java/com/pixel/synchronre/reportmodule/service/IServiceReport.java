package com.pixel.synchronre.reportmodule.service;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IServiceReport
{
    byte[] generateReport(String reportName, Map<String, Object> params) throws Exception;

    void displayPdf(HttpServletResponse response, byte[] reportBytes, String displayName)  throws Exception;
}
