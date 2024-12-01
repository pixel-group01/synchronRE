package com.pixel.synchronre.sychronremodule.service.interfac;

import java.math.BigDecimal;

public interface ICompteCalculService {
    public BigDecimal calculatePrimeCessionnaireOnCompte(Long compteCedId, Long cesId);
}
