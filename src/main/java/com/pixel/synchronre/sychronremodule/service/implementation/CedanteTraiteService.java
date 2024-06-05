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
import com.pixel.synchronre.sychronremodule.model.events.CedanteTraiteEvent;
import com.pixel.synchronre.sychronremodule.model.events.LoggingEvent;
import com.pixel.synchronre.sychronremodule.model.events.SimpleEvent;
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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class CedanteTraiteService implements IServiceCedanteTraite
{
    private final CedanteTraiteRepository cedTraiRepo;
    //private final IServiceRepartitionTraiteNP repTnpService;
    //private final IserviceRepartition repFacService;
    private final CedanteTraiteMapper cedTraiMapper;
    //private final ILogService logService;
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

        this.setMontantsPrimes(dto, cedanteTraite);

        eventPublisher.publishEvent(new LoggingEvent(this, UPDATE_CEDANTE_ON_TRAITE_NP, new CedanteTraite(), cedanteTraite, SynchronReTables.CEDANTE_TRAITE));
        eventPublisher.publishEvent(new CedanteTraiteEvent(this, ADD_CEDANTE_TO_TRAITE_NP, cedanteTraite, dto.getCessionsLegales()));

        CedanteTraiteResp cedanteTraiteResp = cedTraiRepo.getCedanteTraiteRespById(cedanteTraite.getCedanteTraiteId());
        cedanteTraiteResp.setCessionsLegales(repTraiRepo.findPersistedCesLegsByCedTraiId(cedanteTraite.getCedanteTraiteId()));

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
        this.setMontantsPrimes(dto, cedanteTraite);
        eventPublisher.publishEvent(new LoggingEvent(this, UPDATE_CEDANTE_ON_TRAITE_NP, oldCedanteTraite, cedanteTraite, SynchronReTables.CEDANTE_TRAITE));
        eventPublisher.publishEvent(new CedanteTraiteEvent(this, UPDATE_CEDANTE_ON_TRAITE_NP, cedanteTraite, dto.getCessionsLegales()));

        CedanteTraiteResp cedanteTraiteResp = cedTraiRepo.getCedanteTraiteRespById(dto.getCedanteTraiteId());
        cedanteTraiteResp.setCessionsLegales(repTraiRepo.findPersistedCesLegsByCedTraiId(dto.getCedanteTraiteId()));
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
                    List<CesLeg> repartitions = repTraiRepo.findPersistedCesLegsByCedTraiId(cedTrai.getCedanteTraiteId());

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
        //logService.logg(SynchronReActions.REMOVE_CEDANTE_ON_TRAITE_NP, oldCedanteTraite, cedanteTraite, SynchronReTables.CEDANTE_TRAITE);

        eventPublisher.publishEvent(new LoggingEvent(this, REMOVE_CEDANTE_ON_TRAITE_NP, oldCedanteTraite, cedanteTraite, SynchronReTables.CEDANTE_TRAITE));
        eventPublisher.publishEvent(new SimpleEvent(this, REMOVE_CEDANTE_ON_TRAITE_NP, cedanteTraite));
    }

    @Override
    public CedanteTraiteReq getEditDto(Long cedanteTraiteId)
    {
        CedanteTraiteReq dto = cedTraiRepo.getEditDto(cedanteTraiteId);
        List<CesLeg> cesLegs =repTraiRepo.findPersistedCesLegsByCedTraiId(cedanteTraiteId);
        if(cesLegs == null || cesLegs.isEmpty())
        {
            cesLegs = paramCesLegRepo.findCesLegsByCedId(dto.getCedId());
        }
        dto.setCessionsLegales(cesLegs);
        return dto;
    }

    @Override
    public CedanteTraiteReq getEditDto(CedanteTraiteReq dto)
    {
        if(dto == null) return null;
        Long cedanteTraiteId = dto.getCedanteTraiteId() != null ? dto.getCedanteTraiteId() : cedTraiRepo.getCedanteTraiteIdByTraiIdAndCedId(dto.getTraiteNpId(), dto.getCedId());
        dto.setCedanteTraiteId(cedanteTraiteId);
        BigDecimal pmd = dto.getAssiettePrime() != null && dto.getTauxPrime() != null ?
                dto.getAssiettePrime().multiply(dto.getTauxPrime()).divide(CENT, 20, RoundingMode.HALF_UP) :
                dto.getPmd();
        dto.setPmd(pmd);

        //Cessionns légales envoyées par le front dans le dto
        List<CesLeg> dtoCesLegs = dto.getCessionsLegales();


        //Cessionns légales lié au pays de la cedante concerné
        List<CesLeg> baseCesLegs = paramCesLegRepo.findCesLegsByCedId(dto.getCedId());

        //Cessionns légales Déjà persisté en base pour ce traité et cette cédante
        List<CesLeg> persistedCesLegs = cedanteTraiteId != null ?
                        repTraiRepo.findPersistedCesLegsByCedTraiId(cedanteTraiteId) :
                repTraiRepo.findPersistedCesLegsByTraiIdAndCedId(dto.getTraiteNpId(), dto.getCedId());

        List<CesLeg> cesLegs = null;

        //Le front n'envoie aucune donnée de cessions légales et il n'existe aucune cessionn légale déjà persistée
        if((dtoCesLegs == null || dtoCesLegs.isEmpty()) && (persistedCesLegs == null || persistedCesLegs.isEmpty()))
        {
            cesLegs = baseCesLegs;
        }

        //Le front n'envoie aucune donnée de cessions légales mais il existe des cessionns légales déjà persistées
        if((dtoCesLegs == null || dtoCesLegs.isEmpty()) && persistedCesLegs != null && !persistedCesLegs.isEmpty())
        {
            cesLegs = persistedCesLegs;
        }

        //Le front envoie des données de cessions légales mais il n'existe aucune cessionn légales déjà persistée
        if((dtoCesLegs != null && !dtoCesLegs.isEmpty()) && (persistedCesLegs == null || persistedCesLegs.isEmpty()))
        {
            cesLegs = dtoCesLegs;
        }

        //Le front envoie des données de cessions légales et il existe des cessionns légales déjà persistées
        if((dtoCesLegs != null && !dtoCesLegs.isEmpty()) && (persistedCesLegs != null && !persistedCesLegs.isEmpty()))
        {
            cesLegs = persistedCesLegs.stream()
                    .peek(persCL->setCesLegAccepte(persCL, dtoCesLegs)).collect(Collectors.toList());
        }

        if(cesLegs != null && pmd != null)
        {
            cesLegs.forEach(cesLeg -> {
                BigDecimal cesLegPmd = pmd.multiply(cesLeg.getTauxCesLeg()).divide(CENT, 20, RoundingMode.HALF_UP);
                cesLeg.setPmd(cesLegPmd);
            });
        }
        dto.setCessionsLegales(cesLegs);
        return dto;
    }

    private void setCesLegAccepte(CesLeg cesLeg, List<CesLeg> dtoCesLegs)
    {
        Optional<CesLeg> correspondingDtoCesLeg$ = dtoCesLegs.stream().filter(dtoCl-> Objects.equals(cesLeg.getParamCesLegalId(), dtoCl.getParamCesLegalId())).findFirst();

        cesLeg.setAccepte(correspondingDtoCesLeg$.isPresent() ? correspondingDtoCesLeg$.get().isAccepte() : false);
    }

    @Override
    public CedanteTraiteReq getEditDto(Long traiteNpId, Long cedId)
    {
        if(traiteNpId == null || !traiteRepo.existsById(traiteNpId)) throw new AppException("Traité non proportionnel introuvable");
        if(cedId == null || !cedRepo.existsById(cedId)) throw new AppException("Cédante introuvable");
        CedanteTraiteReq dto = new CedanteTraiteReq(traiteNpId, cedId);
        List<CesLeg> cesLegs = repTraiRepo.findPersistedCesLegsByTraiIdAndCedId(traiteNpId, cedId);
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
