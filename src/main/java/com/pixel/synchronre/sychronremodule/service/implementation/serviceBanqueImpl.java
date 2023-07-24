package com.pixel.synchronre.sychronremodule.service.implementation;


import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.BanqueRepository;
import com.pixel.synchronre.sychronremodule.model.dto.banque.request.CreateBanqueReq;
import com.pixel.synchronre.sychronremodule.model.dto.banque.request.UpdateBanqueReq;
import com.pixel.synchronre.sychronremodule.model.dto.banque.response.BanqueDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.banque.response.BanqueListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.BanqueMapper;
import com.pixel.synchronre.sychronremodule.model.entities.Banque;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceBanque;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

@Service
@RequiredArgsConstructor
public class serviceBanqueImpl implements IserviceBanque {

    private final BanqueRepository banRepo;
    private final BanqueMapper banMapper;
    private final ObjectCopier<Banque> banCopier;
    private final ILogService logService;

    @Override @Transactional
    public BanqueDetailsResp createBanque(CreateBanqueReq dto) throws UnknownHostException {
        Banque ban = banMapper.mapBanqueToBanqueReq(dto);
        ban = banRepo.save(ban);
        logService.logg(SynchronReActions.CREATE_BANQUE, null, ban, SynchronReTables.BANQUE);
        return banMapper.mapBanqueDetailsRespToBanque(ban);
    }

    @Override  @Transactional
    public BanqueDetailsResp updateBanque(UpdateBanqueReq dto) throws UnknownHostException {
        Banque ban = banRepo.findById(dto.getBanId()).orElseThrow(()->new AppException("Banque introuvable"));
        Banque oldBanque = banCopier.copy(ban);
        ban.setBanCode(dto.getBanCode());
        ban.setBanLibelle(dto.getBanLibelle());
        ban.setBanLibelleAbrege(dto.getBanLibelleAbrege());
        ban.setBanNumCompte(dto.getBanNumCompte());
        ban.setBanIban(dto.getBanIban());
        ban.setBanCodeBic(dto.getBanCodeBic());
        ban = banRepo.save(ban);
        logService.logg(SynchronReActions.UPDATE_BANQUE, oldBanque, ban, SynchronReTables.BANQUE);
        return banMapper.mapBanqueDetailsRespToBanque(ban);
    }

    @Override
    public Page<BanqueListResp> searchBanque(String key, Pageable pageable) {
        return banRepo.searchBanques(StringUtils.stripAccentsToUpperCase(key), pageable);
    }
}
