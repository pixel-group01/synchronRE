package com.pixel.synchronre.authmodule.controller.services.impl;

import com.pixel.synchronre.authmodule.controller.services.spec.IHistoDetailsService;
import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.authmodule.model.entities.HistoDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service @RequiredArgsConstructor
public class HistoDetailsService implements IHistoDetailsService
{
    private final IJwtService jwtService;
    @Override
    public HistoDetails getHistoDetailsFromSecurityContext(String actionName)
    {
        String actionId = UUID.randomUUID().toString();
        String connectionId = jwtService.getCurrentJwt() == null ? null : jwtService.getJwtInfos().getConnectionId();

        HistoDetails actionIdentifier = new HistoDetails(actionName, actionId, connectionId);

        return actionIdentifier;
    }
}
