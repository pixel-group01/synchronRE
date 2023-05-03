package com.pixel.synchronre.sychronremodule.service.interfac;

import java.math.BigDecimal;

public interface IServiceCalculsComptablesSinistre
{
    BigDecimal calculateMtSinistreTotalARegle(Long exeCode);
    BigDecimal calculateMtSinistreTotalDejaRegle(Long exeCode);
    BigDecimal calculateResteSinistreTotalARegler(Long exeCode);
    BigDecimal calculateTauxDeReglementSinistreByExercice(Long exeCode);

    BigDecimal calculateMtARegleBySin(Long sinId);
    BigDecimal calculateMtDejaRegleBySin(Long sinId);
    BigDecimal calculateResteARegleBySin(Long sinId);
    BigDecimal calculateTauxDeReglementSinistre(Long sinId);


    BigDecimal calculateMtARegleBySinAndCes(Long sinId, Long cesId);
    BigDecimal calculateMtDejaReglerBySinAndCes(Long sinId, Long cesId);
    BigDecimal calculateRestAReglerBySinAndCes(Long sinId, Long cesId);
    BigDecimal calculateTauxDeReglementSinistreBySinAndCes(Long sinId, Long cesId);


    BigDecimal calculateMtSinistreTotalAReverserByCed(Long cedId);
    BigDecimal calculateMtSinistreTotalDejaReverseByCed(Long cedId);
    BigDecimal calculateResteSinistreTotalAReverserByCed(Long cedId);
    BigDecimal calculateTauxDeReversementSinistreByCed(Long cedId);

    BigDecimal calculateMtSinistreTotalAReverserByCed(Long cedId, Long exeCode);
    BigDecimal calculateMtSinistreTotalDejaReverseByCed(Long cedId, Long exeCode);
    BigDecimal calculateResteSinistreTotalAReverserByCed(Long cedId, Long exeCode);
    BigDecimal calculateTauxDeReversementSinistreByCed(Long cedId, Long exeCode);


    BigDecimal calculateMtSinistreTotalAReglerByCes(Long cesId);
    BigDecimal calculateMtSinistreTotalDejaReglerByCes(Long cesId);
    BigDecimal calculateResteSinistreTotalAReglerByCes(Long cesId);
    BigDecimal calculateTauxDeReglementSinistreByCes(Long cesId);

    BigDecimal calculateMtSinistreTotalAReglerByCes(Long cesId, Long exeCode);
    BigDecimal calculateMtSinistreTotalDejaReglerByCes(Long cesId, Long exeCode);
    BigDecimal calculateResteSinistreTotalAReglerByCes(Long cesId, Long exeCode);
    BigDecimal calculateTauxDeReglementSinistreByCes(Long cesId, Long exeCode);
}
