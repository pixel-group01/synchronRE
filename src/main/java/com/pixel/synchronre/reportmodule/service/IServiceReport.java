package com.pixel.synchronre.reportmodule.service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IServiceReport
{
    byte[] generateReport(String reportName, Map<String, Object> params, List<Object> data, String qrText) throws Exception;

    byte[] generateReportExcel(String reportName, Map<String, Object> params, List<Object> data, String qrText) throws Exception;
    public byte[] exportReportToExcels(JasperPrint jasperPrint) throws JRException;

    byte[] generateNoteCessionFac(Long plaId, String interlocuteur) throws Exception;

    byte[] generateNoteDebitFac(Long affId) throws Exception;

    byte[] generateNoteCreditFac(Long affId, @PathVariable Long cesId) throws Exception;

    byte[] generateNoteCessionSinistre(Long sinId, Long cesId) throws Exception;
    byte[] generateNoteCessionSinistre(Long sinId, Long cesId, String interlocuteur) throws Exception;

    byte[] generateNoteDebitSinistre(Long sinId) throws Exception;

    byte[] generateCheque(Long regId) throws Exception;

    byte[] generateChequeSinistre(Long regId) throws Exception;

    byte[] generateNoteCessionFac(Long plaId) throws Exception;

    byte[] generateCompteTraite(Long traitenpId, Long cedenteId, Long trancheId, String periodicite, Long periodeId) throws Exception;
    byte[] generateSituationFinanciereCedRea(Long exeCode, Long cedId, Long cesId, String statutEnvoie, String statutEncaissement) throws Exception;

    byte[] generateSituationFinanciereCed(Long exeCode, Long cedId, String statutEnvoie, String statutEncaissement) throws Exception;
    byte[] exportSituationFinanciereCed(Long exeCode, Long cedId, String statutEnvoie, String statutEncaissement) throws Exception;

    byte[] generateSituationNoteCredit(Long exeCode, Long cedId, Long cesId) throws Exception;

    byte[] generateChiffreAffairesPeriodeCedRea(Long exeCode, Long cedId, Long cesId, String dateDebut, String dateFin) throws Exception;

    byte[] exportSituationFinanciereCedRea(Long exeCode, Long cedId, Long cesId, String statutEnvoie, String statutEncaissement) throws Exception;

    byte[] exportSituationNoteCredit(Long exeCode, Long cedId, Long cesId) throws Exception;
}
