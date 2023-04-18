package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.CreateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.UpdateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtSuivantReq;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceMouvement;
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

@RestController
@RequestMapping("/affaires")
@RequiredArgsConstructor
public class AffaireController
{
    private final IserviceFacultative facService;
    private final AffaireRepository affRepo;
    private IJwtService jwtService;
    private final IServiceMouvement mvtService;

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
    public Page<FacultativeListResp> searchAffaireByUser(@RequestParam(defaultValue = "") String key,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size)
    {
        return affRepo.searchAffaires(key, null, jwtService.getConnectedUserId(), null, null, null, Arrays.asList("SAI", "RET"), PageRequest.of(page, size));
    }

    @GetMapping(path = "/facultative/by-function")
    public Page<FacultativeListResp> searchAffaireByFnc(@RequestParam(defaultValue = "") String key,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size)
    {
        return affRepo.searchAffaires(key, jwtService.getConnectedUserFunctionId(), null, null, null,null, Arrays.asList("SAI", "RET", "CREP"), PageRequest.of(page, size));
    }

    @GetMapping(path = "/facultative/by-cedante")
    public Page<FacultativeListResp> searchAffaireByCedante(@RequestParam(defaultValue = "") String key,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size)
    {
        return affRepo.searchAffaires(key, null, null, jwtService.getConnectedUserCedId(), null, null, Arrays.asList("SAI", "RET", "CREP"), PageRequest.of(page, size));
    }

    @GetMapping(path = "/facultative/by-cedante-transmis") //Transmis par la cedante mais en cours de traitement
    public Page<FacultativeListResp> searchAffaireByCedanteTrans(@RequestParam(defaultValue = "") String key,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size)
    {
        return affRepo.searchAffaires(key, null, null, null, jwtService.getConnectedUserCedId(), null, Arrays.asList("VAL", "TRA"), PageRequest.of(page, size));
    }

    @GetMapping(path = "/facultative/by-reassureur-en-traitement") //Transmis par les cédantes et saisi par le réassureur
    public Page<FacultativeListResp> searchAffaireByReassureurEnTrai(@RequestParam(defaultValue = "") String key,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size)
    {
        return affRepo.searchAffaires(key, null, null, jwtService.getConnectedUserCesId(), null, jwtService.getConnectedUserCesId(),Arrays.asList("SAI", "TRA"), PageRequest.of(page, size));
    }

    @GetMapping(path = "/facultative/by-reassureur-valide") //validé par le réassureur
    public Page<FacultativeListResp> searchAffaireByReassureurValide(@RequestParam(defaultValue = "") String key,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size)
    {
        return affRepo.searchAffaires(key, null, null, jwtService.getConnectedUserCesId(), null, jwtService.getConnectedUserCesId(),Arrays.asList("VAL"), PageRequest.of(page, size));
    }

    //====================================

    @GetMapping(path = "/facultative/by-user-arch")
    public Page<FacultativeListResp> searchAffaireByUserAch(@RequestParam(defaultValue = "") String key,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size)
    {
        return affRepo.searchAffaires(key, null, jwtService.getConnectedUserId(), null, null, null, Arrays.asList("ARC"), PageRequest.of(page, size));
    }

    @GetMapping(path = "/facultative/by-function-arch")
    public Page<FacultativeListResp> searchAffaireByFncArch(@RequestParam(defaultValue = "") String key,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size)
    {
        return affRepo.searchAffaires(key, jwtService.getConnectedUserFunctionId(), null, null, null,null, Arrays.asList("ARC"), PageRequest.of(page, size));
    }

    @GetMapping(path = "/facultative/by-cedante-arch")
    public Page<FacultativeListResp> searchAffaireByCedanteArch(@RequestParam(defaultValue = "") String key,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size)
    {
        return affRepo.searchAffaires(key, null, null, jwtService.getConnectedUserCedId(), null, null, Arrays.asList("ARC"), PageRequest.of(page, size));
    }

    @PostMapping(path = "/affaire/transmettre/{affId}")
    public Page<FacultativeListResp> transmettreAffaire(@PathVariable Long affId)
    {
        mvtService.createMvtSuivant(new MvtSuivantReq("APLA", affId));
        return affRepo.searchAffaires("", null, null, jwtService.getConnectedUserCedId(), jwtService.getConnectedUserCedId(), null, Arrays.asList("SAI", "RET", "CREP"), PageRequest.of(0, 10));
    }

    @PostMapping(path = "/affaire/valider/{affId}")
    public void validerPlacement(@PathVariable Long affId)
    {
        mvtService.createMvtSuivant(new MvtSuivantReq("AREG", affId));
    }

    @PostMapping(path = "/affaire/archiver/{affId}")
    public void archiverAffaire(@PathVariable Long affId)
    {
        mvtService.createMvtSuivant(new MvtSuivantReq("ARC", affId));
    }
}
