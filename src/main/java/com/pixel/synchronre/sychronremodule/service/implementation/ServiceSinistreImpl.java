package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.SinRepo;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.SinMapper;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.CreateSinistreReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.UpdateSinistreReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.SinistreDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Sinistre;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceSinistre;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.Arrays;

@Service @RequiredArgsConstructor
public class ServiceSinistreImpl implements IServiceSinistre
{
    private final ObjectCopier<Sinistre> sinCopier;
    private final ILogService logService;
    private final SinRepo sinRepo;
    private final SinMapper sinMapper;
    private final IJwtService jwtService;

    @Override
    public SinistreDetailsResp createSinistre(CreateSinistreReq dto) throws UnknownHostException
    {
        Sinistre sinistre = sinMapper.mapToSinistre(dto);
        sinistre = sinRepo.save(sinistre);
        logService.logg(SynchronReActions.CREATE_SINISTRE, null, sinistre, SynchronReTables.SINISTRE);
        return sinMapper.mapToSinistreDetailsResp(sinistre);
    }

    @Override
    public SinistreDetailsResp updateSinistre(UpdateSinistreReq dto) throws UnknownHostException
    {
        Sinistre sinistre = sinRepo.findById(dto.getSinId()).orElseThrow(()->new AppException("Sinistre introuvable"));
        Sinistre oldSin = sinCopier.copy(sinistre);
        BeanUtils.copyProperties(dto, sinistre);
        sinistre = sinRepo.save(sinistre);
        logService.logg(SynchronReActions.UPDATE_SINISTRE, oldSin, sinistre, SynchronReTables.SINISTRE);
        return sinMapper.mapToSinistreDetailsResp(sinistre);
    }

    @Override
    public Page<SinistreDetailsResp> searchSinistre(String key, Pageable pageable)
    {
        Page<SinistreDetailsResp> sinPage = sinRepo.searchSinistres(key,null, null, null, jwtService.getConnectedUserCesId(), Arrays.asList(), pageable);
        return sinPage;
    }
}
