package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtRetourAffaireReq;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtSuivantAffaireReq;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.MouvementListResp;

import java.util.List;

public interface IServiceMouvement
{
    void createMvtRet(MvtRetourAffaireReq dto);

    void createMvtSuivant(MvtSuivantAffaireReq dto);

    List<MouvementListResp> findByAffaire(Long affId);
}
