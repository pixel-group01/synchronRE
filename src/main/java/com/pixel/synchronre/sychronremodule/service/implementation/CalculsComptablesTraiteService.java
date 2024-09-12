package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sychronremodule.model.dao.CompteTraiteRepo;
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
    public BigDecimal calculateTauxRestantAPlacer(Long traiNPId)
    {
        return CENT.subtract(calculateTauxDejaPlace(traiNPId));
    }

    @Override
    public BigDecimal calculateTauxDejaPlace(Long traiNPId)
    {
        BigDecimal dejaReparti = ctRepo.calculateTauxDejaPlace(traiNPId);
        return dejaReparti == null ? BigDecimal.ZERO : dejaReparti;
    }
}