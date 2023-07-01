package com.pixel.synchronre.authmodule.controller.services.spec;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.dtos.appfunction.CreateFncDTO;
import com.pixel.synchronre.authmodule.model.dtos.appfunction.ReadFncDTO;
import com.pixel.synchronre.authmodule.model.dtos.asignation.SetAuthoritiesToFunctionDTO;
import com.pixel.synchronre.authmodule.model.dtos.appfunction.UpdateFncDTO;

import java.net.UnknownHostException;
import java.util.List;

public interface IFunctionService
{
    Long getActiveCurrentFunctionId(Long userId);
    String getActiveCurrentFunctionName(Long userId);
    ReadFncDTO createFnc(CreateFncDTO dto) throws UnknownHostException;
    void setFunctionAsDefault(Long fncId) throws UnknownHostException;
    void revokeFunction(Long fncId) throws UnknownHostException;
    void restoreFunction(Long fncId) throws UnknownHostException;
    ReadFncDTO setFunctionAuthorities(SetAuthoritiesToFunctionDTO dto);
    ReadFncDTO updateFunction(UpdateFncDTO dto);

    ReadFncDTO getActiveCurrentFunction(Long userId);

    ReadFncDTO getFunctioninfos(Long foncId);
}
