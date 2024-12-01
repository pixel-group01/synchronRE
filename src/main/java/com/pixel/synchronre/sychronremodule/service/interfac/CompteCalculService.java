package com.pixel.synchronre.sychronremodule.service.interfac;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import static java.math.BigDecimal.ZERO;

@Service @RequiredArgsConstructor
public class CompteCalculService implements ICompteCalculService
{
    @Override //TODO à implémenter
    public BigDecimal calculatePrimeCessionnaireOnCompte(Long compteCedId, Long cesId)
    {
        return ZERO;
    }
}