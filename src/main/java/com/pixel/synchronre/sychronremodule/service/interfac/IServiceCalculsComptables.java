package com.pixel.synchronre.sychronremodule.service.interfac;

import java.math.BigDecimal;

public interface IServiceCalculsComptables
{
    BigDecimal calculateRestARepartir(Long affId);

    BigDecimal calculateDejaRepartir(Long affId);

    BigDecimal calculateTauxDejaRepartir(Long affId);
    BigDecimal calculateTauxRestARepartir(Long affId);

    BigDecimal calculateDejaRegle(Long affId);
    BigDecimal calculateRestARegler(Long affId);

    BigDecimal calculateDejaReverse(Long affId);

    BigDecimal calculateMtTotalAReverseAuxCes(Long affId);

    BigDecimal calculateMtPrimeNetteByCes(Long plaId) //Prime nette du aux cessionnaires
    ;

    BigDecimal calculateRestAReverser(Long affId);

    BigDecimal calculateMtTotaleCms(Long affId);

    BigDecimal calculateMtCmsByCes(Long plaId);

    BigDecimal calculateMtCmsCedByCes(Long plaId);
    BigDecimal calculateMtTotaleCmsCed(Long affId);

    BigDecimal calculateMtCmsCourtageByCes(Long plaId);
    BigDecimal calculateMtTotalCmsCourtage(Long affId);

    BigDecimal calculateTauxDeReglement(Long affId);

    BigDecimal calculateTauxDeReversement(Long affId);


}
