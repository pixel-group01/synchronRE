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
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Reglement;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
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
    private final AffaireRepository affRepo;
    private final ReglementRepository regRepo;
    private final AbstractDocumentService docService;

    @GetMapping("/note-cession/{plaId}")
    public Base64FileDto generateNoteCession(HttpServletResponse response, @PathVariable Long plaId) throws Exception
    {
        Repartition placement = repRepo.findById(plaId).orElseThrow(()-> new AppException("Placement introuvable"));
        if(!placement.getType().getUniqueCode().equals("REP_PLA")) throw new AppException("Cette repartition n'est pas un placement");
        Map<String, Object> params = new HashMap<>();
        params.put("aff_id", placement.getAffaire().getAffId());
        params.put("aff_assure", placement.getAffaire().getAffAssure());
        params.put("fac_numero_police", placement.getAffaire().getFacNumeroPolice());
        params.put("ces_id", placement.getCessionnaire().getCesId());
        params.put("param_image", jrConfig.imagesLocation);
        if (placement.getRepStaCode().getStaCode().equals("VAL") || placement.getRepStaCode().getStaCode().equals("MAIL")){
            params.put("param_visible", "true");
        }
        else{
            params.put("param_visible", "false");
        }
        byte[] reportBytes = jrService.generateReport(jrConfig.noteCession, params, new ArrayList<>(), null);
        String base64Url = Base64ToFileConverter.convertBytesToBase64UrlString(reportBytes);
        return new Base64FileDto(base64Url);
    }

    @GetMapping("/note-de-debit/{affId}")
    public Base64FileDto generateNoteDebit(HttpServletResponse response, @PathVariable Long affId) throws Exception
    {
        Repartition repart = repRepo.repartFindByAffaire(affId).orElseThrow(()-> new AppException("Affaire introuvable"));

        Map<String, Object> params = new HashMap<>();
        params.put("aff_id", repart.getAffaire().getAffId());
        params.put("aff_assure", repart.getAffaire().getAffAssure());
        params.put("fac_numero_police", repart.getAffaire().getFacNumeroPolice());
        params.put("param_image", jrConfig.imagesLocation);

        //Affichage du cachet en fonction d'une expression dans l'etat
        if (repart.getRepStaCode().getStaCode().equals("VAL") || repart.getRepStaCode().getStaCode().equals("MAIL")){
            params.put("param_visible", "true");
        }else{
            params.put("param_visible", "false");
        }

        byte[] reportBytes = jrService.generateReport(jrConfig.noteDebit, params, new ArrayList<>(), null);
        String base64Url = Base64ToFileConverter.convertBytesToBase64UrlString(reportBytes);
        return new Base64FileDto(base64Url);
    }

    @GetMapping("/note-de-credit/{affId}/{cesId}")
    public Base64FileDto generateNoteCredit(HttpServletResponse response, @PathVariable Long affId, @PathVariable Long cesId) throws Exception
    {
        Repartition placement = repRepo.getPlacementByAffIdAndCesId(affId,cesId).orElseThrow(()-> new AppException("Placement introuvable"));
        if(!placement.getType().getUniqueCode().equals("REP_PLA")) throw new AppException("Cette repartition n'est pas un placement");
        Map<String, Object> params = new HashMap<>();
        params.put("aff_id", placement.getAffaire().getAffId());
        params.put("aff_assure", placement.getAffaire().getAffAssure());
        params.put("fac_numero_police", placement.getAffaire().getFacNumeroPolice());
        params.put("ces_id", placement.getCessionnaire().getCesId());
        params.put("param_image", jrConfig.imagesLocation);
        byte[] reportBytes = jrService.generateReport(jrConfig.noteCredit, params, new ArrayList<>(), null);
        String base64Url = Base64ToFileConverter.convertBytesToBase64UrlString(reportBytes);
        return new Base64FileDto(base64Url);
    }

    @GetMapping("/note-cession-sinistre/{plaId}")
    public Base64FileDto generateNoteCessionSinistre(HttpServletResponse response, @PathVariable Long plaId) throws Exception
    {
        Repartition placement = repRepo.findById(plaId).orElseThrow(()-> new AppException("Placement introuvable"));
        if(!placement.getType().getUniqueCode().equals("REP_PLA")) throw new AppException("Cette repartition n'est pas un placement");
        Map<String, Object> params = new HashMap<>();
        params.put("aff_id", placement.getAffaire().getAffId());
        params.put("aff_assure", placement.getAffaire().getAffAssure());
        params.put("fac_numero_police", placement.getAffaire().getFacNumeroPolice());
        params.put("ces_id", placement.getCessionnaire().getCesId());
        params.put("param_image", jrConfig.imagesLocation);

        //Affichage du cachet en fonction d'une expression dans l'etat
        if (placement.getRepStaCode().getStaCode().equals("VAL") || placement.getRepStaCode().getStaCode().equals("MAIL")){
            params.put("param_visible", "true");
        }else{
            params.put("param_visible", "false");
        }
        byte[] reportBytes = jrService.generateReport(jrConfig.noteCessionSinistre, params, new ArrayList<>(), null);
        String base64Url = Base64ToFileConverter.convertBytesToBase64UrlString(reportBytes);
        return new Base64FileDto(base64Url);
    }

    @GetMapping("/note-debit-sinistre/{affId}")
    public Base64FileDto generateNoteDebitSinistre(HttpServletResponse response, @PathVariable Long affId) throws Exception
    {
        Affaire affaire = affRepo.findById(affId).orElseThrow(()-> new AppException("Affaire introuvable"));
        Map<String, Object> params = new HashMap<>();
        params.put("aff_id", affaire.getAffId());
        params.put("aff_assure", affaire.getAffAssure());
        params.put("fac_numero_police", affaire.getFacNumeroPolice());
        params.put("param_image", jrConfig.imagesLocation);
        byte[] reportBytes = jrService.generateReport(jrConfig.noteDebitSinistre, params, new ArrayList<>(), null);
        String base64Url = Base64ToFileConverter.convertBytesToBase64UrlString(reportBytes);
        return new Base64FileDto(base64Url);
    }

    @GetMapping("/cheque/{regId}")
    public Base64FileDto generateCheque(HttpServletResponse response, @PathVariable Long regId) throws Exception
    {
        Reglement reglement =  regRepo.findById(regId).orElseThrow(()-> new AppException(("Règlement introuvable")));
        Map<String, Object> params = new HashMap<>();
        params.put("reg_id", reglement.getRegId());
        byte[] reportBytes = jrService.generateReport(jrConfig.cheque, params, new ArrayList<>(), null);
        String base64Url = Base64ToFileConverter.convertBytesToBase64UrlString(reportBytes);
        return new Base64FileDto(base64Url);
    }
}