package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceAffaire;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class ServiceAffaireImpl implements IserviceAffaire
{
    private final AffaireRepository affRepo;
    private final RepartitionRepository repRepo;
    @Override
    public Float calculateRestARepartir(Long affId)
    {
        boolean affIdExists = affId != null && affRepo.existsById(affId);
        Float dejaReparti = affId == null || !affIdExists ? 0 : repRepo.getRepartitionsByAffId(affId);
        dejaReparti = dejaReparti == null ? 0 : dejaReparti;
        Float capitalinit = !affIdExists ? 0 : affRepo.getCapitalInitial(affId);
        capitalinit = capitalinit == null ? 0 : capitalinit;

        return  capitalinit - dejaReparti ;
    }

    @Override
    public Float calculateDejaRepartir(Long affId)
    {
        Float dejaReparti = affId == null || !affRepo.existsById(affId) ? 0 : repRepo.getRepartitionsByAffId(affId);
        dejaReparti = dejaReparti == null ? 0 : dejaReparti;
        return dejaReparti;
    }

    @Override
    public Float calculateTauxDejaRepartir(Long affId)
    {
        boolean affIdExists = affId != null && affRepo.existsById(affId);
        Float dejaReparti = !affIdExists ? 0 : repRepo.getRepartitionsByAffId(affId);
        dejaReparti = dejaReparti == null ? 0 : dejaReparti;
        Float capitalinit = !affIdExists ? 0 : affRepo.getCapitalInitial(affId);
        capitalinit = capitalinit == null ? 0 : capitalinit;
        return (dejaReparti/capitalinit)*100;
    }

    @Override
    public Float calculateRestTauxARepartir(Long affId) {
        Float tauxDejaReparti = this.calculateTauxDejaRepartir(affId);
        tauxDejaReparti = tauxDejaReparti == null ? 0 : tauxDejaReparti;
        return 100 - tauxDejaReparti;
    }
}
