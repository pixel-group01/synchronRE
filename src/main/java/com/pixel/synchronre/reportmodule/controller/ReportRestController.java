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
import com.pixel.synchronre.sychronremodule.model.entities.Reglement;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceInterlocuteur;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController @RequestMapping(path = "/reports") @RequiredArgsConstructor @ResponseStatus(HttpStatus.OK)
public class ReportRestController
{
    private final IServiceReport jrService;

    private final JasperReportConfig jrConfig;
    private final RepartitionRepository repRepo;
    private final AbstractDocumentService docService;
    private final IServiceInterlocuteur interService;

    @GetMapping("/note-cession-fac/{plaId}")
    public Base64FileDto generateNoteCession(@PathVariable Long plaId) throws Exception
    {
        byte[] reportBytes = jrService.generateNoteCessionFac(plaId);
        String base64Url = Base64ToFileConverter.convertBytesToBase64UrlString(reportBytes).replace("_", "/").replace("-", "+");
        Base64.getEncoder().encodeToString(reportBytes);
        return new Base64FileDto(Base64.getEncoder().encodeToString(reportBytes), reportBytes);
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

    @GetMapping("/note-cession-sinistre/{sinId}/{cesId}")
    public Base64FileDto generateNoteCessionSinistre(@PathVariable Long sinId, @PathVariable Long cesId) throws Exception
    {
        byte[] reportBytes = jrService.generateNoteCessionSinistre(sinId, cesId);
        String base64Url = Base64ToFileConverter.convertBytesToBase64UrlString(reportBytes).replace("_", "/").replace("-", "+");
        return new Base64FileDto(base64Url, reportBytes);
    }

    @GetMapping("/note-debit-sinistre/{sinId}")
    public Base64FileDto generateNoteDebitSinistre(@PathVariable Long sinId) throws Exception
    {
        byte[] reportBytes = jrService.generateNoteDebitSinistre(sinId);
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

    @GetMapping("/display-note-de-debit-sinistre/{sinId}")
    public void generateNoteDebitSinistre(HttpServletResponse response, @PathVariable Long sinId) throws Exception
    {
        byte[] reportBytes = jrService.generateNoteDebitSinistre(sinId);
        docService.displayPdf(response, reportBytes, "Note-de-debit");
    }

    @GetMapping("/display-note-de-cession-sinistre/{sinId}/{cesId}")
    public void generateNoteCessionSinistre(HttpServletResponse response, @PathVariable Long sinId, @PathVariable Long cesId) throws Exception
    {
        byte[] reportBytes = jrService.generateNoteCessionSinistre(sinId, cesId);
        docService.displayPdf(response, reportBytes, "Note-de-cession");
    }

    @GetMapping("/cheque-sinistre/{regId}")
    public Base64FileDto generateChequeSinistre(@PathVariable Long regId) throws Exception
    {
        byte[] reportBytes = jrService.generateChequeSinistre(regId);
        String base64Url = Base64ToFileConverter.convertBytesToBase64UrlString(reportBytes).replace("_", "/").replace("-", "+");
        return new Base64FileDto(base64Url, reportBytes);
    }

    @GetMapping("/compte-traites/{traitenpId}/{cedenteId}/{trancheId}/{periodicite}/{periode}")
    public Base64FileDto generateCompteTraite(@PathVariable Long traitenpId,
                                                @PathVariable Long cedenteId,
                                                @PathVariable Long trancheId,
                                                @PathVariable String periodicite,
                                                @PathVariable @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate periode
                                                ) throws Exception
    {
        byte[] reportBytes = jrService.generateCompteTraite(traitenpId,cedenteId,trancheId,periodicite,periode);
        String base64Url = Base64ToFileConverter.convertBytesToBase64UrlString(reportBytes).replace("_", "/").replace("-", "+");
        return new Base64FileDto(base64Url, reportBytes);
    }

    @GetMapping("/situation-note-debit-par-cedante-reassureur")
    public Base64FileDto generateSituationFinanciereCedRea(@RequestParam(required = false) Long exeCode,
                                                           @RequestParam(required = false) Long cedId,
                                                           @RequestParam(required = false) Long cesId,
                                                           @RequestParam(required = false) String statutEnvoi,
                                                           @RequestParam(required = false) String statutEncaissement) throws Exception
    {
        byte[] reportBytes = jrService.generateSituationFinanciereCedRea(exeCode,cedId,cesId,statutEnvoi,statutEncaissement);
        String base64Url = Base64ToFileConverter.convertBytesToBase64UrlString(reportBytes).replace("_", "/").replace("-", "+");
        return new Base64FileDto(base64Url, reportBytes);
    }

    @GetMapping("/situation-note-debit-par-cedante")
    public Base64FileDto generateSituationFinanciereCed(@RequestParam(required = false) Long exeCode,
                                                           @RequestParam(required = false) Long cedId,
                                                           @RequestParam(required = false) String statutEnvoie,
                                                           @RequestParam(required = false) String statutEncaissement) throws Exception
    {
        byte[] reportBytes = jrService.generateSituationFinanciereCed(exeCode,cedId,statutEnvoie,statutEncaissement);
        String base64Url = Base64ToFileConverter.convertBytesToBase64UrlString(reportBytes).replace("_", "/").replace("-", "+");
        return new Base64FileDto(base64Url, reportBytes);
    }

    @GetMapping("/situation-note-credit-par-cedante-reassureur")
    public Base64FileDto generateSituationNoteCredit(@RequestParam(required = false) Long exeCode,
                                                        @RequestParam(required = false) Long cedId,
                                                        @RequestParam(required = false) Long cesId) throws Exception
    {
        byte[] reportBytes = jrService.generateSituationNoteCredit(exeCode,cedId,cesId);
        String base64Url = Base64ToFileConverter.convertBytesToBase64UrlString(reportBytes).replace("_", "/").replace("-", "+");
        return new Base64FileDto(base64Url, reportBytes);
    }

    @GetMapping("/chiffre-affaires-par-periode-par-ced-rea")
    public Base64FileDto generateChiffreAffairesPeriodeCedRea(@RequestParam(required = false) Long exeCode,
                                                        @RequestParam(required = false) Long cedId,
                                                        @RequestParam(required = false) Long cesId,
                                                        @RequestParam(required = false) String dateDebut,
                                                        @RequestParam(required = false) String dateFin) throws Exception
    {
        byte[] reportBytes = jrService.generateChiffreAffairesPeriodeCedRea(exeCode,cedId,cesId,dateDebut,dateFin);
        String base64Url = Base64ToFileConverter.convertBytesToBase64UrlString(reportBytes).replace("_", "/").replace("-", "+");
        return new Base64FileDto(base64Url, reportBytes);
    }

}
