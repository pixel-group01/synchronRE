package com.pixel.synchronre.sychronremodule.service.listeners;

import com.pixel.synchronre.sychronremodule.model.entities.CedanteTraite;
import com.pixel.synchronre.sychronremodule.model.entities.TraiteNonProportionnel;
import com.pixel.synchronre.sychronremodule.model.events.CedanteTraiteEvent;
import com.pixel.synchronre.sychronremodule.model.events.SimpleEvent;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

public interface IRepartitionTnpListener
{
    void onRemoveCedanteFromTraiteEvent(SimpleEvent<CedanteTraite> event);

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void onAddOrUpdateCedanteToTraiteEvent(CedanteTraiteEvent event);

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void onTauxCourtierChangeFromTnp(SimpleEvent<TraiteNonProportionnel> event);
}
