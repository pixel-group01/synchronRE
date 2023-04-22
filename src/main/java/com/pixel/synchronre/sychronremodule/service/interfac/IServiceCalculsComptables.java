package com.pixel.synchronre.sychronremodule.service.interfac;

import java.math.BigDecimal;

public interface IServiceCalculsComptables
{
    BigDecimal calculateRestARepartir(Long affId);

    BigDecimal calculateDejaRepartir(Long affId);

    BigDecimal calculateTauxDejaRepartir(Long affId);
    BigDecimal calculateRestTauxARepartir(Long affId);

    BigDecimal calculateDejaRegle(Long affId);
    BigDecimal calculateRestARegler(Long affId);

    BigDecimal calculateDejaReverse(Long affId);

    BigDecimal calculateMtTotalAReverseAuxCes(Long affId);

    BigDecimal calculateMtPrimeNetteByCes(Long affId, Long cesId) //Prime nette du aux cessionnaires
    ;

    BigDecimal calculateRestAReverser(Long affId);

    BigDecimal calculateMtTotaleCms(Long affId);

    BigDecimal calculateMtCmsByCes(Long affId, Long cesId);

    BigDecimal calculateMtCmsCedByCes(Long affId, Long cesId);
    BigDecimal calculateMtTotaleCmsCed(Long affId);

    BigDecimal calculateMtCmsReaOwnerByCes(Long affId, Long cesId);
    BigDecimal calculateMtTotalCmsReaOwner(Long affId);

    BigDecimal calculateTauxDeReglement(Long affId);

    BigDecimal calculateTauxDeReversement(Long affId);


}
