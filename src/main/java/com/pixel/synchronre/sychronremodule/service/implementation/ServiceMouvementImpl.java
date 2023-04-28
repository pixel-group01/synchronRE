package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.MouvementRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.MvtMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtPlacementReq;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtRetourAffaireReq;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtSuivantAffaireReq;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.MouvementListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Mouvement;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
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


    @Override @Transactional
    public void createMvtRet(MvtRetourAffaireReq dto)
    {
        Affaire aff = affRepo.findById(dto.getAffId()).orElseThrow(()->new AppException("Affaire introuvable"));
        aff.setStatut(new Statut("RET"));
        Mouvement mvt = mvtMapper.mapTomouvement(dto);
        mvtRepo.save(mvt);
    }

    @Override @Transactional
    public void createMvtSuivant(MvtSuivantAffaireReq dto)
    {
        Affaire aff = affRepo.findById(dto.getAffId()).orElseThrow(()->new AppException("Affaire introuvable"));
        aff.setStatut(new Statut(dto.getStaCode()));
        affRepo.save(aff);
        Mouvement mvt = mvtMapper.mapTomouvement(dto);
        mvtRepo.save(mvt);
    }



    @Override
    public List<MouvementListResp> findByAffaire(Long affId) {
        return mvtRepo.findByAffaire(affId);
    }


}
