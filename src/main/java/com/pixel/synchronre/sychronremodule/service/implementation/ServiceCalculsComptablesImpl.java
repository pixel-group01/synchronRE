package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.ReglementRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptables;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor @Service
public class ServiceCalculsComptablesImpl implements IServiceCalculsComptables
{
    private final AffaireRepository affRepo;
    private final RepartitionRepository repRepo;
    private final ReglementRepository regRepo;
    private final BigDecimal ZERO = BigDecimal.ZERO;
    private final BigDecimal CENT = new BigDecimal(100);

    @Override
    public BigDecimal calculateDejaRepartir(Long affId)
    {
        BigDecimal dejaReparti = affId == null || !affRepo.existsById(affId) ? ZERO : repRepo.getRepartitionsByAffId(affId);
        dejaReparti = dejaReparti == null ? ZERO : dejaReparti;
        return dejaReparti;
    }

    @Override
    public BigDecimal calculateRestARepartir(Long affId)
    {
        boolean affIdExists = affId != null && affRepo.existsById(affId);
        BigDecimal dejaReparti = !affIdExists ? ZERO : repRepo.getRepartitionsByAffId(affId);
        dejaReparti = dejaReparti == null ? ZERO : dejaReparti;
        BigDecimal capitalinit = !affIdExists ? ZERO : affRepo.getCapitalInitial(affId);
        capitalinit = capitalinit == null ? ZERO : capitalinit;
        return  capitalinit.subtract(dejaReparti) ;
    }

    @Override
    public BigDecimal calculateTauxDejaRepartir(Long affId)
    {
        boolean affIdExists = affId != null && affRepo.existsById(affId);
        BigDecimal dejaReparti = this.calculateDejaRepartir(affId);
        BigDecimal capitalinit = !affIdExists ? ZERO : affRepo.getCapitalInitial(affId);
        capitalinit = capitalinit == null ? ZERO : capitalinit;
        return dejaReparti.multiply(CENT).divide(capitalinit, 2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateTauxRestARepartir(Long affId) {
        BigDecimal tauxDejaReparti = this.calculateTauxDejaRepartir(affId);
        return CENT.subtract(tauxDejaReparti);
    }

    @Override
    public BigDecimal calculateDejaRegle(Long affId) {
        BigDecimal dejaRegle = affId == null || !affRepo.existsById(affId) ? ZERO : regRepo.getMontantRegleByAffId(affId, "paiements");
        dejaRegle = dejaRegle == null ? ZERO : dejaRegle;
        return dejaRegle;
    }

    @Override
    public BigDecimal calculateRestARegler(Long affId)
    {
        boolean affIdExists = affId != null && affRepo.existsById(affId);
        BigDecimal dejaRegle = this.calculateDejaRegle(affId);
        BigDecimal facprime = !affIdExists ? ZERO : affRepo.getFacPrime(affId);
        facprime = facprime == null ? ZERO : facprime;
        return  facprime.subtract(dejaRegle);
    }

    @Override
    public BigDecimal calculateDejaReverse(Long affId)
    {
        BigDecimal dejaReverse = affId == null || !affRepo.existsById(affId) ? ZERO : regRepo.getMontantRegleByAffId(affId, "reversements");
        dejaReverse = dejaReverse == null ? ZERO : dejaReverse;
        return dejaReverse;
    }

    @Override
    public BigDecimal calculateMtTotalAReverseAuxCes(Long affId) //Prime nette du aux cessionnaires
    {
        List<Long> plaIds = repRepo.getPlaIdsByAffId(affId);
        BigDecimal primeNetteTotale = plaIds.stream().map(plaId->this.calculateMtPrimeNetteByCes(plaId)).reduce(BigDecimal::add).orElse(ZERO);
        return primeNetteTotale;
    }

    @Override
    public BigDecimal calculateMtPrimeNetteByCes(Long plaId) //Prime nette du aux cessionnaires
    {
        BigDecimal primeTotale = repRepo.getFacPrimeTotalByPlaId(plaId);
        BigDecimal tauxRep = repRepo.getTauRep(plaId);
        BigDecimal tauxCms = repRepo.getTauxSousCommission(plaId);

        primeTotale = primeTotale == null ? ZERO : primeTotale;
        tauxRep = tauxRep == null ? ZERO : tauxRep;
        tauxCms = tauxCms == null ? ZERO : tauxCms;

        return primeTotale.multiply(tauxRep.divide(CENT, 2, RoundingMode.HALF_UP))
                .multiply(CENT.subtract(tauxCms).divide(CENT, 2, RoundingMode.HALF_UP));
    }

    @Override
    public BigDecimal calculateRestAReverser(Long affId)
    {
        boolean affIdExists = affId != null && affRepo.existsById(affId);
        BigDecimal dejaReverse = this.calculateDejaReverse(affId);
        BigDecimal mtTotalAReverseAuxCes = this.calculateMtTotalAReverseAuxCes(affId);
        mtTotalAReverseAuxCes = mtTotalAReverseAuxCes == null ? ZERO : mtTotalAReverseAuxCes;
        return  mtTotalAReverseAuxCes.subtract(dejaReverse);
    }



    @Override
    public BigDecimal calculateMtTotaleCms(Long affId) //Montant total de la commission du à la Cedante et au réassureur propriétaire de l'affaire (NelsonRE)
    {
        boolean affIdExists = affId != null && affRepo.existsById(affId);
        BigDecimal sousCommissionTotal = !affIdExists ? ZERO : repRepo.calculateMtTotalSousCommission(affId);
        return  sousCommissionTotal == null ? ZERO : sousCommissionTotal;
    }

    @Override
    public BigDecimal calculateMtCmsByCes(Long plaId)//Montant de la commission du à la Cedante et au réassureur propriétaire de l'affaire (NelsonRE) sur chaque cessionnaire
    {
        boolean plaIdExists = plaId != null && repRepo.placementExists(plaId);
        BigDecimal mtSousCommission = !plaIdExists ? ZERO : repRepo.calculateMtSousCmsByCes(plaId);
        return  mtSousCommission == null ? ZERO : mtSousCommission;
    }

    @Override
    public BigDecimal calculateMtCmsCedByCes(Long plaId) //TODO Revoir les calcul
    {
        Repartition placement = repRepo.findPlacementById(plaId).orElseThrow(()->new AppException("Placement inexistant"));
        BigDecimal mtCommissionCed = repRepo.calculateMtCmsCedByCes(plaId);
        return  mtCommissionCed;
    }

    @Override
    public BigDecimal calculateMtTotaleCmsCed(Long affId) //Montant totale de la commission cedante
    {
        List<Long> plaIds = repRepo.getPlaIdsByAffId(affId);
        plaIds = plaIds == null ? new ArrayList<>() : plaIds;
        return plaIds.stream()
                .map(plaId->this.calculateMtCmsCedByCes(plaId))
                .reduce(ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal calculateMtCmsCourtageByCes(Long plaId) //Montant de la commission du réassureur propriétaire de l'affaire (NelsonRE) par cessionnaire
    {
        Repartition placement = repRepo.findPlacementById(plaId).orElseThrow(()->new AppException("Placement introuvable"));
        return repRepo.calculateMtCmsCourtByCes(plaId);
    }

    @Override
    public BigDecimal calculateMtTotalCmsCourtage(Long affId) //Montant total de la commission du réassureur proproétaire de l'affaire (NelsonRE)
    {
        List<Long> plaIds = repRepo.getPlaIdsByAffId(affId);
        plaIds = plaIds == null ? new ArrayList<>() : plaIds;
        return plaIds.stream()
                .map(plaId->this.calculateMtCmsCourtageByCes(plaId))
                .reduce(ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal calculateTauxDeReglement(Long affId)
    {
        return CENT.multiply(this.calculateDejaRegle(affId).divide(affRepo.getFacPrime(affId), 2, RoundingMode.HALF_UP));
    }

    @Override
    public BigDecimal calculateTauxDeReversement(Long affId)
    {
        BigDecimal mtTotalAReverseAuxCes = this.calculateMtTotalAReverseAuxCes(affId);
        if(mtTotalAReverseAuxCes.compareTo(ZERO) == 0) return CENT;
        return CENT.multiply(this.calculateDejaReverse(affId).divide(mtTotalAReverseAuxCes, 2, RoundingMode.HALF_UP));
    }
}