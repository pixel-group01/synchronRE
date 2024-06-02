package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;

import static com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions.*;
import static com.pixel.synchronre.sychronremodule.model.constants.UsualNumbers.CENT;
import com.pixel.synchronre.sychronremodule.model.dao.CedanteTraiteRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionTraiteRepo;
import com.pixel.synchronre.sychronremodule.model.dao.TraiteNPRepository;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CesLeg;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.PmdGlobalResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.RepartitionTraiteNPMapper;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.PlacementTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionTraiteNPResp;
import com.pixel.synchronre.sychronremodule.model.dto.traite.response.TauxCourtiersResp;
import com.pixel.synchronre.sychronremodule.model.entities.CedanteTraite;
import com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import com.pixel.synchronre.sychronremodule.model.events.GenereicEvent;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptablesTraite;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceRepartitionTraiteNP;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service @RequiredArgsConstructor
public class RepartitionTraiteNPService implements IServiceRepartitionTraiteNP
{
    private final IServiceCalculsComptablesTraite comptaTraiteService;
    private final RepartitionTraiteRepo rtRepo;
    private final RepartitionTraiteNPMapper repTnpMapper;
    private final ILogService logService;
    private final ObjectCopier<Repartition> repCopier;
    private final TypeRepo typeRepo;
    private final CedanteTraiteRepository cedTraiRepo;
    private final TraiteNPRepository tnpRepo;

    @Override
    public RepartitionTraiteNPResp save(PlacementTraiteNPReq dto)
    {
        if(dto.getRepId() == null) return this.create(dto);
        return this.update(dto);
    }

    @Override
    public Page<RepartitionTraiteNPResp> search(Long traiteNPId, String key, Pageable pageable)
    {
        Page<RepartitionTraiteNPResp> repartitionPage = rtRepo.search(traiteNPId, key, pageable);
        List<RepartitionTraiteNPResp> repartitionList = repartitionPage.stream()
                .peek(r->r.setTauxDejaReparti(comptaTraiteService.calculateTauxDejaPlace(traiteNPId)))
                .toList();
        return new PageImpl<>(repartitionList, pageable, repartitionPage.getTotalElements());
    }

    @Override @Transactional
    public RepartitionTraiteNPResp create(PlacementTraiteNPReq dto)
    {
        //TODO Calculer la pmd du cessionnaire
        Optional<Long> plaId$ = rtRepo.getPlacementIdByTraiteNpIdAndCesId(dto.getTraiteNpId(), dto.getCesId());
        if(plaId$.isPresent())
        {
            dto.setRepId(plaId$.get());
            this.update(dto);
        }
        Repartition repartition = repTnpMapper.mapToPlacementTnp(dto);
        repartition.setType(typeRepo.findByUniqueCode("REP_PLA_TNP").orElseThrow(()->new AppException("Type(REP_PLA_TNP) introuvable")));
        //if(rtRepo.)
        setMontantsPrimes(dto.getTraiteNpId(), dto.getRepTaux(), repartition);

        repartition = rtRepo.save(repartition);
        if(dto.isAperiteur()) setAsAperiteur(repartition);
        logService.logg("Enregistrement d'un placement sur traité non proportionnel", new Repartition(), repartition, "Repartition");

        RepartitionTraiteNPResp repartitionTraiteNPResp = rtRepo.getRepartitionTraiteNPResp(repartition.getRepId());
        repartitionTraiteNPResp.setTauxDejaReparti(comptaTraiteService.calculateTauxDejaPlace(dto.getTraiteNpId()));
        repartitionTraiteNPResp.setTauxRestant(comptaTraiteService.calculateTauxRestantAPlacer(dto.getTraiteNpId()));
        return repartitionTraiteNPResp;
    }

    @Override @Transactional
    public RepartitionTraiteNPResp update(PlacementTraiteNPReq dto)
    {
        //TODO Calculer la pmd du cessionnaire
        Repartition placement = rtRepo.findById(dto.getRepId()).orElseThrow(()->new AppException("Placement introuvable"));
        Repartition oldPlacement = repCopier.copy(placement);
        placement.setRepTaux(dto.getRepTaux());
        if(placement.getCessionnaire() == null || !Objects.equals(dto.getCesId(), placement.getCessionnaire().getCesId()))  //Si le cessionnaire à changé
        {
            placement.setCessionnaire(new Cessionnaire(dto.getCesId()));
        }
        if(dto.isAperiteur()) setAsAperiteur(placement);
        setMontantsPrimes(dto.getTraiteNpId(), dto.getRepTaux(), placement);
        logService.logg("Modification d'un placement sur traité non proportionnel", oldPlacement, placement, "Repartition");
        RepartitionTraiteNPResp repartitionTraiteNPResp = rtRepo.getRepartitionTraiteNPResp(dto.getRepId());
        repartitionTraiteNPResp.setTauxDejaReparti(comptaTraiteService.calculateTauxDejaPlace(dto.getTraiteNpId()));
        repartitionTraiteNPResp.setTauxRestant(comptaTraiteService.calculateTauxRestantAPlacer(dto.getTraiteNpId()));
        return repartitionTraiteNPResp;
    }

    @Override @Transactional
    public void createRepartitionCesLegTraite(CesLeg cesLeg, Long cedTraiId)
    {
        if(cedTraiId == null || !cedTraiRepo.existsById(cedTraiId)) throw new AppException("Cédante non prise en compte par le traité");
        Repartition repartition = repTnpMapper.mapToCesLegRepartition(cesLeg, cedTraiId);

        rtRepo.save(repartition);
        logService.logg("Ajout d'une repartition de type cession légale sur un traité non proportionel", new Repartition(), repartition, "Repartition");
    }

    @Override @Transactional
    public void updateRepartitionCesLegTraite(CesLeg cesLeg, Long cedTraiId)
    {
        Repartition repartition;
        if(cesLeg.getRepId() == null && cedTraiId == null) throw new AppException("Repartition nulle");
        if(cesLeg.getRepId() == null) repartition = rtRepo.findByCedTraiIdAndPclId(cedTraiId, cesLeg.getParamCesLegalId());
        else repartition  = rtRepo.findById(cesLeg.getRepId()).orElseThrow(()->new AppException("Repartition introuvable"));
        Repartition oldRepartition = repCopier.copy(repartition);
        repartition.setRepTaux(cesLeg.getTauxCesLeg());
        repartition.setRepPrime(cesLeg.getPmd());
        logService.logg("Modification d'une repartition de type cession légale sur un traité non proportionel", oldRepartition, repartition, "Repartition");
    }

    @Override @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onCedanteTraiteEvent(GenereicEvent<CedanteTraite> event)
    {
        System.out.println("============Listening event : " + event.getAction() + "=============");
        if(event == null || event.getData() == null) throw new AppException("Evènement publié sans aucune donnée");
        if(event.getData().getTraiteNonProportionnel() == null || event.getData().getTraiteNonProportionnel().getTraiteNpId() == null)
            throw new AppException("Les données du traité n'ont par été fournies sur à la publication de l'événement");

        Long traiteNpId = event.getData().getTraiteNonProportionnel().getTraiteNpId();
        switch (event.getAction())
        {
            case ADD_CEDANTE_TO_TRAITE_NP, UPDATE_CEDANTE_ON_TRAITE_NP, REMOVE_CEDANTE_ON_TRAITE_NP ->
            {
                List<Repartition> placements = rtRepo.getValidPlacementsOnTraiteNp(traiteNpId);
                if(placements != null)
                {
                    placements.forEach(placement->this.setMontantsPrimes(traiteNpId, placement.getRepTaux(), placement));
                }
            }
        }
    }

    private void setMontantsPrimes(Long traiteNpId, BigDecimal repTaux, Repartition repartition)
    {
        PmdGlobalResp pmdGlobal = cedTraiRepo.getPmdGlobal(traiteNpId);
        if(pmdGlobal == null || repartition == null) return;
        TauxCourtiersResp tauxCourtiers = tnpRepo.getTauxCourtiers(traiteNpId);
        BigDecimal tauxCoutier = tauxCourtiers == null ? BigDecimal.ZERO : tauxCourtiers.getTraiTauxCourtier();
        BigDecimal tauxCourtierPlaceur = tauxCourtiers ==  null ? BigDecimal.ZERO  :  tauxCourtiers.getTraiTauxCourtierPlaceur();

        BigDecimal repMontantComCourt = pmdGlobal.getTraiPmdCourtier() == null ? BigDecimal.ZERO : pmdGlobal.getTraiPmdCourtier().multiply(tauxCoutier).divide(CENT, 20, RoundingMode.HALF_UP);
        BigDecimal repMontantCourt = pmdGlobal.getTraiPmdCourtierPlaceur() == null ? BigDecimal.ZERO : pmdGlobal.getTraiPmdCourtierPlaceur().multiply(tauxCourtierPlaceur).divide(CENT, 20, RoundingMode.HALF_UP);
        BigDecimal repPrime = pmdGlobal.getTraiPmd() == null ? BigDecimal.ZERO : pmdGlobal.getTraiPmd().multiply(repTaux).divide(CENT, 20, RoundingMode.HALF_UP);
        BigDecimal repPrimeNette = pmdGlobal.getTraiPmdNette() == null ? BigDecimal.ZERO : pmdGlobal.getTraiPmdNette().multiply(repTaux).divide(CENT, 20, RoundingMode.HALF_UP);

        repartition.setRepMontantComCourt(repMontantComCourt);
        repartition.setRepMontantCourtierPlaceur(repMontantCourt);
        repartition.setRepPrime(repPrime);
        repartition.setRepPrimeNette(repPrimeNette);
    }

    private void setAsAperiteur(Repartition repartition)
    {
        if(repartition == null) return;
        repartition.setAperiteur(true);
        rtRepo.setAsTheOnlyAperiteur(repartition.getRepId());
    }
}