package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.dao.CedanteTraiteRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionTraiteRepo;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CesLeg;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.RepartitionTraiteNPMapper;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.PlacementTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionTraiteNPResp;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptablesTraite;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceRepartitionTraiteNP;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                .peek(r->r.setTauxDejaReparti(comptaTraiteService.calculateTauxDejaReparti(traiteNPId)))
                .toList();
        return new PageImpl<>(repartitionList, pageable, repartitionPage.getTotalElements());
    }

    @Override @Transactional
    public RepartitionTraiteNPResp create(PlacementTraiteNPReq dto)
    {
        //TODO Calculer la pmd du cessionnaire
        Repartition repartition = repTnpMapper.mapToPlacementTnp(dto);
        repartition.setType(typeRepo.findByUniqueCode("REP_PLA_TNP").orElseThrow(()->new AppException("Type(REP_PLA_TNP) introuvable")));
        //if(rtRepo.)
        repartition = rtRepo.save(repartition);
        if(dto.isAperiteur()) setAsAperiteur(repartition);
        logService.logg("Enregistrement d'un placement sur traité non proportionnel", new Repartition(), repartition, "Repartition");

        RepartitionTraiteNPResp repartitionTraiteNPResp = rtRepo.getRepartitionTraiteNPResp(repartition.getRepId());
        repartitionTraiteNPResp.setTauxDejaReparti(comptaTraiteService.calculateTauxDejaReparti(dto.getTraiteNpId()));
        repartitionTraiteNPResp.setTauxRestant(comptaTraiteService.calculateTauxRestantARepartir(dto.getTraiteNpId()));
        return repartitionTraiteNPResp;
    }

    //@Transactional
    private void setAsAperiteur(Repartition repartition)
    {
        if(repartition == null) return;
        repartition.setAperiteur(true);
        rtRepo.setAsTheOnlyAperiteur(repartition.getRepId());
    }

    @Override @Transactional
    public RepartitionTraiteNPResp update(PlacementTraiteNPReq dto)
    {
        //TODO Calculer la pmd du cessionnaire
        Repartition placement = rtRepo.findById(dto.getRepId()).orElseThrow(()->new AppException("Placement introuvable"));
        Repartition oldPlacement = repCopier.copy(placement);
        placement.setRepTaux(dto.getRepTaux());
        if(dto.isAperiteur()) setAsAperiteur(placement);
        logService.logg("Modification d'un placement sur traité non proportionnel", oldPlacement, placement, "Repartition");
        RepartitionTraiteNPResp repartitionTraiteNPResp = rtRepo.getRepartitionTraiteNPResp(dto.getRepId());
        repartitionTraiteNPResp.setTauxDejaReparti(comptaTraiteService.calculateTauxDejaReparti(dto.getTraiteNpId()));
        repartitionTraiteNPResp.setTauxRestant(comptaTraiteService.calculateTauxRestantARepartir(dto.getTraiteNpId()));
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
}