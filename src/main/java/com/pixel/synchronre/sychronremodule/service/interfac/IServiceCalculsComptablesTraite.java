package com.pixel.synchronre.sychronremodule.service.interfac;

import java.math.BigDecimal;

public interface IServiceCalculsComptablesTraite
{
    BigDecimal calculateTauxRestantARepartir(Long traiNPId);
    BigDecimal calculateTauxDejaReparti(Long traiNPId);
}
