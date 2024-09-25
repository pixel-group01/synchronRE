package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.CouvertureRepository;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.request.CreateCouvertureReq;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.request.UpdateCouvertureReq;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.response.CouvertureDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.response.CouvertureListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.CouvertureMapper;
import com.pixel.synchronre.sychronremodule.model.entities.Branche;
import com.pixel.synchronre.sychronremodule.model.entities.Couverture;
import com.pixel.synchronre.sychronremodule.model.entities.Tranche;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceCouverture;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class serviceCouvertureImpl implements IserviceCouverture {

    private final CouvertureRepository couvRepo;
    private final CouvertureMapper couvMapper;
    private final ObjectCopier<Couverture> couvCopier;
    private final ILogService logService;

    @Override
    public CouvertureDetailsResp createCouverture(CreateCouvertureReq dto) throws UnknownHostException {
        Couverture couv = couvMapper.mapCouvertureReqToCouverture(dto);
        couv = couvRepo.save(couv);
        logService.logg(SynchronReActions.CREATE_COUVERTURE, null, couv, SynchronReTables.COUVERTURE);
        return couvMapper.mapToCouvertureDetailsResp(couv);
    }

    @Override
    public CouvertureDetailsResp updateCouverture(UpdateCouvertureReq dto) throws UnknownHostException {
        Couverture couv = couvRepo.findById(dto.getCouId()).orElseThrow(()->new AppException("Couverture introuvable"));
        Couverture oldCouverture = couvCopier.copy(couv);
        couv.setCouLibelle(dto.getCouLibelle());
        couv.setCouLibelleAbrege(dto.getCouLibelleAbrege());
        couv.setCouParent(dto.getCouParentId() == null ? null : new Couverture(dto.getCouParentId()));
        couv.setBranche(new Branche(dto.getBranId()));
        couv = couvRepo.save(couv);
        logService.logg(SynchronReActions.UPDATE_COUVERTURE, oldCouverture, couv, SynchronReTables.COUVERTURE);
        return couvMapper.mapToCouvertureDetailsResp(couv);
    }

    @Override
    public List<CouvertureListResp> getCouerturesParents(Long traiteNpId)
    {
        return couvRepo.getCouerturesParents(traiteNpId);
    }

    @Override
    public Page<CouvertureListResp> searchCouverture(String key, Pageable pageable) {
        return  couvRepo.searchCouvertures(StringUtils.stripAccentsToUpperCase(key), pageable);
    }

    @Override
    public List<CouvertureListResp> getCouerturesFilles(Long traiteNpId, Long couParentId) {
        return couvRepo.getCouerturesFilles(traiteNpId, couParentId);
    }
}
