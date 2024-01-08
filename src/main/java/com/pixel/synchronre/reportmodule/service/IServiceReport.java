package com.pixel.synchronre.reportmodule.service;

import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

public interface IServiceReport
{
    byte[] generateReport(String reportName, Map<String, Object> params, List<Object> data, String qrText) throws Exception;

    byte[] generateNoteCessionFac(Long plaId, String interlocuteur) throws Exception;

    byte[] generateNoteDebitFac(Long affId) throws Exception;

    byte[] generateNoteCreditFac(Long affId, @PathVariable Long cesId) throws Exception;

    byte[] generateNoteCessionSinistre(Long sinId, Long cesId) throws Exception;
    byte[] generateNoteCessionSinistre(Long sinId, Long cesId, String interlocuteur) throws Exception;

    byte[] generateNoteDebitSinistre(Long sinId) throws Exception;

    byte[] generateCheque(Long regId) throws Exception;

    byte[] generateChequeSinistre(Long regId) throws Exception;

    byte[] generateNoteCessionFac(Long plaId) throws Exception;
}
