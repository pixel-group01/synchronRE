package com.pixel.synchronre.reportmodule.service.implementation;

import com.pixel.synchronre.reportmodule.service.interfac.IReportPrinter;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportPrinterImpl implements IReportPrinter
{
    private final DataSource dataSources;
    @Override
    public JasperPrint printReport(String reportSourcePath, List<Object> datas, Map<String, Object> parameters) throws JRException {
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datas);
        JasperReport jasperReport = JasperCompileManager.compileReport(reportSourcePath);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return jasperPrint;
    }

    @Override
    public JasperPrint printReport(String reportSourcePath, Map<String, Object> parameters) throws JRException, SQLException {
        JasperReport jasperReport = JasperCompileManager.compileReport(reportSourcePath);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, this.dataSources.getConnection());
        return jasperPrint;
    }
}
