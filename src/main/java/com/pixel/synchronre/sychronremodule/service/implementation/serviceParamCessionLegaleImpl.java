package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.ParamCessionLegaleRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.ParamCessionLegaleMapper;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.request.CreateParamCessionLegaleReq;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.request.UpdateParamCessionLegaleReq;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceParamCessionLegale;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

@Service
@RequiredArgsConstructor
public class serviceParamCessionLegaleImpl implements IserviceParamCessionLegale {

    private final ParamCessionLegaleRepository paramRepo;
    private final ParamCessionLegaleMapper paramMapper;
    private final ObjectCopier<ParamCessionLegale> paramCopier;
    private final ILogService logService;

    @Override
    public ParamCessionLegaleDetailsResp createParamCessionLegale(CreateParamCessionLegaleReq dto) throws UnknownHostException {
        ParamCessionLegale param = paramMapper.mapParamCessionToParamCessionLegaleReq(dto);
        param = paramRepo.save(param);
        logService.logg(SynchronReActions.CREATE_PARAM_CESSION_LEGALE, null, param, SynchronReTables.PARAM_CESSION_LEGALE);
        return  paramMapper.mapParamDetailsToParamCessionLegale(param);
    }

    @Override
    public ParamCessionLegaleDetailsResp updateParamCessionLegale(UpdateParamCessionLegaleReq dto) throws UnknownHostException {
        ParamCessionLegale param = paramRepo.findById(dto.getParamCesLegId()).orElseThrow(()->new AppException("Paramètre de cession légale introuvable"));
        ParamCessionLegale oldParam = paramCopier.copy(param);
        param.setParamCesLegCapital(dto.getParamCesLegCapital());
        param.setParamCesLegLibelle(dto.getParamCesLegLibelle());
        param.setPays(new Pays(dto.getPaysId()));
        param = paramRepo.save(param);
        logService.logg(SynchronReActions.UPDATE_PARAM_CESSION_LEGALE, oldParam, param, SynchronReTables.PARAM_CESSION_LEGALE);
        return paramMapper.mapParamDetailsToParamCessionLegale(param);
    }

    @Override
    public Page<ParamCessionLegaleListResp> searchParamCessionLegale(String key, Pageable pageable) {
        return paramRepo.searchParams(StringUtils.stripAccentsToUpperCase(key), pageable);
    }
}
