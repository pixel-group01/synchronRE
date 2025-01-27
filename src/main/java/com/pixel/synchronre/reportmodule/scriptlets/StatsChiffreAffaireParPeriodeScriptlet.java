package com.pixel.synchronre.reportmodule.scriptlets;

import com.pixel.synchronre.sychronremodule.model.dao.ReglementRepository;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRDefaultScriptlet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component @RequiredArgsConstructor
public class StatsChiffreAffaireParPeriodeScriptlet extends JRDefaultScriptlet
{
    private final ReglementRepository regRepo;
    public BigDecimal calculateMontantEncaisseParPeriode(Long affId, LocalDate debut, LocalDate fin)
    {
        return regRepo.calculateEncaissementBetween(affId, debut, fin);
    }
}
