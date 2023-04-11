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
        Float resp = repRepo.getRepartitionsByAffId(affId);
        resp = resp == null ? 0 : resp;
        return resp  - affRepo.getCapitalInitial(affId);
    }

    @Override
    public Float calculateDejaRepartir(Long affId)
    {
        Float resp = repRepo.getRepartitionsByAffId(affId);
        resp = resp == null ? 0 : resp;
        return resp;
    }

    @Override
    public Float calculateTauxDejaRepartir(Long affId)
    {
        Float resp = this.calculateDejaRepartir(affId);
        resp = resp == null ? 0 : resp;
        return (resp/affRepo.getCapitalInitial(affId))*100;
    }

    @Override
    public Float calculateRestTauxARepartir(Long affId) {
        Float resp = this.calculateTauxDejaRepartir(affId);
        resp = resp == null ? 0 : resp;
        return 100 - resp;
    }
}
