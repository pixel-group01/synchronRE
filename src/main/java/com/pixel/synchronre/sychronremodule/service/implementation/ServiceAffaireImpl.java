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
    public float calculateRestARepartir(Long affId)
    {
        return repRepo.getRepartitionsByAffId(affId) - affRepo.getCapitalInitial(affId);
    }

    @Override
    public float calculateDejaRepartir(Long affId) {
        return repRepo.getRepartitionsByAffId(affId);
    }

    @Override
    public float calculateTauxDejaRepartir(Long affId)
    {
        return (this.calculateDejaRepartir(affId)/affRepo.getCapitalInitial(affId))*100;
    }

    @Override
    public float calculateRestTauxARepartir(Long affId) {
        return 100 - this.calculateTauxDejaRepartir(affId);
    }
}
