package com.pixel.synchronre.reportmodule.service;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IServiceReport
{
    byte[] generateReport(String reportName, Map<String, Object> params, List<Object> data, String qrText) throws Exception;

    byte[] generateNoteCession(Long plaId) throws Exception;

    byte[] generateNoteDebit(Long affId) throws Exception;

    byte[] generateNoteCredit(Long affId, @PathVariable Long cesId) throws Exception;

    byte[] generateNoteCessionSinistre(Long plaId) throws Exception;

    byte[] generateNoteDebitSinistre(Long affId) throws Exception;

    byte[] generateCheque(Long regId) throws Exception;
}
