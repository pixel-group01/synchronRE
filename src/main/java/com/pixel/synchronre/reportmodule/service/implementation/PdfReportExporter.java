package com.pixel.synchronre.reportmodule.service.implementation;

import com.pixel.synchronre.reportmodule.service.interfac.IReportExporter;
import com.pixel.synchronre.reportmodule.service.interfac.IReportPrinter;
import net.sf.jasperreports.engine.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service("pdfReporter")
@Primary
public class PdfReportExporter implements IReportExporter
{
    private final IReportPrinter reportPrinter;

    public PdfReportExporter(IReportPrinter reportPrinter)
    {
        this.reportPrinter = reportPrinter;
    }

    @Override
    public File exportReport(JasperPrint jasperPrint, String destinationFilePath) throws JRException
    {
        JasperExportManager.exportReportToPdfFile(jasperPrint, destinationFilePath);
        //JasperExportManager.exportReportToPdfFile()
        return new File(destinationFilePath);
    }

    @Override
    public File exportReport(String reportSourcePath, List<Object> datas, Map<String, Object> parameters, String destinationFilePath) throws JRException
    {
        JasperPrint jasperPrint = reportPrinter.printReport(reportSourcePath, datas,parameters);
        return this.exportReport(jasperPrint,destinationFilePath);
    }

    @Override
    public File exportReport(String reportSourcePath, Map<String, Object> parameters, String destinationFilePath) throws JRException, SQLException {
        JasperPrint jasperPrint = reportPrinter.printReport(reportSourcePath,parameters);
        return this.exportReport(jasperPrint,destinationFilePath);
    }

    @Override
    public File exportReport(String reportSourcePath, Map<String, Object> parameters) throws JRException, SQLException
    {
        JasperPrint jasperPrint = reportPrinter.printReport(reportSourcePath,parameters);
        return this.exportReport(jasperPrint, ".pdf");
    }
}
