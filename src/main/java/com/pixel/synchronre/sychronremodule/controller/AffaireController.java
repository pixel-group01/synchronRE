package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.archivemodule.controller.service.AffaireDocUploader;
import com.pixel.synchronre.archivemodule.model.dtos.request.UploadDocReq;
import com.pixel.synchronre.authmodule.controller.services.spec.IHistoDetailsService;
import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.authmodule.model.entities.HistoDetails;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.constants.AffStatutGroup;
import com.pixel.synchronre.sychronremodule.model.constants.AffaireActions;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.CreateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.RenewFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.UpdateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.EtatComptableAffaire;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.FacultativeMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceMouvement;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceAffaire;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceExercie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.pixel.synchronre.sharedmodule.enums.StatutEnum.*;

@RestController
@RequestMapping("/affaires")
@RequiredArgsConstructor @ResponseStatus(HttpStatus.OK)
public class AffaireController
{
    private final AffaireRepository affRepo;
    private final FacultativeMapper facMapper;
    private final IJwtService jwtService;
    private final IServiceMouvement mvtService;
    private final IserviceAffaire affService;
    private final IserviceExercie exoService;
    private final AffaireDocUploader docService;
    private final IHistoDetailsService hds;

    @GetMapping("/facultative/details/{affId}")
    @ResponseStatus(HttpStatus.OK)
    public FacultativeDetailsResp getDetailsAffaire(@PathVariable Long affId){
        Affaire affaire = affRepo.findById(affId).orElseThrow(()->new AppException("Affaire introuvable"));
        return facMapper.mapToFacultativeDetailsResp(affaire);
    }

    @GetMapping("/facultative/test")
    public void test(){
    }

    @PostMapping("/facultative/create")
    @ResponseStatus(HttpStatus.CREATED)
    public FacultativeDetailsResp saveAffaire(@RequestBody @Valid CreateFacultativeReq dto){
        HistoDetails hd = hds.getHistoDetailsFromSecurityContext("Enregistrement d'une affaire");
        return affService.createFacultative(dto, hd);
    }

    @PostMapping("/facultative/delete/{affId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean deleteAffaire(@PathVariable Long affId)
    {
        return affService.deleteAffaire(affId);
    }

    @PutMapping("/facultative/update")
    @ResponseStatus(HttpStatus.CREATED)
    public FacultativeDetailsResp updateAffaire(@RequestBody @Valid UpdateFacultativeReq dto) throws UnknownHostException {
        HistoDetails hd = hds.getHistoDetailsFromSecurityContext("Modification d'une affaire");
        return affService.updateFacultative(dto, hd);
    }
    @PostMapping("/facultative/renew")
    @ResponseStatus(HttpStatus.CREATED)
    public FacultativeDetailsResp renewAffaire(@RequestBody @Valid RenewFacultativeReq dto) throws UnknownHostException {
        return affService.renewAffaire(dto);
    }
    //Tab all affaires : affiche toutes les affaires qui sont pas supprimées quelques soit l'acteur
    @GetMapping(path = "/facultative/all")
    public Page<FacultativeListResp> searchAllAffaires(@RequestParam(required = false) Long exeCode, @RequestParam(required = false) Long cedId,
                                                            @RequestParam(defaultValue = "") String key,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "false") boolean withMaxSize)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        cedId = cedId == null ? jwtService.getConnectedUserCedId() : cedId;

        Page<FacultativeListResp> facPages =withMaxSize ?
                new PageImpl<>( affRepo.searchAffaires(key, null, null,  cedId,  AffStatutGroup.tabAllAffaires, exeCode) ) :
                affRepo.searchAffaires(key, null, null,  cedId,  AffStatutGroup.tabAllAffaires, exeCode, PageRequest.of(page, size));
        //List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(affService.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        return facPages;
    }

    //Tab saisie par la cedante : affiche les affaires saisies par la cedante
    @GetMapping(path = "/facultative/by-sinistre")
    public Page<FacultativeListResp> searchAffaireBySinistre(@RequestParam(required = false) Long exeCode,
                                                            @RequestParam(defaultValue = "") String key,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10000") int size)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, null,  jwtService.getConnectedUserCedId(), AffStatutGroup.tabAffaireSinistre, exeCode, PageRequest.of(page, size));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(affService.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        return new PageImpl<>(facList, PageRequest.of(page, size), facPages.getTotalElements());
    }


    @GetMapping(path = "/facultative/by-user")
    public Page<FacultativeListResp> searchAffaireByUser(@RequestParam(required = false) Long exeCode,
                                                         @RequestParam(defaultValue = "") String key,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, jwtService.getConnectedUserId(), null, Arrays.asList(SAISIE.staCode, RETOURNE.staCode), exeCode, PageRequest.of(page, size));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(affService.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        return affService.searchAffaires(exeCode, key, Arrays.asList(SAISIE.staCode, RETOURNE.staCode), PageRequest.of(page, size));
    }

    @GetMapping(path = "/facultative/by-function")
    public Page<FacultativeListResp> searchAffaireByFnc(@RequestParam(required = false) Long exeCode,
                                                        @RequestParam(defaultValue = "") String key,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, jwtService.getConnectedUserFunctionId(), null, null,Arrays.asList(SAISIE.staCode, RETOURNE.staCode, EN_COURS_DE_REPARTITION.staCode), exeCode, PageRequest.of(page, size));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(affService.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        return new PageImpl<>(facList, PageRequest.of(page, size), facPages.getTotalElements());
    }

    //Tab saisie par la cedante : affiche les affaires saisies par la cedante
    @GetMapping(path = "/facultative/by-cedante")
    public Page<FacultativeListResp> searchAffaireByCedante(@RequestParam(required = false) Long exeCode,
                                                            @RequestParam(defaultValue = "") String key,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, null,  jwtService.getConnectedUserCedId(), AffStatutGroup.tabSaisieCed, exeCode, PageRequest.of(page, size));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(affService.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        return new PageImpl<>(facList, PageRequest.of(page, size), facPages.getTotalElements());
    }

    //Tab transmis et en cours de placement visible par la cedante :affiche les affaires transmises au souscripte
    @GetMapping(path = "/facultative/by-cedante-transmis") //Transmis par la cedante mais en cours de traitement
    public Page<FacultativeListResp> searchAffaireByCedanteTrans(@RequestParam(required = false) Long exeCode,
                                                                 @RequestParam(defaultValue = "") String key,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, null, jwtService.getConnectedUserCedId(), AffStatutGroup.tabEnCoursPla, exeCode, PageRequest.of(page, size));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(affService.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        return new PageImpl<>(facList, PageRequest.of(page, size), facPages.getTotalElements());
    }

    //Tab saisie par le souscripteur NEL-RE : affiche les affaires saisies par le souscripteur
    @GetMapping(path = "/facultative/by-reassureur-en-traitement") //Transmis par les cédantes et saisi par le réassureur
    public Page<FacultativeListResp> searchAffaireByReassureurEnTrai(@RequestParam(required = false) Long exeCode,
                                                                     @RequestParam(defaultValue = "") String key,
                                                         @RequestParam(required = false) Long cedId,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, null, cedId, AffStatutGroup.tabSaisieSous, exeCode, PageRequest.of(page, size));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(affService.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        return new PageImpl<>(facList, PageRequest.of(page, size), facPages.getTotalElements());
    }

    //Tab En cours de placement par le souscripteur NEL-RE : affiche les affaires en attentes de placement par le souscripteur et transmises par la cedante
    @GetMapping(path = "/facultative/by-reassureur-en-placement")
    public Page<FacultativeListResp> searchAffaireByReassureurEnPlacement(@RequestParam(required = false) Long exeCode,
                                                                     @RequestParam(defaultValue = "") String key,
                                                                     @RequestParam(required = false) Long cedId,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, null, cedId, AffStatutGroup.tabEnCoursPla, exeCode, PageRequest.of(page, size));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(affService.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        return new PageImpl<>(facList, PageRequest.of(page, size), facPages.getTotalElements());
    }
    @GetMapping(path = "/facultative/by-reassureur-valide") //validé par le réassureur
    public Page<FacultativeListResp> searchAffaireByReassureurValide(@RequestParam(required = false) Long exeCode,
                                                                     @RequestParam(defaultValue = "") String key,
                                                                     @RequestParam(required = false) Long cedId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, null,  cedId, Arrays.asList("VAL"), exeCode, PageRequest.of(page, size));

        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(affService.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        return new PageImpl<>(facList, PageRequest.of(page, size), facPages.getTotalElements());
    }

    //====================================

    @GetMapping(path = "/facultative/by-user-arch")
    public Page<FacultativeListResp> searchAffaireByUserAch(@RequestParam(required = false) Long exeCode,
                                                            @RequestParam(defaultValue = "") String key,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, jwtService.getConnectedUserId(), null, Arrays.asList(ARCHIVE.staCode), exeCode, PageRequest.of(page, size));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(affService.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        return new PageImpl<>(facList, PageRequest.of(page, size), facPages.getTotalElements());
    }

    @GetMapping(path = "/facultative/by-function-arch")
    public Page<FacultativeListResp> searchAffaireByFncArch(@RequestParam(required = false) Long exeCode,
                                                            @RequestParam(defaultValue = "") String key,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, jwtService.getConnectedUserFunctionId(), null, null,Arrays.asList(ARCHIVE.staCode), exeCode, PageRequest.of(page, size));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(affService.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        return new PageImpl<>(facList, PageRequest.of(page, size), facPages.getTotalElements());
    }

    //Tab archives : affiche les affaires archivées de la cedante
    @GetMapping(path = "/facultative/by-cedante-arch")
    public Page<FacultativeListResp> searchAffaireByCedanteArch(@RequestParam(required = false) Long exeCode,
                                                                 @RequestParam(defaultValue = "") String key,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, null, jwtService.getConnectedUserCedId(), AffStatutGroup.tabArchives, exeCode, PageRequest.of(page, size));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(affService.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        return new PageImpl<>(facList, PageRequest.of(page, size), facPages.getTotalElements());
    }

    //Tab en cours de reglement : affiche les affaires du souscripteur NE-LRE en cours de reglement
    @GetMapping(path = "/facultative/by-reassureur-en-reglement")
    public Page<FacultativeListResp> searchAffaireByCessionnaireEnReglement(@RequestParam(required = false) Long exeCode,
                                                                @RequestParam(defaultValue = "") String key,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, null, null, AffStatutGroup.tabEnCoursPaiement, exeCode, PageRequest.of(page, size));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(affService.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        return new PageImpl<>(facList, PageRequest.of(page, size), facPages.getTotalElements());
    }

    //Tab en cours de reglement : affiche les affaires de la cedante en cours de reglement
    @GetMapping(path = "/facultative/by-cedante-en-reglement")
    public Page<FacultativeListResp> searchAffaireByCedantEnReglement(@RequestParam(required = false) Long exeCode,
                                                                            @RequestParam(defaultValue = "") String key,
                                                                            @RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "10") int size)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, null, jwtService.getConnectedUserCedId(), AffStatutGroup.tabEnCoursPaiement, exeCode, PageRequest.of(page, size));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(affService.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        return new PageImpl<>(facList, PageRequest.of(page, size), facPages.getTotalElements());
    }

    @PutMapping(path = "/facultative/transmettre/{affId}")
    public Page<FacultativeListResp> transmettreAffaire(@PathVariable Long affId,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) throws UnknownHostException
    {
        return affService.transmettreAffaireAuSouscripteur(affId, PageRequest.of(page, size));
    }

    @PutMapping(path = "/facultative/retourner")
    public Page<FacultativeListResp> retournerAffaireALaCedante(@Valid @RequestBody MvtReq dto,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) throws UnknownHostException {
        return affService.retournerAffaireALaCedante(dto, PageRequest.of(page, size));
    }

    @PutMapping(path = "/facultative/valider/{affId}")
    public Page<FacultativeListResp> validerAffaire(@PathVariable Long affId,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) throws UnknownHostException
    {
        return affService.validerAffaire(affId, PageRequest.of(page, size));
    }

    @PutMapping(path = "/facultative/archiver/{affId}")
    public Page<FacultativeListResp> archiverAffaire(@PathVariable Long affId, @RequestParam(required = false) Long cedId,
                                                     @RequestParam(defaultValue = "") String key,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) throws UnknownHostException {
        mvtService.createMvtAffaire(new MvtReq(AffaireActions.ARCHIVER, affId, ARCHIVE.staCode,null));
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, null, cedId
                ,Arrays.asList(EN_COURS_DE_PAIEMENT.staCode), exoService.getExerciceCourant().getExeCode(), PageRequest.of(0, 10));

        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(affService.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        return new PageImpl<>(facList, PageRequest.of(page, size), facPages.getTotalElements());
    }

    @GetMapping(path = "/facultative/etat-comptable/{affId}")
    public EtatComptableAffaire getEtatComptable(@PathVariable Long affId)
    {
        return affService.getEtatComptable(affId);
    }

    @PutMapping(path = "/facultative/set-as-realisee/{affId}")
    public void setAsRealise(@PathVariable Long affId) throws UnknownHostException
    {
        affService.setAsRealisee(affId);
    }

    @PutMapping(path = "/facultative/set-as-non-realisee/{affId}")
    public void setAsNonRealise(@PathVariable Long affId) throws UnknownHostException
    {
        affService.setAsNonRealisee(affId);
    }

    @PostMapping(path = "/affaires/upload-doc/{docType}/{affId}")
    public void uploadDoc(@RequestParam(name = "file") MultipartFile file, @PathVariable String docType, @PathVariable Long affId) throws IOException
    {
        UploadDocReq dto = new UploadDocReq();
        dto.setDocNum(UUID.randomUUID().toString());
        dto.setDocUniqueCode(docType);
        dto.setFile(file);
        dto.setObjectId(affId);
        dto.setDocDescription(docType);
        docService.uploadDocument(dto);
    }

    @PutMapping(path = "/envoyer-note-debit-fac/{affId}")
    boolean envoyerNoteCession(@PathVariable Long affId) throws Exception {
        return affService.senNoteDebitFac(affId);
    }
}