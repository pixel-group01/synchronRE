package com.pixel.synchronre.reportmodule.controller;

import com.pixel.synchronre.reportmodule.config.JasperReportConfig;
import com.google.zxing.EncodeHintType;
import com.pixel.synchronre.reportmodule.service.IServiceReport;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller @RequestMapping(path = "/reports") @RequiredArgsConstructor
public class ReportController
{
    private final IServiceReport jrService;
    private final JasperReportConfig jrConfig;

    @GetMapping("/note-cession")
    public void generateNoteCession(HttpServletResponse response, @RequestParam Long affId, @RequestParam Long cesId) throws Exception
    {
        Map<String, Object> params = new HashMap<>();
        params.put("aff_id", affId);
        params.put("ces_id", cesId);
        params.put("param_image", jrConfig.imagesLocation);

        byte[] reportBytes = jrService.generateReport(jrConfig.noteCession, params);
        jrService.displayPdf(response, reportBytes, "Note-de-cession");
    }

    @GetMapping("/note-de-debit")
    public void generateNoteDebit(HttpServletResponse response, @RequestParam Long affId) throws Exception
    {
        Map<String, Object> params = new HashMap<>();
        params.put("aff_id", affId);
        params.put("param_image", jrConfig.imagesLocation);
        byte[] reportBytes = jrService.generateReport(jrConfig.noteDebit, params);
        jrService.displayPdf(response, reportBytes, "Note-de-debit");
    }

    @GetMapping("/note-de-credit")
    public void generateNoteCredit(HttpServletResponse response, @RequestParam Long affId, @RequestParam Long cesId) throws Exception
    {
        Map<String, Object> params = new HashMap<>();
        params.put("aff_id", affId);
        params.put("ces_id", cesId);
        params.put("param_image", jrConfig.imagesLocation);
        byte[] reportBytes = jrService.generateReport(jrConfig.noteCredit, params);
        jrService.displayPdf(response, reportBytes, "Note-de-credit");
    }
}
