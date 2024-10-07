package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.*;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteResp;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CesLeg;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TranchePmdDto;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.CedanteTraiteMapper;
import com.pixel.synchronre.sychronremodule.model.dto.traite.response.TauxCourtiersResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.model.events.CedanteTraiteEvent;
import com.pixel.synchronre.sychronremodule.model.events.LoggingEvent;
import com.pixel.synchronre.sychronremodule.model.events.SimpleEvent;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCedanteTraite;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions.*;
import static com.pixel.synchronre.sychronremodule.model.constants.USUAL_NUMBERS.CENT;

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
    private final TrancheRepository trancheRepo;
    private final ParamCessionLegaleRepository paramCesLegRepo;
    private final ApplicationEventPublisher eventPublisher;
    private final TrancheCedanteRepository trancheCedRepo;

    @Override @Transactional
    public CedanteTraiteResp create(CedanteTraiteReq dto)
    {
        //Si le traite a déjà cette cédante, on fait un update
        Long traiteNpId = dto.getTraiteNpId();
        if(cedTraiRepo.traiteHasCedante(traiteNpId, dto.getCedId()))
        {
            Long cedanteTraiteId = cedTraiRepo.getCedanteTraiteIdByTraiIdAndCedId(traiteNpId, dto.getCedId());
            dto.setCedanteTraiteId(cedanteTraiteId);
            return this.update(dto);
        }
        CedanteTraite cedanteTraite = cedTraiMapper.mapToCedanteTraite(dto);
        cedanteTraite = cedTraiRepo.save(cedanteTraite);
        Long cedanteTraiteId = cedanteTraite.getCedanteTraiteId();
        dto.setCedanteTraiteId(cedanteTraiteId);
        List<TranchePmdDto> tranchePmdDtos = this.getTranchePmdDtos(dto, 20);
        if(tranchePmdDtos != null && !tranchePmdDtos.isEmpty())
        {
            tranchePmdDtos.stream().forEach(trPmd->this.saveTranchePmd(trPmd, cedanteTraiteId));
        }

        eventPublisher.publishEvent(new LoggingEvent(this, UPDATE_CEDANTE_ON_TRAITE_NP, new CedanteTraite(), cedanteTraite, SynchronReTables.CEDANTE_TRAITE));
        eventPublisher.publishEvent(new CedanteTraiteEvent(this, ADD_CEDANTE_TO_TRAITE_NP, cedanteTraite, dto));

        CedanteTraiteResp cedanteTraiteResp = cedTraiRepo.getCedanteTraiteRespById(cedanteTraite.getCedanteTraiteId());
        cedanteTraiteResp.setCessionsLegales(repTraiRepo.findPersistedCesLegsByCedTraiId(cedanteTraite.getCedanteTraiteId()));

        return cedanteTraiteResp;
    }

    private void saveTranchePmd(TranchePmdDto dto, Long cedanteTraiteId)
    {
        TrancheCedante trancheCedante = trancheCedRepo.findByTrancheIdAndCedanteTraiteId(dto.getTrancheId(), cedanteTraiteId);
        if(trancheCedante == null)
        {
            trancheCedante = new TrancheCedante();
            trancheCedante.setTranche(new Tranche(dto.getTrancheId()));
            trancheCedante.setCedanteTraite(new CedanteTraite(cedanteTraiteId));
        }

        trancheCedante.setPmd(dto.getPmd());
        trancheCedante.setPmdCourtier(dto.getPmdCourtier());
        trancheCedante.setPmdCourtierPlaceur(dto.getPmdCourtierPlaceur());
        trancheCedante.setPmdNette(dto.getPmdNette());
        trancheCedRepo.save(trancheCedante);
    }

    @Override @Transactional
    public CedanteTraiteResp update(CedanteTraiteReq dto)
    {
        if(dto.getCedanteTraiteId() == null) throw new AppException("CedanteTraite null");
        CedanteTraite cedanteTraite = cedTraiRepo.findById(dto.getCedanteTraiteId()).orElseThrow(()->new AppException("CedanteTraite introuvable"));
        CedanteTraite oldCedanteTraite = cedTraiCopier.copy(cedanteTraite);
        cedanteTraite.setCedante(new Cedante(dto.getCedId()));
        cedanteTraite.setAssiettePrime(dto.getAssiettePrime());
        eventPublisher.publishEvent(new LoggingEvent(this, UPDATE_CEDANTE_ON_TRAITE_NP, oldCedanteTraite, cedanteTraite, SynchronReTables.CEDANTE_TRAITE));
        eventPublisher.publishEvent(new CedanteTraiteEvent(this, UPDATE_CEDANTE_ON_TRAITE_NP, cedanteTraite, dto));

        Long cedanteTraiteId = cedanteTraite.getCedanteTraiteId();
        dto.setCedanteTraiteId(cedanteTraiteId);
        List<TranchePmdDto> tranchePmdDtos = this.getTranchePmdDtos(dto, 20);
        if(tranchePmdDtos != null && !tranchePmdDtos.isEmpty())
        {
            tranchePmdDtos.stream().forEach(trPmd->this.saveTranchePmd(trPmd, cedanteTraiteId));
        }

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
        eventPublisher.publishEvent(new SimpleEvent<CedanteTraite>(this, REMOVE_CEDANTE_ON_TRAITE_NP, cedanteTraite));
    }

    @Override
    public CedanteTraiteReq getEditDto(CedanteTraiteReq dto, int scale)
    {
        if(dto == null) return null;
        Long traiteNpId = dto.getTraiteNpId();
        Long cedId = dto.getCedId();
        Long cedanteTraiteId = dto.getCedanteTraiteId() != null ? dto.getCedanteTraiteId() : cedTraiRepo.getCedanteTraiteIdByTraiIdAndCedId(traiteNpId, cedId);

        BigDecimal persistedAssiettePrime = cedanteTraiteId == null ? BigDecimal.ZERO : cedTraiRepo.getAssiettePrime(cedanteTraiteId);
        BigDecimal assiettePrime = dto.getAssiettePrime() != null ? dto.getAssiettePrime() : persistedAssiettePrime;
        List<TranchePmdDto> tranchePmdDtos = this.getTranchePmdDtos(dto, scale);
        List<CesLeg> cesLegs = this.getCesLegs(dto);

        dto.setCedanteTraiteId(cedanteTraiteId);
        dto.setTraiteNpId(traiteNpId);
        dto.setCedId(cedId);
        dto.setAssiettePrime(assiettePrime);
        dto.setTranchePmdDtos(tranchePmdDtos);
        dto.setCessionsLegales(cesLegs);
        return dto;
    }

    @Override
    public List<TranchePmdDto> getTranchePmdDtos(CedanteTraiteReq dto, int scale)
    {
        Long traiteNpId = dto.getTraiteNpId();
        Long cedId = dto.getCedId();
        List<TranchePmdDto> naturalTranchePmds = trancheCedRepo.findNaturalTranchePmdForCedanteAndTraite(cedId, traiteNpId);
        if(naturalTranchePmds == null) return Collections.emptyList();
        naturalTranchePmds.forEach(trPmd->
        {
            calculatePmds(trPmd, dto, scale);
        });
        return naturalTranchePmds;
    }

    @Override
    public TranchePmdDto calculatePmds(TranchePmdDto trPmd, CedanteTraiteReq dto, int scale)
    {
        Long traiteNpId = dto.getTraiteNpId();
        Long cedanteTraiteId = dto.getCedanteTraiteId() != null ? dto.getCedanteTraiteId() : cedTraiRepo.getCedanteTraiteIdByTraiIdAndCedId(traiteNpId, dto.getCedId());

        BigDecimal tauxAbattement = traiteRepo.getTauxAbattement(traiteNpId);
        BigDecimal finalTauxAbattement = tauxAbattement == null || tauxAbattement.compareTo(BigDecimal.ZERO) == 0 ? CENT : tauxAbattement;
        TauxCourtiersResp tauxCourtiersResp = traiteRepo.getTauxCourtiers(traiteNpId);
        BigDecimal pmd = dto.getAssiettePrime() != null && trPmd.getTrancheTauxPrime() != null ?
                dto.getAssiettePrime().multiply(trPmd.getTrancheTauxPrime()).multiply(CENT.subtract(finalTauxAbattement)).divide(CENT.multiply(CENT), 20, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        BigDecimal pmdCourtier = tauxCourtiersResp == null || tauxCourtiersResp.getTraiTauxCourtier() == null ? BigDecimal.ZERO : pmd.multiply(tauxCourtiersResp.getTraiTauxCourtier()).divide(CENT, scale, RoundingMode.HALF_UP);
        BigDecimal pmdCourtierPlaceur = tauxCourtiersResp == null || tauxCourtiersResp.getTraiTauxCourtier() == null ? BigDecimal.ZERO : pmd.multiply(tauxCourtiersResp.getTraiTauxCourtierPlaceur()).divide(CENT, scale, RoundingMode.HALF_UP);
        BigDecimal pmdNette = pmd.subtract(pmdCourtier.add(pmdCourtierPlaceur));
        trPmd.setPmd(pmd);
        trPmd.setPmdCourtier(pmdCourtier);
        trPmd.setPmdCourtierPlaceur(pmdCourtierPlaceur);
        trPmd.setPmdNette(pmdNette);
        Long trancheCedanteId = trancheCedRepo.getTrancheCedanteIdByTrancheIdAndCedanteTraiteId(trPmd.getTrancheId(), cedanteTraiteId);
        trPmd.setTrancheCedanteId(trancheCedanteId);
        return trPmd;
    }

    private List<CesLeg> getCesLegs(CedanteTraiteReq dto) {
        //Cessionns légales envoyées par le front dans le dto
        List<CesLeg> dtoCesLegs = dto.getCessionsLegales();

        //Cessionns légales lié au pays de la cedante concerné
        List<CesLeg> baseCesLegs = paramCesLegRepo.findCesLegsByCedId(dto.getCedId());

        //Cessionns légales Déjà persisté en base pour ce traité et cette cédante
        List<CesLeg> persistedCesLegs = dto.getCedanteTraiteId() != null ?
                        repTraiRepo.findPersistedCesLegsByCedTraiId(dto.getCedanteTraiteId()) :
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

        if(cesLegs != null && dto.getAssiettePrime() != null)
        {
            cesLegs.forEach(cesLeg -> {
                BigDecimal cesLegPmd = dto.getAssiettePrime().multiply(cesLeg.getTauxCesLeg()).divide(CENT, 20, RoundingMode.HALF_UP);
                cesLeg.setPmd(cesLegPmd);
            });
        }
        return cesLegs;
    }

    private void setCesLegAccepte(CesLeg cesLeg, List<CesLeg> dtoCesLegs)
    {
        Optional<CesLeg> correspondingDtoCesLeg$ = dtoCesLegs.stream().filter(dtoCl-> Objects.equals(cesLeg.getParamCesLegalId(), dtoCl.getParamCesLegalId())).findFirst();

        cesLeg.setAccepte(correspondingDtoCesLeg$.isPresent() ? correspondingDtoCesLeg$.get().isAccepte() : false);
    }

    @Override
    public List<CedanteTraiteResp> getCedanteTraitelist(Long traiteNpId) {
        return cedTraiRepo.getCedanteTraitelist(traiteNpId);
    }

    @Override
    public List<ReadCedanteDTO> getListCedanteAsaisirSurTraite(Long traiteNpId)
    {
        return cedTraiRepo.getListCedanteAsaisirSurTraite(traiteNpId);
    }
}
