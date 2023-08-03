package com.pixel.synchronre.sychronremodule.service.interfac;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

public interface IServiceCalculsComptables
{
    BigDecimal calculateRestARepartir(Long affId);

    BigDecimal calculateDejaRepartir(Long affId);


    BigDecimal calculateTauxDejaRepartir(Long affId);
    BigDecimal calculateTauxRestARepartir(Long affId);

    BigDecimal calculateDejaRegle(Long affId);
    BigDecimal calculateRestARegler(Long affId);

    BigDecimal calculateMtTotalAReverseAuxCes(Long affId);
    BigDecimal calculateDejaReverse(Long affId);
    BigDecimal calculateRestAReverser(Long affId);
    public BigDecimal calculateMtEnAttenteDeAReversement(Long affId);
    BigDecimal calculateTauxDeReversement(Long affId);

    BigDecimal calculateDejaReverseByCes(Long plaId);
    BigDecimal calculateRestAReverserbyCes(Long plaId);
    BigDecimal calculateTauxDeReversementByCes(Long plaId);


    BigDecimal calculateMtPrimeNetteByCes(Long plaId); //Prime nette du aux cessionnaires
    BigDecimal calculateMtTotaleCms(Long affId);
    BigDecimal calculateMtCmsByCes(Long plaId);
    BigDecimal calculateMtCmsCedByCes(Long plaId);
    BigDecimal calculateMtTotaleCmsCed(Long affId);

    BigDecimal calculateMtCmsCourtageByCes(Long plaId);
    BigDecimal calculateMtTotalCmsCourtage(Long affId);
    BigDecimal calculateTauxDeReglement(Long affId);


    BigDecimal calculateRestARepartir(Long affId, Long repIdToExclude);
    BigDecimal calculateDejaRepartir(Long affId, Long repIdToExclude);
}
