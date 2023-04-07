package com.pixel.synchronre.sychronremodule.service.implementation;


import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.BrancheRepository;
import com.pixel.synchronre.sychronremodule.model.dto.banque.request.UpdateBanqueReq;
import com.pixel.synchronre.sychronremodule.model.dto.banque.response.BanqueDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.banque.response.BanqueListResp;
import com.pixel.synchronre.sychronremodule.model.dto.branche.request.CreateBrancheReq;
import com.pixel.synchronre.sychronremodule.model.dto.branche.request.UpdateBrancheReq;
import com.pixel.synchronre.sychronremodule.model.dto.branche.response.BrancheDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.branche.response.BrancheListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.BrancheMapper;
import com.pixel.synchronre.sychronremodule.model.entities.Banque;
import com.pixel.synchronre.sychronremodule.model.entities.Branche;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceBranche;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

@Service
@RequiredArgsConstructor
public class serviceBrancheImpl implements IserviceBranche {

    private final BrancheRepository branRepo;
    private final BrancheMapper branMapper;
    private final ObjectCopier<Branche> branCopier;
    private final ILogService logService;

    @Override @Transactional
    public BrancheDetailsResp createBranche(CreateBrancheReq dto) throws UnknownHostException {
        Branche bran = branMapper.mapBrancheToBrancheReq(dto);
        bran = branRepo.save(bran);
        logService.logg(SynchronReActions.CREATE_BRANCHE, null, bran, SynchronReTables.BRANCHE);
        return branMapper.mapBrancheDetailsRespToBranche(bran);
    }

    @Override  @Transactional
    public BrancheDetailsResp updateBranche(UpdateBrancheReq dto) throws UnknownHostException {
        Branche bran = branRepo.findById(dto.getBranId()).orElseThrow(()->new AppException("Branche introuvable"));
        Branche oldBranche = branCopier.copy(bran);
        bran.setBranLibelle(dto.getBranLibelle());
        bran.setBranLibelleAbrege(dto.getBranLibelleAbrege());
        bran = branRepo.save(bran);
        logService.logg(SynchronReActions.UPDATE_BRANCHE, oldBranche, bran, SynchronReTables.BRANCHE);
        return branMapper.mapBrancheDetailsRespToBranche(bran);
    }

    @Override
    public Page<BrancheListResp> searchBranche(String key, Pageable pageable) {
        return branRepo.searchBranches(StringUtils.stripAccentsToUpperCase(key), pageable);
    }
}
