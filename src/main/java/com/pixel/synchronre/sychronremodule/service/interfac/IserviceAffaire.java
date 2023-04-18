package com.pixel.synchronre.sychronremodule.service.interfac;

import java.math.BigDecimal;

public interface IserviceAffaire {
    BigDecimal calculateRestARepartir(Long affId);

    BigDecimal calculateDejaRepartir(Long affId);

    BigDecimal calculateTauxDejaRepartir(Long affId);
    BigDecimal calculateRestTauxARepartir(Long affId);
}
