package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.PaysRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.PaysMapper;
import com.pixel.synchronre.sychronremodule.model.dto.pays.request.CreatePaysReq;
import com.pixel.synchronre.sychronremodule.model.dto.pays.request.UpdatePaysReq;
import com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Banque;
import com.pixel.synchronre.sychronremodule.model.entities.Pays;
import com.pixel.synchronre.sychronremodule.service.interfac.IservicePays;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;


@Service
@RequiredArgsConstructor
public class servicePaysImpl implements IservicePays {

    private final PaysRepository paysRepo;
    private final PaysMapper paysMapper;
    private final ObjectCopier<Pays> payCopier;
    private final ILogService logService;

    @Override
    public PaysDetailsResp createPays(CreatePaysReq dto) throws UnknownHostException {
        Pays pay = paysMapper.mapToPaysReq(dto);
        pay = paysRepo.save(pay);
        logService.logg(SynchronReActions.CREATE_PAYS, null, pay, SynchronReTables.PAYS);
        return paysMapper.mapToPaysDetails(pay);
    }

    @Override
    public PaysDetailsResp updatePays(UpdatePaysReq dto) throws UnknownHostException {
        Pays pay = paysMapper.mapToUpdatePaysReq(dto);
        Pays oldPays = payCopier.copy(pay);
        if(paysRepo.existsByPaysCode(dto.getPaysCode())){
            pay.setPaysIndicatif(dto.getPaysIndicatif());
            pay.setPaysNom(dto.getPaysNom());
        }
        pay =  paysRepo.save(pay);
        logService.logg(SynchronReActions.UPDATE_PAYS, oldPays, pay, SynchronReTables.PAYS);
        return paysMapper.mapToPaysDetails(pay);
    }

    @Override
    public Page<PaysListResp> searchPays(String key, Pageable pageable) {
        return paysRepo.searchPays(StringUtils.stripAccentsToUpperCase(key), pageable);
    }
}
