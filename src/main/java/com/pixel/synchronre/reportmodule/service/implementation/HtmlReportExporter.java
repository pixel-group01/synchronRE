package com.pixel.synchronre.reportmodule.service.implementation;

import com.pixel.synchronre.reportmodule.service.interfac.IReportExporter;
import com.pixel.synchronre.reportmodule.service.interfac.IReportPrinter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service("htmlReporter")
public class HtmlReportExporter implements IReportExporter
{
    private final IReportPrinter reportPrinter;

    public HtmlReportExporter(IReportPrinter reportPrinter)
    {
        this.reportPrinter = reportPrinter;
    }

    @Override
    public File exportReport(JasperPrint jasperPrint, String destinationFilePath) throws JRException
    {
        JasperExportManager.exportReportToHtmlFile(jasperPrint, destinationFilePath);
        return new File(destinationFilePath);
    }

    @Override
    public File exportReport(String reportSourcePath, List<Object> datas, Map<String, Object> parameters, String destinationFilePath) throws JRException
    {
        JasperPrint jasperPrint = reportPrinter.printReport(reportSourcePath, datas,parameters);
        return exportReport(jasperPrint,destinationFilePath);
    }

    @Override
    public File exportReport(String reportSourcePath, Map<String, Object> parameters, String destinationFilePath) throws JRException, SQLException {
        JasperPrint jasperPrint = reportPrinter.printReport(reportSourcePath, parameters);
        return exportReport(jasperPrint,destinationFilePath);
    }

    @Override
    public File exportReport(String reportSourcePath, Map<String, Object> parameters) throws JRException, SQLException {
        JasperPrint jasperPrint = reportPrinter.printReport(reportSourcePath, parameters);
        return this.exportReport(jasperPrint, "report/listPersonnel.html");
    }
}
