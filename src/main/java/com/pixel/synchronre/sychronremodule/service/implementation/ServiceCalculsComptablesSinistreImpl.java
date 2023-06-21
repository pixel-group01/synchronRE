package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.SinRepo;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptablesSinistre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.BigDecimal.ZERO;

@Service @RequiredArgsConstructor
public class ServiceCalculsComptablesSinistreImpl implements IServiceCalculsComptablesSinistre
{
    private final SinRepo sinRepo;
    private final BigDecimal CENT = new BigDecimal(100);


    @Override
    public BigDecimal calculateMtAPayerBySin(Long sinId)
    {
        BigDecimal mtSinistre = sinRepo.getMtSinistre(sinId);
        mtSinistre = mtSinistre == null ? ZERO : mtSinistre;
        return mtSinistre;
    }

    @Override
    public BigDecimal calculateMtDejaPayeBySin(Long sinId)
    {
        BigDecimal mtDejaRegle = sinRepo.calculateMtDejaPayeBySin(sinId);
        return mtDejaRegle == null ? ZERO : mtDejaRegle;
    }

    @Override
    public BigDecimal calculateResteAPayerBySin(Long sinId)
    {
        BigDecimal mtSinistre = sinRepo.getMtSinistre(sinId);
        mtSinistre = mtSinistre == null ? ZERO : mtSinistre;
        return mtSinistre.subtract(this.calculateMtDejaPayeBySin(sinId));
    }

    @Override
    public BigDecimal calculateTauxDePaiementSinistre(Long sinId)
    {
        BigDecimal mtSinistre = this.calculateMtAPayerBySin(sinId);
        if(mtSinistre.compareTo(ZERO) == 0 ) throw new AppException("Le montant du sinistre est null");
        return this.calculateMtDejaPayeBySin(sinId)
                .divide(mtSinistre, 2, RoundingMode.HALF_UP)
                .multiply(CENT);
    }



    @Override
    public BigDecimal calculateMtAPayerBySinAndCes(Long sinId, Long cesId)
    {
        BigDecimal mtAPayerBySinAndCes = sinRepo.calculateMtAPayerBySinAndCes(sinId, cesId);
        return mtAPayerBySinAndCes == null ? ZERO : mtAPayerBySinAndCes;
    }

    @Override
    public BigDecimal calculateMtDejaPayeBySinAndCes(Long sinId, Long cesId)
    {
        BigDecimal mtDejaReglerBySinAndCes = sinRepo.calculateMtDejaReglerBySinAndCes(sinId, cesId);
        return  mtDejaReglerBySinAndCes == null ? ZERO : mtDejaReglerBySinAndCes;
    }

    @Override
    public BigDecimal calculateResteAPayerBySinAndCes(Long sinId, Long cesId)
    {
        return this.calculateMtAPayerBySinAndCes(sinId, cesId)
                .subtract(this.calculateMtDejaPayeBySinAndCes(sinId, cesId));
    }

    @Override
    public BigDecimal calculateTauxDePaiementSinistreBySinAndCes(Long sinId, Long cesId)
    {
        BigDecimal mtAReglerBySinAndCes = this.calculateMtAPayerBySinAndCes(sinId, cesId);
        if(mtAReglerBySinAndCes.compareTo(ZERO) == 0) throw new AppException("La part sinistre du cessionnaire est null");
        return this.calculateMtDejaPayeBySinAndCes(sinId, cesId)
                .divide(mtAReglerBySinAndCes, 2, RoundingMode.HALF_UP)
                .multiply(CENT);
    }

    @Override
    public BigDecimal calculateMtSinistreTotalAReverserByCed(Long cedId)
    {
        BigDecimal mtSinistreTotalAReverserByCed = sinRepo.calculateMtSinistreTotalAReverserByCed(cedId);
        return mtSinistreTotalAReverserByCed == null ? ZERO : mtSinistreTotalAReverserByCed;
    }

    @Override
    public BigDecimal calculateMtSinistreTotalDejaReverseByCed(Long cedId) {
        BigDecimal mtSinistreTotalDejaReverserByCed = sinRepo.calculateMtSinistreTotalDejaReverserByCed(cedId);
        return mtSinistreTotalDejaReverserByCed == null ? ZERO : mtSinistreTotalDejaReverserByCed;
    }

    @Override
    public BigDecimal calculateResteSinistreTotalAReverserByCed(Long cedId)
    {
        return this.calculateMtSinistreTotalAReverserByCed(cedId).subtract(this.calculateMtSinistreTotalDejaReverseByCed(cedId));
    }

    @Override
    public BigDecimal calculateTauxDeReversementSinistreByCed(Long cedId)
    {
        BigDecimal mtSinistreTotalAReverserByCed = this.calculateMtSinistreTotalAReverserByCed(cedId);
        if(mtSinistreTotalAReverserByCed.compareTo(ZERO) == 0) throw new AppException("Le montant total sinistre à reverser à la cédante est null");
        return this.calculateMtSinistreTotalDejaReverseByCed(cedId)
                .divide(mtSinistreTotalAReverserByCed, 2, RoundingMode.HALF_UP).multiply(CENT);
    }

    @Override
    public BigDecimal calculateMtSinistreTotalAReverserByCed(Long cedId, Long exeCode) {
        return null;
    }

    @Override
    public BigDecimal calculateMtSinistreTotalDejaReverseByCed(Long cedId, Long exeCode) {
        return null;
    }

    @Override
    public BigDecimal calculateResteSinistreTotalAReverserByCed(Long cedId, Long exeCode) {
        return null;
    }

    @Override
    public BigDecimal calculateTauxDeReversementSinistreByCed(Long cedId, Long exeCode) {
        return null;
    }


    @Override
    public BigDecimal calculateMtSinistreTotalDejaReverseBySin(Long sinId)
    {
        BigDecimal mtDejaReverse = sinRepo.calculateMtDejaReverseBySin(sinId);
        return mtDejaReverse == null ? ZERO : mtDejaReverse;
    }

    @Override
    public BigDecimal calculateMtSinistreEnAttenteDeAReversement(Long sinId)
    {
        BigDecimal mtDejaPaye = sinRepo.calculateMtDejaPayeBySin(sinId);
        BigDecimal mtDejaReverse = sinRepo.calculateMtDejaReverseBySin(sinId);
        return mtDejaPaye == null ? ZERO : mtDejaPaye.subtract(mtDejaReverse == null ? ZERO : mtDejaReverse);
    }

    @Override
    public BigDecimal calculateMtSinistreTotalAPayerByCes(Long cesId) {
        return null;
    }

    @Override
    public BigDecimal calculateMtSinistreTotalDejaPayeByCes(Long cesId) {
        return null;
    }

    @Override
    public BigDecimal calculateResteSinistreTotalAPayerByCes(Long cesId) {
        return null;
    }

    @Override
    public BigDecimal calculateTauxDePaiementSinistreByCes(Long cesId) {
        return null;
    }


    @Override
    public BigDecimal calculateMtSinistreTotalAPayer(Long exeCode)
    {
        BigDecimal mtTotalSinistreByExercice = sinRepo.calculateMtTotalSinistreByExercice(exeCode);
        return mtTotalSinistreByExercice == null ? ZERO : mtTotalSinistreByExercice;
    }

    @Override
    public BigDecimal calculateMtSinistreTotalDejaPaye(Long exeCode)
    {
        BigDecimal mtTotalSinistreDejaReglerByExercice = sinRepo.calculateMtTotalSinistreDejaReglerByExercice(exeCode);
        return mtTotalSinistreDejaReglerByExercice == null ? ZERO : mtTotalSinistreDejaReglerByExercice;
    }

    @Override
    public BigDecimal calculateResteSinistreTotalAPayer(Long exeCode) {
        return this.calculateMtSinistreTotalAPayer(exeCode).subtract(this.calculateMtSinistreTotalDejaPaye(exeCode));
    }

    @Override
    public BigDecimal calculateTauxDePaiementSinistreByExercice(Long exeCode) {
        BigDecimal mtTotalSinistreByExercice = this.calculateMtSinistreTotalAPayer(exeCode);
        if(mtTotalSinistreByExercice.compareTo(ZERO) == 0) throw new AppException("La montant total des sinistres de l'exercice est null");
        return this.calculateMtSinistreTotalDejaPaye(exeCode).divide(mtTotalSinistreByExercice, 2, RoundingMode.HALF_UP)
                .multiply(CENT);

    }



    @Override
    public BigDecimal calculateMtSinistreTotalAPayerByCes(Long cesId, Long exeCode)
    {
        BigDecimal MtTotalARegleByCes = sinRepo.calculateMtSinistreAReglerByCesAndExercice(cesId, exeCode);
        return MtTotalARegleByCes == null ? ZERO : MtTotalARegleByCes;
    }
    @Override
    public BigDecimal calculateMtSinistreTotalDejaPayeByCes(Long cesId, Long exeCode) {
        BigDecimal MtTotalARegleByCes = sinRepo.calculateMtSinistreDejaRegleByCesAndExercice(cesId, exeCode);
        return MtTotalARegleByCes == null ? ZERO : MtTotalARegleByCes;
    }

    @Override
    public BigDecimal calculateResteSinistreTotalAPayerByCes(Long cesId, Long exeCode) {
        return this.calculateMtSinistreTotalAPayerByCes(cesId, exeCode).subtract(this.calculateMtSinistreTotalDejaPayeByCes(cesId, exeCode));
    }

    @Override
    public BigDecimal calculateTauxDePaiementSinistreByCes(Long cesId, Long exeCode)
    {
        BigDecimal MtTotalARegleByCesAndExercice = this.calculateMtSinistreTotalAPayerByCes(cesId, exeCode);
        if(MtTotalARegleByCesAndExercice == null) throw new AppException("Le montant sinistre total à regler par le cessionnaire au cours de l'exercice " + exeCode + " est null");

        return this.calculateMtSinistreTotalDejaPayeByCes(cesId, exeCode)
                .divide(MtTotalARegleByCesAndExercice, 2, RoundingMode.HALF_UP)
                .multiply(CENT);
    }
}