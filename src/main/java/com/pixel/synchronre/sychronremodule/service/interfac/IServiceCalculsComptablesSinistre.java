package com.pixel.synchronre.sychronremodule.service.interfac;

import java.math.BigDecimal;

public interface IServiceCalculsComptablesSinistre
{
    BigDecimal calculateMtTotalARegle(Long courtierId, Long exeCode);
    BigDecimal calculateMtTotalDejaRegle(Long courtierId, Long exeCode);
    BigDecimal calculateResteTotalARegler(Long courtierId, Long exeCode);
    BigDecimal calculateTauxDeReglementSinistreByCourtier(Long courtierId, Long exeCode);

    BigDecimal calculateMtARegleBySin(Long sinId);
    BigDecimal calculateMtDejaRegleBySin(Long sinId);
    BigDecimal calculateResteARegleBySin(Long sinId);
    BigDecimal calculateTauxDeReglementSinistre(Long sinId);



    BigDecimal calculateMtTotalARegleByCes(Long cesId, Long exeCode);
    BigDecimal calculateMtTotalDejaRegleByCes(Long cesId, Long exeCode);
    BigDecimal calculateResteTotalARegleByCes(Long cesId, Long exeCode);
    BigDecimal calculateTauxDeReglementSinistreByCes(Long cesId, Long exeCode);

    BigDecimal calculateMtARegleBySinAndCes(Long sinId, Long cesId);
    BigDecimal calculateMtDejaReglerBySinAndCes(Long sinId, Long cesId);
    BigDecimal calculateRestAReglerBySinAndCes(Long sinId, Long cesId);
    BigDecimal calculateTauxDeReglementSinistreBySinAndCes(Long sinId, Long cesId);


    //BigDecimal calculateMtTotalARegleByCed(Long cedId);
    //BigDecimal calculateMtTotalDejaRegleByCed(Long cedId);
    //BigDecimal calculateResteTotalARegleByCed(Long cedId);
}
