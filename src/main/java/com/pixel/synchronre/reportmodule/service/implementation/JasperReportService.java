package com.pixel.synchronre.reportmodule.service.implementation;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;

@Service @RequiredArgsConstructor
public class JasperReportService
{
    private final JasperReport jasperReport;
    private final DataSource dataSource;

    public byte[] generateReport(Map<String, Object> params) throws Exception {
        Connection conn = dataSource.getConnection();

        // Remplissez le rapport Jasper en utilisant la connexion JDBC
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);

        // Exportez le rapport au format PDF
        byte[] reportBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        // Fermez la connexion
        conn.close();

        return reportBytes;
    }
}
