package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sychronremodule.model.dao.MouvementRepository;
import com.pixel.synchronre.sychronremodule.model.dao.StatutRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.MouvementListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Mouvement;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceMouvement;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service @RequiredArgsConstructor
public class ServiceMouvementImpl implements IServiceMouvement
{
    private final MouvementRepository mvtRepo;
    private final StatutRepository staRepo;

    @Override
    public void createMouvement(Long affId, String staCode, String mvtObs)
    {
        Mouvement mvt = new Mouvement(null, new Statut(staCode), mvtObs, new Affaire(affId), LocalDateTime.now());
        mvtRepo.save(mvt);
    }

    @Override
    public List<MouvementListResp> findByAffaire(Long affId) {
        return mvtRepo.findByAffaire(affId);
    }
}
