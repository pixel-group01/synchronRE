package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.AssociationRepository;
import com.pixel.synchronre.sychronremodule.model.dao.PaysRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.PaysMapper;
import com.pixel.synchronre.sychronremodule.model.dto.pays.request.CreatePaysReq;
import com.pixel.synchronre.sychronremodule.model.dto.pays.request.UpdatePaysReq;
import com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Pays;
import com.pixel.synchronre.sychronremodule.service.interfac.IservicePays;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class servicePaysImpl implements IservicePays {

    private final PaysRepository paysRepo;
    private final PaysMapper paysMapper;
    private final ObjectCopier<Pays> payCopier;
    private final ILogService logService;
    private final AssociationRepository assoRepo;

    @Override
    public PaysDetailsResp createPays(CreatePaysReq dto) {
        Pays pay = paysMapper.mapToPaysReq(dto);
        pay = paysRepo.save(pay);
        logService.logg(SynchronReActions.CREATE_PAYS, null, pay, SynchronReTables.PAYS);
        return paysMapper.mapToPaysDetails(pay);
    }

    @Override
    public PaysDetailsResp updatePays(UpdatePaysReq dto)
    {
        Pays pays = paysRepo.findById(dto.getPaysCode()).orElseThrow(()->new AppException("Code pays introuvable"));
        Pays oldPays = payCopier.copy(pays);
        BeanUtils.copyProperties(dto, pays);
        pays =  paysRepo.save(pays);
        logService.logg(SynchronReActions.UPDATE_PAYS, oldPays, pays, SynchronReTables.PAYS);
        return paysMapper.mapToPaysDetails(pays);
    }

    @Override
    public Page<PaysListResp> searchPays(String key, Pageable pageable) {
        return paysRepo.searchPays(StringUtils.stripAccentsToUpperCase(key), pageable);
    }

    @Override
    public List<PaysListResp> getPaysByOrgCodes(List<String> orgCodes) {
        List<PaysListResp> allPays = paysRepo.getAllPays();
        if(orgCodes == null || orgCodes.isEmpty()) return allPays;
        return assoRepo.getPaysByOrgCodes(orgCodes);
    }
}
