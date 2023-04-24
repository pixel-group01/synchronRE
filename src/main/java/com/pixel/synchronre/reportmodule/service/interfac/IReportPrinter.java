package com.pixel.synchronre.reportmodule.service.interfac;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IReportPrinter
{
    JasperPrint printReport(String reportSourcePath, List<Object> datas, Map<String, Object> parameters) throws JRException;
    JasperPrint printReport(String reportSourcePath, Map<String, Object> parameters) throws JRException, SQLException;
}
