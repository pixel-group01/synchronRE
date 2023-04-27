package com.pixel.synchronre.reportmodule.controller;

import com.pixel.synchronre.reportmodule.config.JasperReportConfig;
import com.google.zxing.EncodeHintType;
import com.pixel.synchronre.reportmodule.service.IServiceReport;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller @RequestMapping(path = "/reports") @RequiredArgsConstructor
public class ReportController
{
    private final IServiceReport jrService;
    private final JasperReportConfig jrConfig;
    private final RepartitionRepository repRepo;

    @GetMapping("/note-cession/{plaId}")
    public void generateNoteCession(HttpServletResponse response, @PathVariable Long plaId) throws Exception
    {
        Repartition placement = repRepo.findById(plaId).orElseThrow(()-> new AppException("Placement introuvable"));
        if(!placement.getType().getUniqueCode().equals("REP_PLA")) throw new AppException("Cette repartition n'est pas un placement");
        Map<String, Object> params = new HashMap<>();
        params.put("aff_id", placement.getAffaire().getAffId());
        params.put("ces_id", placement.getCessionnaire().getCesId());
        params.put("param_image", jrConfig.imagesLocation);

        byte[] reportBytes = jrService.generateReport(jrConfig.noteCession, params);
        jrService.displayPdf(response, reportBytes, "Note-de-cession");
    }

    @GetMapping("/note-de-debit/{affId}")
    public void generateNoteDebit(HttpServletResponse response, @PathVariable Long affId) throws Exception
    {
        Map<String, Object> params = new HashMap<>();
        params.put("aff_id", affId);
        params.put("param_image", jrConfig.imagesLocation);
        byte[] reportBytes = jrService.generateReport(jrConfig.noteDebit, params);
        jrService.displayPdf(response, reportBytes, "Note-de-debit");
    }

    @GetMapping("/note-de-credit/{plaId}")
    public void generateNoteCredit(HttpServletResponse response, @PathVariable Long plaId) throws Exception
    {
        Repartition placement = repRepo.findById(plaId).orElseThrow(()-> new AppException("Placement introuvable"));
        if(!placement.getType().getUniqueCode().equals("REP_PLA")) throw new AppException("Cette repartition n'est pas un placement");
        Map<String, Object> params = new HashMap<>();
        params.put("aff_id", placement.getAffaire().getAffId());
        params.put("ces_id", placement.getCessionnaire().getCesId());
        params.put("param_image", jrConfig.imagesLocation);
        byte[] reportBytes = jrService.generateReport(jrConfig.noteCredit, params);
        jrService.displayPdf(response, reportBytes, "Note-de-credit");
    }
}
