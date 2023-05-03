package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.constants.AffStatutGroup;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.CreateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.UpdateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.EtatComptableAffaire;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.FacultativeMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.service.interfac.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.LifecycleState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.pixel.synchronre.sharedmodule.enums.StatutEnum.*;

@RestController
@RequestMapping("/affaires")
@RequiredArgsConstructor
public class AffaireController
{
    private final IserviceFacultative facService;
    private final AffaireRepository affRepo;
    private final FacultativeMapper facMapper;
    private final IJwtService jwtService;
    private final IServiceMouvement mvtService;
    private final IserviceAffaire affService;
    private final IserviceExercie exoService;
    private final IServiceCalculsComptables comptaAffaireService;

    @GetMapping("/facultative/details/{affId}")
    @ResponseStatus(HttpStatus.OK)
    public FacultativeDetailsResp getDetailsAffaire(@PathVariable Long affId) throws MethodArgumentNotValidException, UnknownHostException {
        Affaire affaire = affRepo.findById(affId).orElseThrow(()->new AppException("Affaire introuvable"));
        return facMapper.mapToFacultativeDetailsResp(affaire);
    }

    @PostMapping("/facultative/create")
    @ResponseStatus(HttpStatus.CREATED)
    public FacultativeDetailsResp saveAffaire(@RequestBody @Valid CreateFacultativeReq dto) throws MethodArgumentNotValidException, UnknownHostException {
        return facService.createFacultative(dto);
    }

    @PutMapping("/facultative/update")
    @ResponseStatus(HttpStatus.CREATED)
    public FacultativeDetailsResp saveAffaire(@RequestBody @Valid UpdateFacultativeReq dto) throws MethodArgumentNotValidException, UnknownHostException {
        return facService.updateFacultative(dto);
    }

    @GetMapping(path = "/facultative/by-user")
    public Page<FacultativeListResp> searchAffaireByUser(@RequestParam(required = false) Long exeCode,
                                                         @RequestParam(defaultValue = "") String key,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, jwtService.getConnectedUserId(), null, null, Arrays.asList(SAISIE.staCode, RETOURNE.staCode), exeCode, PageRequest.of(page, size));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(this.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        return new PageImpl<>(facList, PageRequest.of(page, size), facPages.getTotalElements());
    }

    @GetMapping(path = "/facultative/by-function")
    public Page<FacultativeListResp> searchAffaireByFnc(@RequestParam(required = false) Long exeCode,
                                                        @RequestParam(defaultValue = "") String key,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, jwtService.getConnectedUserFunctionId(), null, null,null, Arrays.asList(SAISIE.staCode, RETOURNE.staCode, EN_COURS_DE_REPARTITION.staCode), exeCode, PageRequest.of(page, size));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(this.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
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
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, null,  jwtService.getConnectedUserCedId(), null, AffStatutGroup.tabSaisieCed, exeCode, PageRequest.of(page, size));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(this.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
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
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, null, jwtService.getConnectedUserCedId(), null, AffStatutGroup.tabEnCoursPla, exeCode, PageRequest.of(page, size));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(this.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
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
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, null, cedId, jwtService.getConnectedUserCesId(),AffStatutGroup.tabSaisieSous, exeCode, PageRequest.of(page, size));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(this.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        return new PageImpl<>(facList, PageRequest.of(page, size), facPages.getTotalElements());
    }

    private boolean placementIsFinished(Long affId)
    {
        BigDecimal besFac = this.comptaAffaireService.calculateRestARepartir(affId);
        besFac = besFac == null ? BigDecimal.ZERO : besFac;
        return besFac.compareTo(BigDecimal.ZERO) == 0;
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
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, null, cedId, jwtService.getConnectedUserCesId(),AffStatutGroup.tabEnCoursPla, exeCode, PageRequest.of(page, size));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(this.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
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
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, null,  cedId, jwtService.getConnectedUserCesId(),Arrays.asList("VAL"), exeCode, PageRequest.of(page, size));

        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(this.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
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
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, jwtService.getConnectedUserId(), null, null, Arrays.asList(ARCHIVE.staCode), exeCode, PageRequest.of(page, size));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(this.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        return new PageImpl<>(facList, PageRequest.of(page, size), facPages.getTotalElements());
    }

    @GetMapping(path = "/facultative/by-function-arch")
    public Page<FacultativeListResp> searchAffaireByFncArch(@RequestParam(required = false) Long exeCode,
                                                            @RequestParam(defaultValue = "") String key,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, jwtService.getConnectedUserFunctionId(), null, null,null, Arrays.asList(ARCHIVE.staCode), exeCode, PageRequest.of(page, size));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(this.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
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
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, null, jwtService.getConnectedUserCedId(),  null,AffStatutGroup.tabArchives, exeCode, PageRequest.of(page, size));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(this.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
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
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, null, null,  jwtService.getConnectedUserCesId(), AffStatutGroup.tabEnCoursReg, exeCode, PageRequest.of(page, size));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(this.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
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
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, null, jwtService.getConnectedUserCedId(),  null, AffStatutGroup.tabEnCoursReg, exeCode, PageRequest.of(page, size));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(this.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        return new PageImpl<>(facList, PageRequest.of(page, size), facPages.getTotalElements());
    }

    @PostMapping(path = "/facultative/transmettre/{affId}")
    public Page<FacultativeListResp> transmettreAffaire(@PathVariable Long affId,
                                                        @RequestParam(defaultValue = "") String key,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size)
    {
        mvtService.createMvtAffaire(new MvtReq(affId, EN_ATTENTE_DE_PLACEMENT.staCode, null));
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, null,
                jwtService.getConnectedUserCedId(),
                null, Arrays.asList(SAISIE.staCode, RETOURNE.staCode, EN_COURS_DE_REPARTITION.staCode), exoService.getExerciceCourant().getExeCode(), PageRequest.of(page, size));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(this.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        return new PageImpl<>(facList, PageRequest.of(page, size), facPages.getTotalElements());
    }

    @PostMapping(path = "/facultative/retourner")
    public Page<FacultativeListResp> retournerAffaire(@Valid @RequestBody MvtReq dto,
                                                      @RequestParam(defaultValue = "") String key,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size)
    {
        mvtService.createMvtAffaire(new MvtReq(dto.getObjectId(), RETOURNE.staCode, dto.getMvtObservation()));
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, null,
                affRepo.getAffCedId(dto.getObjectId()),
                null, Arrays.asList(EN_ATTENTE_DE_PLACEMENT.staCode, EN_COURS_DE_PLACEMENT.staCode), exoService.getExerciceCourant().getExeCode(), PageRequest.of(0, 10));

        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(this.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        return new PageImpl<>(facList, PageRequest.of(page, size), facPages.getTotalElements());
    }

    @PostMapping(path = "/facultative/valider/{affId}")
    public Page<FacultativeListResp> validerAffaire(@PathVariable Long affId, @RequestParam(required = false) Long cedId,
                                                    @RequestParam(defaultValue = "") String key,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size)
    {
        mvtService.createMvtAffaire(new MvtReq(affId, EN_COURS_DE_REGLEMENT.staCode,null));
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, null, cedId,
                jwtService.getConnectedUserCesId(), Arrays.asList(EN_ATTENTE_DE_PLACEMENT.staCode, EN_COURS_DE_PLACEMENT.staCode), exoService.getExerciceCourant().getExeCode(), PageRequest.of(0, 10));
        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(this.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
        return new PageImpl<>(facList, PageRequest.of(page, size), facPages.getTotalElements());
    }

    @PostMapping(path = "/facultative/archiver/{affId}")
    public Page<FacultativeListResp> archiverAffaire(@PathVariable Long affId, @RequestParam(required = false) Long cedId,
                                                     @RequestParam(defaultValue = "") String key,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size)
    {
        mvtService.createMvtAffaire(new MvtReq(affId, ARCHIVE.staCode,null));
        Page<FacultativeListResp> facPages = affRepo.searchAffaires(key, null, null, cedId
                ,jwtService.getConnectedUserCesId(),
                Arrays.asList(EN_COURS_DE_REGLEMENT.staCode), exoService.getExerciceCourant().getExeCode(), PageRequest.of(0, 10));

        List<FacultativeListResp> facList = facPages.stream().peek(fac->fac.setPlacementTermine(this.placementIsFinished(fac.getAffId()))).collect(Collectors.toList());
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




}

