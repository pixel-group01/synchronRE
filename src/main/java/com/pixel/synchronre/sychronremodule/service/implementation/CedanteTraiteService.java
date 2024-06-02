package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;

import static com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions.*;
import static com.pixel.synchronre.sychronremodule.model.constants.UsualNumbers.CENT;
import com.pixel.synchronre.sychronremodule.model.dao.*;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteResp;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CesLeg;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.CedanteTraiteMapper;
import com.pixel.synchronre.sychronremodule.model.dto.traite.response.TauxCourtiersResp;
import com.pixel.synchronre.sychronremodule.model.entities.Cedante;
import com.pixel.synchronre.sychronremodule.model.entities.CedanteTraite;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import com.pixel.synchronre.sychronremodule.model.events.GenereicEvent;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCedanteTraite;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceRepartitionTraiteNP;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceRepartition;
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

@Service @RequiredArgsConstructor
public class CedanteTraiteService implements IServiceCedanteTraite
{
    private final CedanteTraiteRepository cedTraiRepo;
    private final IServiceRepartitionTraiteNP repTnpService;
    private final IserviceRepartition repFacService;
    private final CedanteTraiteMapper cedTraiMapper;
    private final ILogService logService;
    private final ObjectCopier<CedanteTraite> cedTraiCopier;
    private final RepartitionTraiteRepo repTraiRepo;
    private final TraiteNPRepository traiteRepo;
    private final CedRepo cedRepo;
    private final ParamCessionLegaleRepository paramCesLegRepo;
    private final ApplicationEventPublisher eventPublisher;


    @Override @Transactional
    public CedanteTraiteResp create(CedanteTraiteReq dto)
    {
        //Si le traite a déjà cette cédante, on fait un update
        if(cedTraiRepo.traiteHasCedante(dto.getTraiteNpId(), dto.getCedId()))
        {
            Long cedanteTraiteId = cedTraiRepo.getCedanteTraiteIdByTraiIdAndCedId(dto.getTraiteNpId(), dto.getCedId());
            dto.setCedanteTraiteId(cedanteTraiteId);
            return this.update(dto);
        }
        CedanteTraite cedanteTraite = cedTraiMapper.mapToCedanteTraite(dto);
        setMontantsPrimes(dto, cedanteTraite);
        cedanteTraite = cedTraiRepo.save(cedanteTraite);
        final Long cedTraiId = cedanteTraite.getCedanteTraiteId();
        logService.logg(ADD_CEDANTE_TO_TRAITE_NP, new CedanteTraite(), cedanteTraite, SynchronReTables.CEDANTE_TRAITE);
        if(dto.getCessionsLegales() != null && !dto.getCessionsLegales().isEmpty())
        {
            dto.getCessionsLegales().forEach(cesLeg->repTnpService.createRepartitionCesLegTraite(cesLeg, cedTraiId));
        }

        CedanteTraiteResp cedanteTraiteResp = cedTraiRepo.getCedanteTraiteRespById(cedanteTraite.getCedanteTraiteId());
        cedanteTraiteResp.setCessionsLegales(repTraiRepo.findCesLegsByCedTraiId(cedanteTraite.getCedanteTraiteId()));
        eventPublisher.publishEvent(new GenereicEvent<CedanteTraite>(this, cedanteTraite, ADD_CEDANTE_TO_TRAITE_NP));
        return cedanteTraiteResp;
    }

    @Override @Transactional
    public CedanteTraiteResp update(CedanteTraiteReq dto)
    {
        if(dto.getCedanteTraiteId() == null) throw new AppException("CedanteTraite null");
        CedanteTraite cedanteTraite = cedTraiRepo.findById(dto.getCedanteTraiteId()).orElseThrow(()->new AppException("CedanteTraite introuvable"));
        CedanteTraite oldCedanteTraite = cedTraiCopier.copy(cedanteTraite);
        cedanteTraite.setCedante(new Cedante(dto.getCedId()));
        cedanteTraite.setAssiettePrime(dto.getAssiettePrime());
        cedanteTraite.setPmd(dto.getPmd());
        cedanteTraite.setTauxPrime(dto.getTauxPrime());
        setMontantsPrimes(dto, cedanteTraite);
        logService.logg(SynchronReActions.UPDATE_CEDANTE_ON_TRAITE_NP, oldCedanteTraite, cedanteTraite, SynchronReTables.CEDANTE_TRAITE);
        if(dto.getCessionsLegales() != null && !dto.getCessionsLegales().isEmpty())
        {
            dto.getCessionsLegales().forEach(cesLeg->repTnpService.updateRepartitionCesLegTraite(cesLeg, dto.getCedanteTraiteId()));
        }

        CedanteTraiteResp cedanteTraiteResp = cedTraiRepo.getCedanteTraiteRespById(dto.getCedanteTraiteId());
        cedanteTraiteResp.setCessionsLegales(repTraiRepo.findCesLegsByCedTraiId(dto.getCedanteTraiteId()));
        eventPublisher.publishEvent(new GenereicEvent<CedanteTraite>(this, cedanteTraite, UPDATE_CEDANTE_ON_TRAITE_NP));
        return cedanteTraiteResp;
    }

    @Override @Transactional
    public CedanteTraiteResp save(CedanteTraiteReq dto)
    {
        if(dto.getCedanteTraiteId() == null ) return this.create(dto);
        else return this.update(dto);
    }

    @Override
    public Page<CedanteTraiteResp> search(Long traiId, String key, Pageable pageable)
    {
        key = StringUtils.stripAccentsToUpperCase(key);
        Page<CedanteTraiteResp> cedanteTraitePage = cedTraiRepo.search(traiId, key, pageable);
        List<CedanteTraiteResp> cedanteTraiteList = cedanteTraitePage.stream()
                .peek(cedTrai-> {
                    List<CesLeg> repartitions = repTraiRepo.findCesLegsByCedTraiId(cedTrai.getCedanteTraiteId());

                    cedTrai.setCessionsLegales(repartitions);
                }).toList();

        return new PageImpl<>(cedanteTraiteList, pageable, cedanteTraitePage.getTotalElements());
    }
    @Override @Transactional
    public void removeCedanteOnTraite(Long cedanteTraiteId)
    {
        CedanteTraite cedanteTraite = cedTraiRepo.findById(cedanteTraiteId).orElseThrow(()->new AppException("CedanteTraite introuvable"));
        CedanteTraite oldCedanteTraite = cedTraiCopier.copy(cedanteTraite);
        cedanteTraite.setStatut(new Statut("SUP"));
        logService.logg(SynchronReActions.REMOVE_CEDANTE_ON_TRAITE_NP, oldCedanteTraite, cedanteTraite, SynchronReTables.CEDANTE_TRAITE);
        repTraiRepo.findCesLegIdsByCedTraiId(cedanteTraiteId).forEach(repId-> repFacService.annulerRepartition(repId));

        eventPublisher.publishEvent(new GenereicEvent(this, cedanteTraite, REMOVE_CEDANTE_ON_TRAITE_NP));
    }

    @Override
    public CedanteTraiteReq getEditDto(Long cedanteTraiteId)
    {
        CedanteTraiteReq dto = cedTraiRepo.getEditDto(cedanteTraiteId);
        List<CesLeg> cesLegs =repTraiRepo.findCesLegsByCedTraiId(cedanteTraiteId);
        if(cesLegs == null || cesLegs.isEmpty())
        {
            cesLegs = paramCesLegRepo.findCesLegsByCedId(dto.getCedId());
        }
        dto.setCessionsLegales(cesLegs);
        return dto;
    }

    @Override
    public CedanteTraiteReq getEditDto(Long traiteNpId, Long cedId)
    {
        if(traiteNpId == null || !traiteRepo.existsById(traiteNpId)) throw new AppException("Traité non proportionnel introuvable");
        if(cedId == null || !cedRepo.existsById(cedId)) throw new AppException("Cédante introuvable");
        CedanteTraiteReq dto = new CedanteTraiteReq(traiteNpId, cedId);
        List<CesLeg> cesLegs = repTraiRepo.findCesLegsByTraiIdAndCedId(traiteNpId, cedId);
        if(cesLegs == null || cesLegs.isEmpty())
        {
            cesLegs = paramCesLegRepo.findCesLegsByCedId(cedId);
        }
        dto.setCessionsLegales(cesLegs);
        return dto;
    }

    @Override
    public CedanteTraiteReq getEditDto(Long cedanteTraiteId, Long traiteNpId, Long cedId)
    {
        if(cedanteTraiteId != null && cedTraiRepo.existsById(cedanteTraiteId)) return this.getEditDto(cedanteTraiteId);
        return this.getEditDto(traiteNpId, cedId);
    }

    @Override
    public List<CedanteTraiteResp> getCedanteTraitelist(Long traiteNpId) {
        return cedTraiRepo.getCedanteTraitelist(traiteNpId);
    }


    private void setMontantsPrimes(CedanteTraiteReq dto, CedanteTraite cedanteTraite)
    {
        TauxCourtiersResp tauxCourtiers = traiteRepo.getTauxCourtiers(dto.getTraiteNpId());
        BigDecimal tauxCourtier = tauxCourtiers == null ? null : tauxCourtiers.getTraiTauxCourtier();
        BigDecimal tauxCourtierPlaceur = tauxCourtiers == null ? null : tauxCourtiers.getTraiTauxCourtierPlaceur();
        BigDecimal pmdCourtier = cedanteTraite.getPmd() == null || tauxCourtier == null ? BigDecimal.ZERO : cedanteTraite.getPmd().multiply(tauxCourtier).divide(CENT).setScale(20, RoundingMode.HALF_UP);
        BigDecimal pmdCourtierPlaceur = cedanteTraite.getPmd() == null || tauxCourtierPlaceur == null ? BigDecimal.ZERO : cedanteTraite.getPmd().multiply(tauxCourtierPlaceur).divide(CENT).setScale(20, RoundingMode.HALF_UP);
        BigDecimal pmdNette = cedanteTraite.getPmd() == null ? BigDecimal.ZERO : cedanteTraite.getPmd().subtract(pmdCourtier.add(pmdCourtierPlaceur));
        cedanteTraite.setPmdCourtier(pmdCourtier);
        cedanteTraite.setPmdCourtierPlaceur(pmdCourtierPlaceur);
        cedanteTraite.setPmdNette(pmdNette);
    }
}
