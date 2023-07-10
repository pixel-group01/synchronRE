package com.pixel.synchronre.sychronremodule.service.interfac;

import java.math.BigDecimal;

public interface IServiceCalculsComptablesSinistre
{
    BigDecimal calculateMtSinistreTotalAPayer(Long exeCode);
    BigDecimal calculateMtSinistreTotalDejaPaye(Long exeCode);
    BigDecimal calculateResteSinistreTotalAPayer(Long exeCode);
    BigDecimal calculateTauxDePaiementSinistreByExercice(Long exeCode);

    BigDecimal calculateMtTotalSinistre(Long sinId);

    BigDecimal calculateMtTotalCessionnairesSurSinistre(Long sinId);

    BigDecimal calculateMtDejaPayeBySin(Long sinId);
    BigDecimal calculateResteAPayerBySin(Long sinId);
    BigDecimal calculateTauxDePaiementSinistre(Long sinId);


    BigDecimal calculateMtAPayerBySinAndCes(Long sinId, Long cesId);
    BigDecimal calculateMtDejaPayeBySinAndCes(Long sinId, Long cesId);
    BigDecimal calculateResteAPayerBySinAndCes(Long sinId, Long cesId);
    BigDecimal calculateTauxDePaiementSinistreBySinAndCes(Long sinId, Long cesId);

    BigDecimal calculateMtSinistreTotalAReverserByCed(Long cedId);
    BigDecimal calculateMtSinistreTotalDejaReverseByCed(Long cedId);
    BigDecimal calculateResteSinistreTotalAReverserByCed(Long cedId);
    BigDecimal calculateTauxDeReversementSinistreByCed(Long cedId);


    BigDecimal calculateMtSinistreTotalAPayerByCes(Long cesId);
    BigDecimal calculateMtSinistreTotalDejaPayeByCes(Long cesId);
    BigDecimal calculateResteSinistreTotalAPayerByCes(Long cesId);
    BigDecimal calculateTauxDePaiementSinistreByCes(Long cesId);

    BigDecimal calculateMtSinistreTotalAPayerByCes(Long cesId, Long exeCode);
    BigDecimal calculateMtSinistreTotalDejaPayeByCes(Long cesId, Long exeCode);
    BigDecimal calculateResteSinistreTotalAPayerByCes(Long cesId, Long exeCode);
    BigDecimal calculateTauxDePaiementSinistreByCes(Long cesId, Long exeCode);


    BigDecimal calculateMtSinistreTotalAReverserByCed(Long cedId, Long exeCode);
    BigDecimal calculateMtSinistreTotalDejaReverseByCed(Long cedId, Long exeCode);
    BigDecimal calculateResteSinistreTotalAReverserByCed(Long cedId, Long exeCode);
    BigDecimal calculateTauxDeReversementSinistreByCed(Long cedId, Long exeCode);

    BigDecimal calculateMtSinistreTotalDejaReverseBySin(Long sinId);
    BigDecimal calculateMtSinistreEnAttenteDeAReversement(Long sinId);
}
