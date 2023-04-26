package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sychronremodule.model.dao.AffaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.ReglementRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptables;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

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
        BigDecimal dejaReparti = affId == null || !affIdExists ? ZERO : repRepo.getRepartitionsByAffId(affId);
        dejaReparti = dejaReparti == null ? ZERO : dejaReparti;
        BigDecimal capitalinit = !affIdExists ? ZERO : affRepo.getCapitalInitial(affId);
        capitalinit = capitalinit == null ? ZERO : capitalinit;
        return  capitalinit.subtract(dejaReparti) ;
    }

    @Override
    public BigDecimal calculateTauxDejaRepartir(Long affId)
    {
        boolean affIdExists = affId != null && affRepo.existsById(affId);
        BigDecimal dejaReparti = !affIdExists ? ZERO : repRepo.getRepartitionsByAffId(affId);
        dejaReparti = dejaReparti == null ? ZERO : dejaReparti;
        BigDecimal capitalinit = !affIdExists ? ZERO : affRepo.getCapitalInitial(affId);
        capitalinit = capitalinit == null ? ZERO : capitalinit;
        return dejaReparti.multiply(CENT).divide(capitalinit, 2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateRestTauxARepartir(Long affId) {
        BigDecimal tauxDejaReparti = this.calculateTauxDejaRepartir(affId);
        tauxDejaReparti = tauxDejaReparti == null ? ZERO : tauxDejaReparti;
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
        BigDecimal primeTotale = affRepo.getFacPrime(affId);
        primeTotale = primeTotale == null ? ZERO : primeTotale;
        return primeTotale.compareTo(ZERO) == 0 ? ZERO : primeTotale.subtract(this.calculateMtTotaleCms(affId));
    }

    @Override
    public BigDecimal calculateMtPrimeNetteByCes(Long affId, Long cesId) //Prime nette du aux cessionnaires
    {
        BigDecimal primeTotale = affRepo.getFacPrime(affId);
        BigDecimal tauxRep = repRepo.getTauRep(affId, cesId);
        BigDecimal tauxCms = repRepo.getTauxCms(affId, cesId);

        primeTotale = primeTotale == null ? ZERO : primeTotale;
        tauxRep = tauxRep == null ? ZERO : tauxRep;
        tauxCms = tauxCms == null ? ZERO : tauxCms;

        return primeTotale.multiply(tauxRep).multiply(CENT.subtract(tauxCms));
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
        BigDecimal sousCommissionTotal = !affIdExists ? ZERO : regRepo.getMtTotalSousCommission(affId);
        return  sousCommissionTotal == null ? ZERO : sousCommissionTotal;
    }

    @Override
    public BigDecimal calculateMtCmsByCes(Long affId, Long cesId)//Montant de la commission du à la Cedante et au réassureur propriétaire de l'affaire (NelsonRE) sur chaque cessionnaire
    {
        boolean affIdExists = affId != null && affRepo.existsById(affId);
        BigDecimal mtSousCommission = !affIdExists ? ZERO : regRepo.getMtSousCommissionByCessionnaire(affId, cesId);
        return  mtSousCommission == null ? ZERO : mtSousCommission;
    }

    @Override
    public BigDecimal calculateMtCmsCedByCes(Long affId, Long cesId)
    {
        Repartition placement = repRepo.getPlacementByAffIdAndCesId(affId, cesId);
        if(placement == null) return BigDecimal.ZERO;
        BigDecimal mtCommission = this.calculateMtCmsByCes(affId, cesId);

        BigDecimal tauxCommission = repRepo.getTauxSousCommission(affId, cesId); //Taux de commission accepté le cessionnaire
        tauxCommission = tauxCommission == null ? ZERO : tauxCommission;
        BigDecimal tauxComCourt = placement.getRepTauxComCourt(); //Taux de commission de NelsonRe
        tauxComCourt = tauxComCourt == null ? ZERO : tauxComCourt;

        return  mtCommission.multiply(tauxCommission.subtract(tauxComCourt));
    }

    @Override
    public BigDecimal calculateMtTotaleCmsCed(Long affId) //Montant totale de la commission cedante
    {
        Set<Long> cesIds = repRepo.getCesIdsByAffId(affId);
        cesIds = cesIds == null ? new HashSet<>() : cesIds;
        return cesIds.stream()
                .map(cesId->this.calculateMtCmsCedByCes(affId, cesId))
                .reduce(ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal calculateMtCmsCourtageByCes(Long affId, Long cesId) //Montant de la commission du réassureur propriétaire de l'affaire (NelsonRE) par cessionnaire
    {
        Repartition placement = repRepo.getPlacementByAffIdAndCesId(affId, cesId);
        BigDecimal mtCms = this.calculateMtCmsByCes(affId, cesId);
        BigDecimal tauxCmsCourt = placement.getRepTauxComCourt();
        tauxCmsCourt = tauxCmsCourt == null ? new BigDecimal(0) : tauxCmsCourt;
        return mtCms.multiply(tauxCmsCourt);
    }

    @Override
    public BigDecimal calculateMtTotalCmsCourtage(Long affId) //Montant total de la commission du réassureur proproétaire de l'affaire (NelsonRE)
    {
        Set<Long> cesIds = repRepo.getCesIdsByAffId(affId);
        cesIds = cesIds == null ? new HashSet<>() : cesIds;
        return cesIds.stream()
                .map(cesId->this.calculateMtCmsCourtageByCes(affId, cesId))
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
        return CENT.multiply(this.calculateDejaReverse(affId).divide(this.calculateMtTotalAReverseAuxCes(affId), 2, RoundingMode.HALF_UP));
    }
}