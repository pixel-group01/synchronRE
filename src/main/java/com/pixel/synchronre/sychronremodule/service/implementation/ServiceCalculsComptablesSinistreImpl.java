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
    public BigDecimal calculateMtARegleBySin(Long sinId)
    {
        BigDecimal mtSinistre = sinRepo.getMtSinistre(sinId);
        mtSinistre = mtSinistre == null ? ZERO : mtSinistre;
        return mtSinistre;
    }

    @Override
    public BigDecimal calculateMtDejaRegleBySin(Long sinId)
    {
        BigDecimal mtDejaRegle = sinRepo.calculateMtDejaRegle(sinId);
        return mtDejaRegle == null ? ZERO : mtDejaRegle;
    }

    @Override
    public BigDecimal calculateResteARegleBySin(Long sinId)
    {
        BigDecimal mtSinistre = sinRepo.getMtSinistre(sinId);
        mtSinistre = mtSinistre == null ? ZERO : mtSinistre;
        return mtSinistre.subtract(this.calculateMtDejaRegleBySin(sinId));
    }

    @Override
    public BigDecimal calculateTauxDeReglementSinistre(Long sinId)
    {
        BigDecimal mtSinistre = this.calculateMtARegleBySin(sinId);
        if(mtSinistre.compareTo(ZERO) == 0 ) throw new AppException("Le montant du sinistre est null");
        return this.calculateMtDejaRegleBySin(sinId)
                .divide(mtSinistre, 2, RoundingMode.HALF_UP)
                .multiply(CENT);
    }



    @Override
    public BigDecimal calculateMtARegleBySinAndCes(Long sinId, Long cesId)
    {
        BigDecimal mtAReglerBySinAndCes = sinRepo.calculateMtAReglerBySinAndCes(sinId, cesId);
        return mtAReglerBySinAndCes == null ? ZERO : mtAReglerBySinAndCes;
    }

    @Override
    public BigDecimal calculateMtDejaReglerBySinAndCes(Long sinId, Long cesId)
    {
        BigDecimal mtDejaReglerBySinAndCes = sinRepo.calculateMtDejaReglerBySinAndCes(sinId, cesId);
        return  mtDejaReglerBySinAndCes == null ? ZERO : mtDejaReglerBySinAndCes;
    }

    @Override
    public BigDecimal calculateRestAReglerBySinAndCes(Long sinId, Long cesId)
    {
        return this.calculateMtDejaReglerBySinAndCes(sinId, cesId)
                .subtract(this.calculateMtDejaReglerBySinAndCes(sinId, cesId));
    }

    @Override
    public BigDecimal calculateTauxDeReglementSinistreBySinAndCes(Long sinId, Long cesId)
    {
        BigDecimal mtAReglerBySinAndCes = this.calculateMtARegleBySinAndCes(sinId, cesId);
        if(mtAReglerBySinAndCes.compareTo(ZERO) == 0) throw new AppException("La part sinistre du cessionnaire est null");
        return this.calculateMtDejaReglerBySinAndCes(sinId, cesId)
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
    public BigDecimal calculateMtSinistreTotalAReglerByCes(Long cesId) {
        return null;
    }

    @Override
    public BigDecimal calculateMtSinistreTotalDejaReglerByCes(Long cesId) {
        return null;
    }

    @Override
    public BigDecimal calculateResteSinistreTotalAReglerByCes(Long cesId) {
        return null;
    }

    @Override
    public BigDecimal calculateTauxDeReglementSinistreByCes(Long cesId) {
        return null;
    }


    @Override
    public BigDecimal calculateMtSinistreTotalARegle(Long exeCode)
    {
        BigDecimal mtTotalSinistreByExercice = sinRepo.calculateMtTotalSinistreByExercice(exeCode);
        return mtTotalSinistreByExercice == null ? ZERO : mtTotalSinistreByExercice;
    }

    @Override
    public BigDecimal calculateMtSinistreTotalDejaRegle(Long exeCode)
    {
        BigDecimal mtTotalSinistreDejaReglerByExercice = sinRepo.calculateMtTotalSinistreDejaReglerByExercice(exeCode);
        return mtTotalSinistreDejaReglerByExercice == null ? ZERO : mtTotalSinistreDejaReglerByExercice;
    }

    @Override
    public BigDecimal calculateResteSinistreTotalARegler(Long exeCode) {
        return this.calculateMtSinistreTotalARegle(exeCode).subtract(this.calculateMtSinistreTotalDejaRegle(exeCode));
    }

    @Override
    public BigDecimal calculateTauxDeReglementSinistreByExercice(Long exeCode) {
        BigDecimal mtTotalSinistreByExercice = this.calculateMtSinistreTotalARegle(exeCode);
        if(mtTotalSinistreByExercice.compareTo(ZERO) == 0) throw new AppException("La montant total des sinistres de l'exercice est null");
        return this.calculateMtSinistreTotalDejaRegle(exeCode).divide(mtTotalSinistreByExercice, 2, RoundingMode.HALF_UP)
                .multiply(CENT);

    }



    @Override
    public BigDecimal calculateMtSinistreTotalAReglerByCes(Long cesId, Long exeCode)
    {
        BigDecimal MtTotalARegleByCes = sinRepo.calculateMtSinistreAReglerByCesAndExercice(cesId, exeCode);
        return MtTotalARegleByCes == null ? ZERO : MtTotalARegleByCes;
    }
    @Override
    public BigDecimal calculateMtSinistreTotalDejaReglerByCes(Long cesId, Long exeCode) {
        BigDecimal MtTotalARegleByCes = sinRepo.calculateMtSinistreDejaRegleByCesAndExercice(cesId, exeCode);
        return MtTotalARegleByCes == null ? ZERO : MtTotalARegleByCes;
    }

    @Override
    public BigDecimal calculateResteSinistreTotalAReglerByCes(Long cesId, Long exeCode) {
        return this.calculateMtSinistreTotalAReglerByCes(cesId, exeCode).subtract(this.calculateMtSinistreTotalDejaReglerByCes(cesId, exeCode));
    }

    @Override
    public BigDecimal calculateTauxDeReglementSinistreByCes(Long cesId, Long exeCode)
    {
        BigDecimal MtTotalARegleByCesAndExercice = this.calculateMtSinistreTotalAReglerByCes(cesId, exeCode);
        if(MtTotalARegleByCesAndExercice == null) throw new AppException("Le montant sinistre total à regler par le cessionnaire au cours de l'exercice " + exeCode + " est null");

        return this.calculateMtSinistreTotalDejaReglerByCes(cesId, exeCode)
                .divide(MtTotalARegleByCesAndExercice, 2, RoundingMode.HALF_UP)
                .multiply(CENT);
    }
}
