package com.pixel.synchronre.reportmodule.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.pixel.synchronre.reportmodule.config.JasperReportConfig;
import jakarta.servlet.http.HttpServletResponse;
import com.google.zxing.EncodeHintType;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service @RequiredArgsConstructor
public class ServiceReportImpl implements IServiceReport
{
    private final JasperReportConfig jrConfig;
    private final DataSource dataSource;

    private void setQrCodeParam(Map<String, Object> parameters, String qrText) throws Exception
    {
        //String qrText = "Application SynchronRE : Votre Demande de placement porte sur N° Affaire : " + parameters.get("aff_id") + " Assuré : " + parameters.get("aff_assure") + " Numéro de Police : " + parameters.get("fac_numero_police");
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix;
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, 200, 200, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, "png", stream);
            parameters.put("qrCode", new ByteArrayInputStream(stream.toByteArray()));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] generateReport(String reportName, Map<String, Object> parameters, List<Object> data, String qrText) throws Exception
    {
        qrText =  qrText != null ? qrText : "Application SynchronRE : Votre Demande de placement porte sur N° Affaire : " + parameters.get("aff_id") + " Assuré : " + parameters.get("aff_assure") + " Numéro de Police : " + parameters.get("fac_numero_police");
        // Génération du code QR
        this.setQrCodeParam(parameters, qrText);

        JasperReport jasperReport = JasperCompileManager.compileReport(jrConfig.reportLocation + "/" + reportName);
        // Remplissez le rapport Jasper en utilisant la connexion JDBC
        Connection connection = dataSource.getConnection();
        JRBeanCollectionDataSource jrBeanCollectionDataSource = data == null || data.isEmpty() ? null : new JRBeanCollectionDataSource(data);

        JasperPrint jasperPrint = jrBeanCollectionDataSource == null
                ? JasperFillManager.fillReport(jasperReport, parameters, connection)
                : JasperFillManager.fillReport(jasperReport, parameters, jrBeanCollectionDataSource);

        // Exportez le rapport au format PDF
        byte[] reportBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        // Fermez la connexion
        connection.close();

        return reportBytes;
    }
}
