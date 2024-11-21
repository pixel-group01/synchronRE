package com.pixel.synchronre.sychronremodule.service.listeners;

import com.pixel.synchronre.sychronremodule.model.entities.TraiteNonProportionnel;
import com.pixel.synchronre.sychronremodule.model.entities.TrancheCedante;
import com.pixel.synchronre.sychronremodule.model.events.TrancheCedanteEvent;
import com.pixel.synchronre.sychronremodule.model.events.SimpleEvent;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

public interface IRepartitionTnpListener
{
    void onRemoveCedanteFromTraiteEvent(SimpleEvent<TrancheCedante> event);

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void onAddOrUpdateCedanteToTraiteEvent(TrancheCedanteEvent event);

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void onTauxCourtierChangeFromTnp(SimpleEvent<TraiteNonProportionnel> event);
}
