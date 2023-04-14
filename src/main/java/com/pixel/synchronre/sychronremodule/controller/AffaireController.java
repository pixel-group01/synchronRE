package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.CreateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.UpdateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeListResp;
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
        return affRepo.searchAffaires(key, null, jwtService.getConnectedUserId(), null, null, Arrays.asList("SAI", "RET"), PageRequest.of(page, size));
    }

    @GetMapping(path = "/facultative/by-function")
    public Page<FacultativeListResp> searchAffaireByFnc(@RequestParam(defaultValue = "") String key,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size)
    {
        return affRepo.searchAffaires(key, jwtService.getConnectedUserFunctionId(), null, null, null, Arrays.asList("SAI", "RET"), PageRequest.of(page, size));
    }

    @GetMapping(path = "/facultative/by-cedante")
    public Page<FacultativeListResp> searchAffaireByCedante(@RequestParam(defaultValue = "") String key,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size)
    {
        return affRepo.searchAffaires(key, null, null, jwtService.getConnectedUserCedId(), null, Arrays.asList("SAI", "RET"), PageRequest.of(page, size));
    }

    @GetMapping(path = "/facultative/by-reassureur")
    public Page<FacultativeListResp> searchAffaireByReassureur(@RequestParam(defaultValue = "") String key,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size)
    {
        return affRepo.searchAffaires(key, null, null, null, jwtService.getConnectedUserCedParentId(), Arrays.asList("SAI", "TRA"), PageRequest.of(page, size));
    }
}
