package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.MouvementListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Mouvement;

import java.util.List;

public interface IServiceMouvement
{
    void createMouvement(Long affId, String staCode, String mvtObs);

    List<MouvementListResp> findByAffaire(Long affId);
}
