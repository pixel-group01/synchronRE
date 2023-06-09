package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.MouvementListResp;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IServiceMouvement
{
    @Transactional
    void createMvtAffaire(MvtReq dto);

    @Transactional
    void createMvtPlacement(MvtReq dto);

    @Transactional
    void createMvtSinistre(MvtReq dto);

    List<MouvementListResp> findMvtAffaire(Long affId);

    List<MouvementListResp> findMvtPlacement(Long plaId);

    List<MouvementListResp> findMvtSinistre(Long sinId);
}
