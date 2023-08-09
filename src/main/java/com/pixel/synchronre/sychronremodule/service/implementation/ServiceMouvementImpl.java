package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.MouvementRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dao.SinRepo;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.MvtMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.MouvementListResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceMouvement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class ServiceMouvementImpl implements IServiceMouvement
{
    private final MouvementRepository mvtRepo;
    private final MvtMapper mvtMapper;
    private final AffaireRepository affRepo;
    private final RepartitionRepository repRepo;
    private final SinRepo sinRepo;

    @Override @Transactional
    public void createMvtAffaire(MvtReq dto)
    {
        Affaire aff = affRepo.findById(dto.getObjectId()).orElseThrow(()->new AppException("Affaire introuvable"));
        aff.setStatut(new Statut(dto.getStaCode()));
        affRepo.save(aff);
        Mouvement mvt = mvtMapper.mapToMvtAffaire(dto);
        mvtRepo.save(mvt);
    }

    @Override @Transactional
    public void createMvtPlacement(MvtReq dto)
    {
        Repartition placement = repRepo.findPlacementById(dto.getObjectId()).orElseThrow(()->new AppException("Placement introuvable"));
        placement.setRepStaCode(new Statut(dto.getStaCode()));
        repRepo.save(placement);
        Mouvement mvt = mvtMapper.mapToMvtPlacement(dto);
        mvtRepo.save(mvt);
    }

    @Override @Transactional
    public void createMvtSinistre(MvtReq dto)
    {
        Sinistre sinistre = sinRepo.findById(dto.getObjectId()).orElseThrow(()->new AppException("Sinistre introuvable"));
        sinistre.setStatut(new Statut(dto.getStaCode()));
        sinRepo.save(sinistre);
        Mouvement mvt = mvtMapper.mapToMvtSinistre(dto);
        mvtRepo.save(mvt);
    }



    @Override
    public List<MouvementListResp> findMouvementById(Long affId,Long sinId) {
        return mvtRepo.findMouvementById(affId,sinId);
    }
}
