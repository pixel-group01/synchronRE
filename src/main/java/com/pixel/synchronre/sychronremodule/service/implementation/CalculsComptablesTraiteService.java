package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sychronremodule.model.dao.CompteTraiteRepo;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptablesTraite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service @RequiredArgsConstructor
public class CalculsComptablesTraiteService implements IServiceCalculsComptablesTraite
{
    private final CompteTraiteRepo ctRepo;
    private final BigDecimal CENT = new BigDecimal(100);
    @Override
    public BigDecimal calculateTauxRestantARepartir(Long traiNPId)
    {
        return CENT.subtract(calculateTauxDejaReparti(traiNPId));
    }

    @Override
    public BigDecimal calculateTauxDejaReparti(Long traiNPId)
    {
        BigDecimal dejaReparti = ctRepo.calculateTauxDejaReparti(traiNPId);
        return dejaReparti == null ? BigDecimal.ZERO : dejaReparti;
    }
}