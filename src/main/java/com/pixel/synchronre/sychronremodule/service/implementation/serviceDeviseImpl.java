package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.DeviseRepository;
import com.pixel.synchronre.sychronremodule.model.dto.banque.response.BanqueDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.devise.request.CreateDeviseReq;
import com.pixel.synchronre.sychronremodule.model.dto.devise.request.UpdateDeviseReq;
import com.pixel.synchronre.sychronremodule.model.dto.devise.response.DeviseDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.devise.response.DeviseListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.DeviseMapper;
import com.pixel.synchronre.sychronremodule.model.entities.Banque;
import com.pixel.synchronre.sychronremodule.model.entities.Devise;
import com.pixel.synchronre.sychronremodule.model.entities.Pays;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceDevise;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class serviceDeviseImpl implements IServiceDevise {

    private final DeviseRepository devRepo;
    private final DeviseMapper devMapper;
    private final ObjectCopier<Devise> devCopier;
    private final ILogService logService;


    @Override @Transactional
    public DeviseDetailsResp createDevise(CreateDeviseReq dto) throws UnknownHostException {
        Devise dev = devMapper.mapDeviseToDeviseReq(dto);
        dev = devRepo.save(dev);
        logService.logg(SynchronReActions.CREATE_DEVISE, null, dev, SynchronReTables.DEVISE);
        return devMapper.mapDeviseDetailsRespToDevise(dev);
    }

    @Override @Transactional
    public DeviseDetailsResp updateDevise(UpdateDeviseReq dto) throws UnknownHostException {
        Devise dev = devMapper.mapToUpdateDeviseReq(dto);
        Devise oldDevise = devCopier.copy(dev);
        if(devRepo.existsDevCode(dto.getDevCode())){
            dev.setDevLibelle(dto.getDevLibelle());
            dev.setDevLibelleAbrege(dto.getDevLibelleAbrege());
        }
        dev =  devRepo.save(dev);
        logService.logg(SynchronReActions.UPDATE_DEVISE, oldDevise, dev, SynchronReTables.DEVISE);

        return devMapper.mapDeviseDetailsRespToDevise(dev);
    }

    @Override
    public List<DeviseListResp> searchDevise(String key) {
        return devRepo.searchDevises(StringUtils.stripAccentsToUpperCase(key));
    }
}
