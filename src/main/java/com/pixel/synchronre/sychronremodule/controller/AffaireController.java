package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.CreateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.UpdateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.EtatComptableAffaire;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.FacultativeMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtRetourReq;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtSuivantReq;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Exercice;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceMouvement;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceAffaire;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceExercie;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceFacultative;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.Arrays;

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
        return affRepo.searchAffaires(key, null, jwtService.getConnectedUserId(), null, null, Arrays.asList(SAISIE.staCode, RETOURNE.staCode), exeCode, PageRequest.of(page, size));
    }

    @GetMapping(path = "/facultative/by-function")
    public Page<FacultativeListResp> searchAffaireByFnc(@RequestParam(required = false) Long exeCode,
                                                        @RequestParam(defaultValue = "") String key,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        return affRepo.searchAffaires(key, jwtService.getConnectedUserFunctionId(), null, null,null, Arrays.asList(SAISIE.staCode, RETOURNE.staCode, EN_COURS_DE_REPARTITION.staCode), exeCode, PageRequest.of(page, size));
    }

    @GetMapping(path = "/facultative/by-cedante")
    public Page<FacultativeListResp> searchAffaireByCedante(@RequestParam(required = false) Long exeCode,
                                                            @RequestParam(defaultValue = "") String key,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        return affRepo.searchAffaires(key, null, null,  jwtService.getConnectedUserCedId(), null, Arrays.asList(SAISIE.staCode, RETOURNE.staCode, EN_COURS_DE_REPARTITION.staCode), exeCode, PageRequest.of(page, size));
    }

    @GetMapping(path = "/facultative/by-cedante-transmis") //Transmis par la cedante mais en cours de traitement
    public Page<FacultativeListResp> searchAffaireByCedanteTrans(@RequestParam(required = false) Long exeCode,
                                                                 @RequestParam(defaultValue = "") String key,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        return affRepo.searchAffaires(key, null, null, jwtService.getConnectedUserCedId(), null, Arrays.asList(EN_ATTENTE_DE_PLACEMENT.staCode, EN_COURS_DE_PLACEMENT.staCode), exeCode, PageRequest.of(page, size));
    }

    @GetMapping(path = "/facultative/by-reassureur-en-traitement") //Transmis par les cédantes et saisi par le réassureur
    public Page<FacultativeListResp> searchAffaireByReassureurEnTrai(@RequestParam(required = false) Long exeCode,
                                                                     @RequestParam(defaultValue = "") String key,
                                                         @RequestParam(required = false) Long cedId,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        return affRepo.searchAffaires(key, null, null, cedId, jwtService.getConnectedUserCesId(),Arrays.asList(SAISIE_CRT.staCode, TRANSMIS.staCode, EN_ATTENTE_DE_PLACEMENT.staCode, EN_COURS_DE_PLACEMENT.staCode), exeCode, PageRequest.of(page, size));
    }

    @GetMapping(path = "/facultative/by-reassureur-valide") //validé par le réassureur
    public Page<FacultativeListResp> searchAffaireByReassureurValide(@RequestParam(required = false) Long exeCode,
                                                                     @RequestParam(defaultValue = "") String key,
                                                                     @RequestParam(required = false) Long cedId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        return affRepo.searchAffaires(key, null, null,  cedId, jwtService.getConnectedUserCesId(),Arrays.asList("VAL"), exeCode, PageRequest.of(page, size));
    }

    //====================================

    @GetMapping(path = "/facultative/by-user-arch")
    public Page<FacultativeListResp> searchAffaireByUserAch(@RequestParam(required = false) Long exeCode,
                                                            @RequestParam(defaultValue = "") String key,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        return affRepo.searchAffaires(key, null, jwtService.getConnectedUserId(), null, null, Arrays.asList(ARCHIVE.staCode), exeCode, PageRequest.of(page, size));
    }

    @GetMapping(path = "/facultative/by-function-arch")
    public Page<FacultativeListResp> searchAffaireByFncArch(@RequestParam(required = false) Long exeCode,
                                                            @RequestParam(defaultValue = "") String key,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        return affRepo.searchAffaires(key, jwtService.getConnectedUserFunctionId(), null, null,null, Arrays.asList(ARCHIVE.staCode), exeCode, PageRequest.of(page, size));
    }

    @GetMapping(path = "/facultative/by-cedante-arch")
    public Page<FacultativeListResp> searchAffaireByCedanteArch(@RequestParam(required = false) Long exeCode,
                                                                 @RequestParam(defaultValue = "") String key,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size)
    {
        exeCode = exeCode ==null ? exoService.getExerciceCourant().getExeCode() : exeCode;
        return affRepo.searchAffaires(key, null, null, jwtService.getConnectedUserCedId(),  null, Arrays.asList(ARCHIVE.staCode), exeCode, PageRequest.of(page, size));
    }

    @PostMapping(path = "/facultative/transmettre/{affId}")
    public Page<FacultativeListResp> transmettreAffaire(@PathVariable Long affId)
    {
        mvtService.createMvtSuivant(new MvtSuivantReq(EN_ATTENTE_DE_PLACEMENT.staCode, affId));
        return affRepo.searchAffaires("", null, null,
                jwtService.getConnectedUserCedId(),
                null, Arrays.asList(SAISIE.staCode, RETOURNE.staCode, EN_COURS_DE_REPARTITION.staCode), exoService.getExerciceCourant().getExeCode(), PageRequest.of(0, 10));
    }

    @PostMapping(path = "/facultative/retourner")
    public Page<FacultativeListResp> retournerAffaire(@Valid @RequestBody MvtRetourReq dto)
    {
        mvtService.createMvtRet(dto);
        return affRepo.searchAffaires("", null, null,
                affRepo.getAffCedId(dto.getAffId()),
                null, Arrays.asList(EN_ATTENTE_DE_PLACEMENT.staCode, EN_COURS_DE_PLACEMENT.staCode), exoService.getExerciceCourant().getExeCode(), PageRequest.of(0, 10));
    }

    @PostMapping(path = "/facultative/valider/{affId}")
    public Page<FacultativeListResp> validerPlacement(@PathVariable Long affId, @RequestParam(required = false) Long cedId)
    {
        mvtService.createMvtSuivant(new MvtSuivantReq(EN_COURS_DE_REGLEMENT.staCode, affId));
        return affRepo.searchAffaires("", null, null, cedId,
                jwtService.getConnectedUserCesId(), Arrays.asList(EN_ATTENTE_DE_PLACEMENT.staCode, EN_COURS_DE_PLACEMENT.staCode), exoService.getExerciceCourant().getExeCode(), PageRequest.of(0, 10));
    }

    @PostMapping(path = "/facultative/archiver/{affId}")
    public Page<FacultativeListResp> archiverAffaire(@PathVariable Long affId, @RequestParam(required = false) Long cedId)
    {
        mvtService.createMvtSuivant(new MvtSuivantReq(ARCHIVE.staCode, affId));
        return affRepo.searchAffaires("", null, null, cedId
                ,jwtService.getConnectedUserCesId(),
                Arrays.asList(EN_COURS_DE_REGLEMENT.staCode), exoService.getExerciceCourant().getExeCode(), PageRequest.of(0, 10));
    }

    @GetMapping(path = "/facultative/etat-comptable/{affId}")
    public EtatComptableAffaire getEtatComptable(@PathVariable Long affId)
    {
        return affService.getEtatComptable(affId);
    }
}

