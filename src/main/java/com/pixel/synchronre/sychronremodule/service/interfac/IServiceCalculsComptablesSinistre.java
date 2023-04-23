package com.pixel.synchronre.sychronremodule.service.interfac;

import java.math.BigDecimal;

public interface IServiceCalculsComptablesSinistre
{
    BigDecimal calculateDejaReverse(Long affId);
    BigDecimal calculateMtTotalAReverseAuxCed(Long affId);

    //========================================================

    BigDecimal calculateMtTotalARegle(Long courtierId);
    BigDecimal calculateMtARegleBySin(Long sinId);
    BigDecimal calculateMtTotalARegleByCes(Long cesId);
    BigDecimal calculateMtARegleBySinAndCes(Long sinId, Long cesId);

    BigDecimal calculateMtTotalDejaRegle(Long courtierId);
    BigDecimal calculateMtDejaRegleBySin(Long sinId);
    BigDecimal calculateMtTotalDejaRegleByCes(Long cesId);
    BigDecimal calculateMtDejaReglerBySinAndCes(Long sinId, Long cesId);

    BigDecimal calculateResteTotalARegler(Long courtierId);
    BigDecimal calculateResteARegleBySin(Long sinId);
    BigDecimal calculateResteTotalARegleByCes(Long cesId);
    BigDecimal calculateRestAReglerBySinAndCes(Long sinId, Long cesId);





    BigDecimal calculateMtAReglerByCes(Long affId, Long cesId) //Prime nette du aux cessionnaires
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
