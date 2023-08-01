package com.pixel.synchronre.reportmodule.controller;

import com.pixel.synchronre.archivemodule.controller.service.AbstractDocumentService;
import com.pixel.synchronre.archivemodule.model.dtos.response.Base64FileDto;
import com.pixel.synchronre.reportmodule.config.JasperReportConfig;
import com.pixel.synchronre.reportmodule.service.IServiceReport;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.Base64ToFileConverter;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.ReglementRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController @RequestMapping(path = "/reports") @RequiredArgsConstructor @ResponseStatus(HttpStatus.OK)
public class ReportRestController
{
    private final IServiceReport jrService;

    private final JasperReportConfig jrConfig;
    private final RepartitionRepository repRepo;
    private final AbstractDocumentService docService;

    @GetMapping("/note-cession-fac/{plaId}")
    public Base64FileDto generateNoteCession(@PathVariable Long plaId) throws Exception
    {
        byte[] reportBytes = jrService.generateNoteCessionFac(plaId);
        String base64Url = Base64ToFileConverter.convertBytesToBase64UrlString(reportBytes).replace("_", "/").replace("-", "+");
        return new Base64FileDto(base64Url, reportBytes);
    }

    @GetMapping("/note-de-debit-fac/{affId}")
    public Base64FileDto generateNoteDebit(@PathVariable Long affId) throws Exception
    {

        byte[] reportBytes = jrService.generateNoteDebitFac(affId);
        String base64Url = Base64ToFileConverter.convertBytesToBase64UrlString(reportBytes).replace("_", "/").replace("-", "+");
        return new Base64FileDto(base64Url, reportBytes);
    }

    @GetMapping("/note-de-credit-fac/{affId}/{cesId}")
    public Base64FileDto generateNoteCredit(@PathVariable Long affId, @PathVariable Long cesId) throws Exception
    {
        byte[] reportBytes = jrService.generateNoteCreditFac(affId, cesId);
        String base64Url = Base64ToFileConverter.convertBytesToBase64UrlString(reportBytes).replace("_", "/").replace("-", "+");
        return new Base64FileDto(base64Url, reportBytes);
    }

    @GetMapping("/note-cession-sinistre/{plaId}")
    public Base64FileDto generateNoteCessionSinistre(@PathVariable Long plaId) throws Exception
    {
        byte[] reportBytes = jrService.generateNoteCessionSinistre(plaId);
        String base64Url = Base64ToFileConverter.convertBytesToBase64UrlString(reportBytes).replace("_", "/").replace("-", "+");
        return new Base64FileDto(base64Url, reportBytes);
    }

    @GetMapping("/note-debit-sinistre/{affId}")
    public Base64FileDto generateNoteDebitSinistre(@PathVariable Long affId) throws Exception
    {
        byte[] reportBytes = jrService.generateNoteDebitSinistre(affId);
        String base64Url = Base64ToFileConverter.convertBytesToBase64UrlString(reportBytes).replace("_", "/").replace("-", "+");
        return new Base64FileDto(base64Url, reportBytes);
    }

    @GetMapping("/cheque/{regId}")
    public Base64FileDto generateCheque(@PathVariable Long regId) throws Exception
    {
        byte[] reportBytes = jrService.generateCheque(regId);
        String base64Url = Base64ToFileConverter.convertBytesToBase64UrlString(reportBytes).replace("_", "/").replace("-", "+");
        return new Base64FileDto(base64Url, reportBytes);
    }

    @GetMapping("/display-note-de-debit-fac/{affId}")
    public void generateNoteDebit(HttpServletResponse response, @PathVariable Long affId) throws Exception
    {
        byte[] reportBytes = jrService.generateNoteDebitFac(affId);
        docService.displayPdf(response, reportBytes, "Note-de-debit");
    }

    @GetMapping("/display-note-de-cession-fac/{plaId}")
    public void generateNoteCession(HttpServletResponse response, @PathVariable Long plaId) throws Exception
    {
        byte[] reportBytes = jrService.generateNoteCessionFac(plaId);
        docService.displayPdf(response, reportBytes, "Note-de-cession");
    }

    @GetMapping("/display-note-de-credit-fac/{affId}/{cesId}")
    public void generateNoteCredit(HttpServletResponse response, @PathVariable Long affId, @PathVariable Long cesId) throws Exception
    {
        byte[] reportBytes = jrService.generateNoteCreditFac(affId, cesId);
        docService.displayPdf(response, reportBytes, "Note-de-credit");
    }
}