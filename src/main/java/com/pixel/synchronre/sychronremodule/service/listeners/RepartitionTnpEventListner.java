package com.pixel.synchronre.sychronremodule.service.listeners;

import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionTraiteRepo;
import com.pixel.synchronre.sychronremodule.model.dao.TraiteNPRepository;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CesLeg;
import com.pixel.synchronre.sychronremodule.model.dto.traite.response.TauxCourtiersResp;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TranchePmdDto;
import com.pixel.synchronre.sychronremodule.model.entities.CedanteTraite;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import com.pixel.synchronre.sychronremodule.model.entities.TraiteNonProportionnel;
import com.pixel.synchronre.sychronremodule.model.events.CedanteTraiteEvent;
import com.pixel.synchronre.sychronremodule.model.events.SimpleEvent;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceRepartitionTraiteNP;
import com.pixel.synchronre.sychronremodule.service.interfac.ITrancheCedanteService;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceRepartition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.util.List;

import static com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions.ADD_CEDANTE_TO_TRAITE_NP;
import static com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions.UPDATE_CEDANTE_ON_TRAITE_NP;

@Service @RequiredArgsConstructor
public class RepartitionTnpEventListner implements IRepartitionTnpListener
{
    private final RepartitionTraiteRepo rtRepo;
    private final TraiteNPRepository tnpRepo;
    private final IserviceRepartition repFacService;
    private final IServiceRepartitionTraiteNP repTnpService;

    @Override @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onRemoveCedanteFromTraiteEvent(SimpleEvent<CedanteTraite> event)
    {
        System.out.println("============Listening event : " + event.getAction() + "=============");
        if(event == null || event.getData() == null) throw new AppException("Evènement publié sans aucune donnée");
        if(event.getData().getTraiteNonProportionnel() == null || event.getData().getTraiteNonProportionnel().getTraiteNpId() == null)
            throw new AppException("Les données du traité n'ont pas été fournies sur à la publication de l'événement");

        Long cedanteTraiteId = event.getData().getCedanteTraiteId();
        Long traiteNpId = event.getData().getTraiteNonProportionnel().getTraiteNpId();
        //On annule toutes les repartitions de type cession légale liées à cette cedante sur ce traité
        rtRepo.findCesLegIdsByCedTraiId(cedanteTraiteId).forEach(repId-> repFacService.annulerRepartition(repId));

        //On recalcule les montants des primes de tous les placements sur le traité
        recalculateMontantForPlacementsOnTraite(traiteNpId);
    }

    @Override @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onAddOrUpdateCedanteToTraiteEvent(CedanteTraiteEvent event)
    {
        System.out.println("============Listening event : " + event.getAction() + "=============");
        if(event == null || event.getCedanteTraite() == null) throw new AppException("Evènement publié sans aucune donnée");
        if(event.getCedanteTraite().getTraiteNonProportionnel() == null || event.getCedanteTraite().getTraiteNonProportionnel().getTraiteNpId() == null)
            throw new AppException("Les données du traité n'ont par été fournies sur à la publication de l'événement");
        Long cedanteTraiteId = event.getCedanteTraite().getCedanteTraiteId();
        Long traiteNpId = event.getCedanteTraite().getTraiteNonProportionnel().getTraiteNpId();
        List<CesLeg> cesLegs = event.getDto().getCessionsLegales();

        recalculateMontantForPlacementsOnTraite(traiteNpId);
        switch (event.getAction())
        {
            case ADD_CEDANTE_TO_TRAITE_NP ->
            {
                if(cesLegs != null && !cesLegs.isEmpty())
                {
                    cesLegs.stream().filter(cesLeg -> cesLeg.isAccepte()).forEach(cesLeg->repTnpService.createRepartitionCesLegTraite(cesLeg, cedanteTraiteId));
                }
            }
            case UPDATE_CEDANTE_ON_TRAITE_NP ->
            {
                if(cesLegs != null && !cesLegs.isEmpty())
                {
                    cesLegs.stream().filter(cesLeg -> cesLeg.isAccepte()).forEach(cesLeg->repTnpService.updateRepartitionCesLegTraite(cesLeg, cedanteTraiteId));
                    cesLegs.stream().filter(cesLeg -> !cesLeg.isAccepte()).forEach(cesLeg->
                        repTnpService.desactivateCesLegByTraiteNpIdAndPclId(traiteNpId, cesLeg.getParamCesLegalId()));
                }
            }
        }
    }

    @Override @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onTauxCourtierChangeFromTnp(SimpleEvent<TraiteNonProportionnel> event)
    {
        TraiteNonProportionnel tnp = event.getData();
        Long traiteNpId = tnp.getTraiteNpId();

        //On recalcule les montants de prime sur les cessions légales du traité
        rtRepo.findCesLegsByTraiteNpId(traiteNpId).forEach(pclRep->
        {
            CesLeg cesLeg = new CesLeg(pclRep.getRepId(), pclRep.getRepTaux(), tnp.getTraiTauxCourtier(), tnp.getTraiTauxCourtierPlaceur());
            repTnpService.setMontantPrimesForCesLegRep(cesLeg, pclRep);
        });

        //On recalcule les montants de prime sur les placements du traité
        recalculateMontantForPlacementsOnTraite(traiteNpId);
    }

    private void recalculateMontantForPlacementsOnTraite(Long traiteNpId)
    {
        List<Repartition> placements = rtRepo.getValidPlacementsOnTraiteNp(traiteNpId);
        if(placements != null)
        {
            TauxCourtiersResp tauxCourtiers = tnpRepo.getTauxCourtiers(traiteNpId);
            BigDecimal tauxCoutier = tauxCourtiers == null ? BigDecimal.ZERO : tauxCourtiers.getTraiTauxCourtier();
            BigDecimal tauxCourtierPlaceur = tauxCourtiers ==  null ? BigDecimal.ZERO  :  tauxCourtiers.getTraiTauxCourtierPlaceur();
            placements.forEach(placement->repTnpService.setMontantsPrimes(traiteNpId, placement.getRepTaux(), tauxCoutier, tauxCourtierPlaceur, placement));
        }
    }
}