package com.pixel.synchronre.reportmodule.service;

import com.pixel.synchronre.reportmodule.config.JasperReportConfig;
import jakarta.servlet.http.HttpServletResponse;
import com.google.zxing.EncodeHintType;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.Map;

@Service @RequiredArgsConstructor
public class ServiceReportImpl implements IServiceReport
{
    private final JasperReportConfig jrConfig;
    private final DataSource dataSource;

    @Override
    public byte[] generateReport(String reportName, Map<String, Object> params) throws Exception {
        Connection conn = dataSource.getConnection();
        JasperReport jasperReport = JasperCompileManager.compileReport(jrConfig.reportLocation + "/" + reportName);
        // Remplissez le rapport Jasper en utilisant la connexion JDBC
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);

        // Exportez le rapport au format PDF
        byte[] reportBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        // Fermez la connexion
        conn.close();

        return reportBytes;
    }

    @Override
    public void displayPdf(HttpServletResponse response, byte[] reportBytes, String displayName)  throws Exception
    {
        // Configurez l'en-tête de la réponse HTTP
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "inline; filename=" + displayName +".pdf");
        response.setContentLength(reportBytes.length);

        // Écrivez le rapport Jasper dans le flux de sortie de la réponse HTTP
        OutputStream outStream = response.getOutputStream();
        outStream.write(reportBytes);
        outStream.flush();
        outStream.close();
    }
}
//JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);
