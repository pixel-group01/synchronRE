package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.MouvementListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Mouvement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;

public interface IServiceMouvement
{
    @Transactional
    void createMvtAffaire(MvtReq dto);
    @Transactional
    void createMvtPlacement(MvtReq dto);
    @Transactional
    void createMvtSinistre(MvtReq dto) throws UnknownHostException;
    Page<MouvementListResp> findMouvementById(Long affId, Long sinId , Pageable pageable);
    Mouvement getAvantDernierByAffId(Long affId);
    Mouvement getAvantDernierByPlaId(Long plaId);
    Mouvement getAvantDernierBySinId(Long sinId);
    @Transactional
    void createMvtTraite(MvtReq dto);


}
