package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionTraiteRepo;
import com.pixel.synchronre.sychronremodule.model.dao.TraiteNPRepository;
import com.pixel.synchronre.sychronremodule.model.dao.TrancheCedanteRepository;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CesLeg;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.PmdGlobalResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.RepartitionTraiteNPMapper;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.PlacementTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionTraiteNPResp;
import com.pixel.synchronre.sychronremodule.model.dto.traite.response.TauxCourtiersResp;
import com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import com.pixel.synchronre.sychronremodule.model.events.LoggingEvent;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptablesTraite;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceRepartitionTraiteNP;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.pixel.synchronre.sychronremodule.model.constants.USUAL_NUMBERS.CENT;

@Service @RequiredArgsConstructor
public class RepartitionTraiteNPService implements IServiceRepartitionTraiteNP
{
    private final IServiceCalculsComptablesTraite comptaTraiteService;
    private final RepartitionTraiteRepo rtRepo;
    private final RepartitionTraiteNPMapper repTnpMapper;
    private final ObjectCopier<Repartition> repCopier;
    private final TypeRepo typeRepo;
    private final TrancheCedanteRepository trancheCedanteRepo;
    private final TraiteNPRepository tnpRepo;

    private final ApplicationEventPublisher eventPublisher;

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
        Optional<Long> plaId$ = rtRepo.getPlacementIdByTraiteNpIdAndCesId(dto.getTraiteNpId() ==null ? 0l : dto.getTraiteNpId(), dto.getCesId());
        if(plaId$.isPresent())
        {
            dto.setRepId(plaId$.get());
            return this.update(dto);
        }
        Repartition repartition = repTnpMapper.mapToPlacementTnp(dto);
        repartition.setType(typeRepo.findByUniqueCode("REP_PLA_TNP").orElseThrow(()->new AppException("Type(REP_PLA_TNP) introuvable")));
        //if(rtRepo.)
        repartition = recalculateMontantPrimeOnPlacement(dto, repartition);
        repartition = rtRepo.save(repartition);
        if(dto.isAperiteur()) setAsAperiteur(repartition);
        eventPublisher.publishEvent(new LoggingEvent(this, "Enregistrement d'un placement sur traité non proportionnel", new Repartition(), repartition, "Repartition"));

        RepartitionTraiteNPResp repartitionTraiteNPResp = rtRepo.getRepartitionTraiteNPResp(repartition.getRepId());
        repartitionTraiteNPResp.setTauxDejaReparti(comptaTraiteService.calculateTauxDejaPlace(dto.getTraiteNpId()));
        repartitionTraiteNPResp.setTauxRestant(comptaTraiteService.calculateTauxRestantAPlacer(dto.getTraiteNpId()));
        return repartitionTraiteNPResp;
    }

    @Override @Transactional
    public RepartitionTraiteNPResp update(PlacementTraiteNPReq dto)
    {
        Repartition placement = rtRepo.findById(dto.getRepId()).orElseThrow(()->new AppException("Placement introuvable"));
        Repartition oldPlacement = repCopier.copy(placement);
        placement.setRepTaux(dto.getRepTaux());
        if(placement.getCessionnaire() == null || !Objects.equals(dto.getCesId(), placement.getCessionnaire().getCesId()))  //Si le cessionnaire à changé
        {
            placement.setCessionnaire(new Cessionnaire(dto.getCesId()));
        }
        if(dto.isAperiteur()) setAsAperiteur(placement);
        placement = recalculateMontantPrimeOnPlacement(dto, placement);
        eventPublisher.publishEvent(new LoggingEvent(this,"Modification d'un placement sur traité non proportionnel", oldPlacement, placement, "Repartition"));
        RepartitionTraiteNPResp repartitionTraiteNPResp = rtRepo.getRepartitionTraiteNPResp(dto.getRepId());
        repartitionTraiteNPResp.setTauxDejaReparti(comptaTraiteService.calculateTauxDejaPlace(dto.getTraiteNpId()));
        repartitionTraiteNPResp.setTauxRestant(comptaTraiteService.calculateTauxRestantAPlacer(dto.getTraiteNpId()));
        return repartitionTraiteNPResp;
    }

    @Override @Transactional
    public void createRepartitionCesLegTraite(CesLeg cesLeg)
    {
        Repartition repartition = repTnpMapper.mapToCesLegRepartition(cesLeg);
        rtRepo.save(repartition);
        setMontantPrimesForCesLegRep(cesLeg, repartition);
        eventPublisher.publishEvent(new LoggingEvent(this,"Ajout d'une repartition de type cession légale sur un traité non proportionel", new Repartition(), repartition, "Repartition"));
    }

    @Override @Transactional
    public void updateRepartitionCesLegTraite(CesLeg cesLeg)
    {
        Repartition repartition;
        Long trancheCedanteId = cesLeg.getTrancheCedanteId();
        if(cesLeg.getRepId() == null && trancheCedanteId == null) throw new AppException("Repartition nulle");
        else if(cesLeg.getRepId() == null) repartition = rtRepo.findByTrancheCedanteIdAndPclId(trancheCedanteId, cesLeg.getParamCesLegalId());
        else repartition  = rtRepo.findById(cesLeg.getRepId()).orElseThrow(()->new AppException("Repartition introuvable"));

        Repartition oldRepartition = repCopier.copy(repartition);

        repartition.setRepStatut(cesLeg.isAccepte());
        repartition.setRepTaux(cesLeg.getTauxCesLeg());
        repartition.setRepTauxComCourt(cesLeg.getTauxCourtier());
        repartition.setRepTauxComCourtPlaceur(cesLeg.getTauxCourtierPlaceur());
        repartition = rtRepo.save(repartition);
        setMontantPrimesForCesLegRep(cesLeg, repartition);
        eventPublisher.publishEvent(new LoggingEvent(this,"Modification d'une repartition de type cession légale sur un traité non proportionel", oldRepartition, repartition, "Repartition"));
    }

    @Override @Transactional
    public void setMontantsPrimes(Long traiteNpId, BigDecimal repTaux, BigDecimal tauxCoutier, BigDecimal tauxCourtierPlaceur, Repartition repartition)
    {
        PmdGlobalResp pmdGlobal = trancheCedanteRepo.getPmdGlobal(traiteNpId);
        if(pmdGlobal == null || repartition == null) return;

        BigDecimal repPrime = pmdGlobal.getTraiPmd() == null ? BigDecimal.ZERO : pmdGlobal.getTraiPmd().multiply(repTaux).divide(CENT, 20, RoundingMode.HALF_UP);
        BigDecimal repMontantComCourt = repPrime == null || tauxCoutier == null ? BigDecimal.ZERO :
                repPrime.multiply(tauxCoutier).divide(CENT, 20, RoundingMode.HALF_UP);
        BigDecimal repMontantCourtPlaceur = repPrime == null || tauxCourtierPlaceur == null ? BigDecimal.ZERO :
                repPrime.multiply(tauxCourtierPlaceur).divide(CENT, 20, RoundingMode.HALF_UP);

        BigDecimal repPrimeNette = repPrime == null ? BigDecimal.ZERO : repPrime.subtract(repMontantComCourt.add(repMontantCourtPlaceur));

        repartition.setRepMontantComCourt(repMontantComCourt);
        repartition.setRepMontantCourtierPlaceur(repMontantCourtPlaceur);
        repartition.setRepPrime(repPrime);
        repartition.setRepPrimeNette(repPrimeNette);
        rtRepo.save(repartition);
    }

    @Override @Transactional
    public void desactivateCesLegByTraiteNpIdAndPclId(Long traiteNpId, Long paramCesLegalId)
    {
        List<Repartition> pclReps = rtRepo.findCesLegByTraiteNpIdAndPclId(traiteNpId, paramCesLegalId);
        if(pclReps == null) return;
        pclReps.forEach(pclRep-> {
            pclRep.setRepStatut(false);
            rtRepo.save(pclRep);
        });

    }

    private void setAsAperiteur(Repartition repartition)
    {
        if(repartition == null) return;
        repartition.setAperiteur(true);
        rtRepo.setAsTheOnlyAperiteur(repartition.getRepId());
    }

    @Override @Transactional
    public void setMontantPrimesForCesLegRep(CesLeg cesLeg, Repartition repartition) {
        if(repartition.getTrancheCedante() == null || repartition.getTrancheCedante().getTrancheCedanteId() == null)
            throw new AppException("Impossible de récupérer l'ID du traité de la CedanteTraite lié à répartition " + repartition.getRepId());
        Long traiteNpId = trancheCedanteRepo.getTraiteIdByTrancheCedanteId(repartition.getTrancheCedante().getTrancheCedanteId());
        this.setMontantsPrimes(traiteNpId,cesLeg.getTauxCesLeg(), cesLeg.getTauxCourtier(), cesLeg.getTauxCourtierPlaceur(), repartition);
    }

    @Override
    public Repartition recalculateMontantPrimeOnPlacement(PlacementTraiteNPReq dto, Repartition placement) {
        TauxCourtiersResp tauxCourtiers = tnpRepo.getTauxCourtiers(dto.getTraiteNpId());
        BigDecimal tauxCourtier = tauxCourtiers == null ? BigDecimal.ZERO : tauxCourtiers.getTraiTauxCourtier();
        BigDecimal tauxCourtierPlaceur = tauxCourtiers ==  null ? BigDecimal.ZERO  :  tauxCourtiers.getTraiTauxCourtierPlaceur();
        placement.setRepTauxComCourt(tauxCourtier);
        placement.setRepTauxComCourtPlaceur(tauxCourtierPlaceur);
        setMontantsPrimes(dto.getTraiteNpId(), dto.getRepTaux(), tauxCourtier, tauxCourtierPlaceur, placement);
        return placement;
    }

    @Override
    public void recalculateMontantPrimeForPlacementOnTraite(Long traiteNpId)
    {
        List<PlacementTraiteNPReq> placementDtos = rtRepo.findPlacementTraiteDtos(traiteNpId);
        if(placementDtos != null && !placementDtos.isEmpty())
        {
            placementDtos.forEach(dto->
            {
                Repartition placement = rtRepo.findById(dto.getRepId()).orElseThrow(null);
                if(placement == null) return;
                this.recalculateMontantPrimeOnPlacement(dto, placement);
            });
        }
    }
}