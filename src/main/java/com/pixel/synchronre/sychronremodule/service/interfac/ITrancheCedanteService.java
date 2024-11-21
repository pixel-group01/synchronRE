package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.TrancheCedanteReq;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.TrancheCedanteResp;
import com.pixel.synchronre.sychronremodule.model.events.TrancheEvent;
import com.pixel.synchronre.sychronremodule.model.views.CedanteTraite;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

public interface ITrancheCedanteService
{
    TrancheCedanteReq save(TrancheCedanteReq dto);

    List<ReadCedanteDTO> getListCedanteAsaisirSurTraite(Long traiteNpId);

    TrancheCedanteReq getEditDto(TrancheCedanteReq dto, int scale);

    @Transactional
    void onAddOrRemoveCedanteToCategorie(Long cedId, Long catId);

    void onAddOrRemoveCategorieToTranche(Long catId, Long trancheId);

    List<CedanteTraite> getAllCedanteTraites();
}
