package com.pixel.synchronre.sychronremodule.service.interfac;

import java.math.BigDecimal;

public interface IServiceCalculsComptablesTraite
{
    BigDecimal calculateTauxRestantAPlacer(Long traiNPId);
    BigDecimal calculateTauxDejaPlace(Long traiNPId);
}
