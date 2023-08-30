package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.AffaireActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.MouvementRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dao.SinRepo;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.response.CouvertureListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.MvtMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.MouvementListResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceMouvement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.List;

@Service @RequiredArgsConstructor
public class ServiceMouvementImpl implements IServiceMouvement
{
    private final MouvementRepository mvtRepo;
    private final MvtMapper mvtMapper;
    private final AffaireRepository affRepo;
    private final RepartitionRepository repRepo;
    private final SinRepo sinRepo;
    private final ILogService logService;
    private final ObjectCopier<Affaire> affCopier;
    private final ObjectCopier<Sinistre> sinCopier;
    private final ObjectCopier<Repartition> repCopier;
    


    @Override @Transactional
    public void createMvtAffaire(MvtReq dto) throws UnknownHostException {
        Affaire aff = affRepo.findById(dto.getObjectId()).orElseThrow(()->new AppException("Affaire introuvable"));
        Affaire oldAff = affCopier.copy(aff);
        aff.setStatut(new Statut(dto.getStaCode()));
        logService.logg(dto.getAction(), oldAff, aff, SynchronReTables.AFFAIRE);
        affRepo.save(aff);
        Mouvement mvt = mvtMapper.mapToMvtAffaire(dto);
        mvtRepo.save(mvt);
    }

    @Override @Transactional
    public void createMvtPlacement(MvtReq dto) throws UnknownHostException {
        Repartition placement = repRepo.findPlacementById(dto.getObjectId()).orElseThrow(()->new AppException("Placement introuvable"));
        Repartition olPla = repCopier.copy(placement);
        placement.setRepStaCode(new Statut(dto.getStaCode()));
        logService.logg(dto.getAction(), olPla, placement, SynchronReTables.REPARTITION);
        repRepo.save(placement);
        Mouvement mvt = mvtMapper.mapToMvtPlacement(dto);
        mvtRepo.save(mvt);
    }

    @Override @Transactional
    public void createMvtSinistre(MvtReq dto) throws UnknownHostException {
        Sinistre sinistre = sinRepo.findById(dto.getObjectId()).orElseThrow(()->new AppException("Sinistre introuvable"));
        sinistre.setStatut(new Statut(dto.getStaCode()));
        Sinistre oldSin = sinCopier.copy(sinistre);
        sinistre.setStatut(new Statut(dto.getStaCode()));
        logService.logg(dto.getAction(), oldSin, sinistre, SynchronReTables.SINISTRE);
        sinRepo.save(sinistre);
        Mouvement mvt = mvtMapper.mapToMvtSinistre(dto);
        mvtRepo.save(mvt);
    }



    @Override
    public Page<MouvementListResp> findMouvementById(Long affId,Long sinId ,Pageable pageable) {
        return mvtRepo.findMouvementById(affId,sinId,pageable);
    }

//    public Page<CouvertureListResp> searchCouverture(String key, Pageable pageable) {
//        return  couvRepo.searchCouvertures(StringUtils.stripAccentsToUpperCase(key), pageable);
}
