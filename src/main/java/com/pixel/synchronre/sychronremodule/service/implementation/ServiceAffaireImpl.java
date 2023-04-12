package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceAffaire;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service @RequiredArgsConstructor
public class ServiceAffaireImpl implements IserviceAffaire
{
    private final AffaireRepository affRepo;
    private final RepartitionRepository repRepo;
    @Override
    public BigDecimal calculateRestARepartir(Long affId)
    {
        boolean affIdExists = affId != null && affRepo.existsById(affId);
        BigDecimal dejaReparti = affId == null || !affIdExists ? new BigDecimal(0) : repRepo.getRepartitionsByAffId(affId);
        dejaReparti = dejaReparti == null ?  new BigDecimal(0) : dejaReparti;
        BigDecimal capitalinit = !affIdExists ?  new BigDecimal(0) : affRepo.getCapitalInitial(affId);
        capitalinit = capitalinit == null ?  new BigDecimal(0) : capitalinit;

        return  capitalinit.subtract(dejaReparti) ;
    }

    @Override
    public BigDecimal calculateDejaRepartir(Long affId)
    {
        BigDecimal dejaReparti = affId == null || !affRepo.existsById(affId) ? new BigDecimal(0) : repRepo.getRepartitionsByAffId(affId);
        dejaReparti = dejaReparti == null ? new BigDecimal(0) : dejaReparti;
        return dejaReparti;
    }

    @Override
    public BigDecimal calculateTauxDejaRepartir(Long affId)
    {
        boolean affIdExists = affId != null && affRepo.existsById(affId);
        BigDecimal dejaReparti = !affIdExists ? new BigDecimal(0) : repRepo.getRepartitionsByAffId(affId);
        dejaReparti = dejaReparti == null ? new BigDecimal(0) : dejaReparti;
        BigDecimal capitalinit = !affIdExists ? new BigDecimal(0) : affRepo.getCapitalInitial(affId);
        capitalinit = capitalinit == null ? new BigDecimal(0) : capitalinit;
        return dejaReparti.multiply(new BigDecimal(100)).divide(capitalinit);
    }

    @Override
    public BigDecimal calculateRestTauxARepartir(Long affId) {
        BigDecimal tauxDejaReparti = this.calculateTauxDejaRepartir(affId);
        tauxDejaReparti = tauxDejaReparti == null ? new BigDecimal(0) : tauxDejaReparti;
        return new BigDecimal(100).subtract(tauxDejaReparti);
    }
}