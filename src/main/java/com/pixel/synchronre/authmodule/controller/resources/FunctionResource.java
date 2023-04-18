package com.pixel.synchronre.authmodule.controller.resources;

import com.pixel.synchronre.authmodule.controller.repositories.FunctionRepo;
import com.pixel.synchronre.authmodule.controller.services.spec.IFunctionService;
import com.pixel.synchronre.authmodule.model.dtos.appfunction.ReadFncDTO;
import com.pixel.synchronre.authmodule.model.dtos.appfunction.CreateFncDTO;
import com.pixel.synchronre.authmodule.model.dtos.appfunction.UpdateFncDTO;
import com.pixel.synchronre.authmodule.model.dtos.asignation.SetAuthoritiesToFunctionDTO;
import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.Set;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/functions")
public class FunctionResource
{
    private final IFunctionService functionService;
    private final FunctionRepo functionRepo;

    @PostMapping(path = "/create")
    public ReadFncDTO createFunction(@RequestBody CreateFncDTO dto) throws UnknownHostException {
        return functionService.createFnc(dto);
    }

    @PutMapping(path = "/update")
    public ReadFncDTO updateFunction(@RequestBody UpdateFncDTO dto) throws UnknownHostException {
         return functionService.updateFunction(dto);
    }

    @GetMapping(path = "/get-current-fnc-for-user/{userId}")
    public Long getActiveCurrentFunctionId(@PathVariable Long userId)
    {
        return functionService.getActiveCurrentFunctionId(userId);
    }

    @PutMapping(path = "/set-fnc-as-default/{fncId}")
    public void setFunctionAsDefault(@PathVariable Long fncId) throws UnknownHostException
    {
        functionService.setFunctionAsDefault(fncId);
    }
    @PutMapping(path = "/revoke/{fncId}")
    public void revokeFunction(@PathVariable Long fncId) throws UnknownHostException
    {
        functionService.revokeFunction(fncId);
    }
    @PutMapping(path = "/restore/{fncId}")
    public void restoreFunction(@PathVariable Long fncId) throws UnknownHostException
    {
        functionService.restoreFunction(fncId);
    }

    @PutMapping(path = "/set-authorities")
    public AppFunction setFunctionAuthorities(@RequestBody SetAuthoritiesToFunctionDTO dto)
    {
        return functionService.setFunctionAuthorities(dto);
    }

    @GetMapping(path = "/active-fnc-for-user/{userId}")
    public Set<AppFunction> getActiveFunctionsForUser(@PathVariable Long userId)
    {
        return functionRepo.findActiveByUser(userId);
    }

    @GetMapping(path = "/all-fnc-for-user/{userId}")
    public Set<AppFunction> getUsersAllFunctions(@PathVariable Long userId)
    {
        return functionRepo.findAllByUser(userId);
    }
}
