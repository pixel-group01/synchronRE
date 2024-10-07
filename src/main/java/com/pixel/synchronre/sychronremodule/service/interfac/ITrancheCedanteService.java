package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteReq;
import com.pixel.synchronre.sychronremodule.model.events.TrancheEvent;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

public interface ITrancheCedanteService
{
    void addTrancheCedantePmd(CedanteTraiteReq dto);

    void updateTrancheCedantePmd(CedanteTraiteReq dto);

    @Transactional
    void onAddOrRemoveCedanteToCategorie(Long cedId, Long catId);

    void onAddOrRemoveCategorieToTranche(Long catId, Long trancheId);

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void onTrancheUpdate(TrancheEvent trancheEvent);
}
