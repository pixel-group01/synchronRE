package com.pixel.synchronre.reportmodule.service.interfac;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IReportExporter
{
    File exportReport(JasperPrint jasperPrint, String destinationFilePath) throws JRException;
    File exportReport(String reportSourcePath, List<Object> datas, Map<String, Object> parameters, String destinationFilePath) throws JRException;
    File exportReport(String reportSourcePath, Map<String, Object> parameters, String destinationFilePath) throws JRException, SQLException;
    File exportReport(String reportSourcePath, Map<String, Object> parameters) throws JRException, SQLException;
}
