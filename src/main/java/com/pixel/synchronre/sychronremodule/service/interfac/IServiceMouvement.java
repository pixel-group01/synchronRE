package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtRetourReq;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtSuivantReq;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.MouvementListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Mouvement;

import java.util.List;

public interface IServiceMouvement
{
    void createMvtRet(MvtRetourReq dto);

    void createMvtSuivant(MvtSuivantReq dto);

    List<MouvementListResp> findByAffaire(Long affId);
}
