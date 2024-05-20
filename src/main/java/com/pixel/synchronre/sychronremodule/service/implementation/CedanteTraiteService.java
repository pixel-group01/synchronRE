package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.dao.*;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteResp;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CesLeg;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.CedanteTraiteMapper;
import com.pixel.synchronre.sychronremodule.model.entities.Cedante;
import com.pixel.synchronre.sychronremodule.model.entities.CedanteTraite;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCedanteTraite;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceRepartition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class CedanteTraiteService implements IServiceCedanteTraite
{
    private final CedanteTraiteRepository cedTraiRepo;
    private final IserviceRepartition repService;
    private final CedanteTraiteMapper cedTraiMapper;
    private final ILogService logService;
    private final ObjectCopier<CedanteTraite> cedTraiCopier;
    private final RepartitionTraiteRepo repTraiRepo;
    private final TraiteNPRepository traiteRepo;
    private final CedRepo cedRepo;

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
        cedanteTraite = cedTraiRepo.save(cedanteTraite);
        final Long cedTraiId = cedanteTraite.getCedanteTraiteId();
        logService.logg("Ajout d'une cédante sur un traité", new CedanteTraite(), cedanteTraite, "CedanteTraite");
        if(dto.getCessionsLegales() == null || dto.getCessionsLegales().isEmpty()) return cedTraiMapper.mapToCedanteTraiteResp(cedanteTraite);
        dto.getCessionsLegales().forEach(cesLeg->
        {
            repService.createRepartitionCesLegTraite(cesLeg, cedTraiId);
        });
        CedanteTraiteResp cedanteTraiteResp = cedTraiRepo.getCedanteTraiteRespById(cedanteTraite.getCedanteTraiteId());
        cedanteTraiteResp.setCessionsLegales(repTraiRepo.findCesLegsByCedTraiId(cedanteTraite.getCedanteTraiteId()));
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
        logService.logg("Modification d'une cédante sur un traité", oldCedanteTraite, cedanteTraite, "CedanteTraite");
        dto.getCessionsLegales().forEach(cesLeg->
        {
            repService.updateRepartitionCesLegTraite(cesLeg, dto.getCedanteTraiteId());
        });
        CedanteTraiteResp cedanteTraiteResp = cedTraiRepo.getCedanteTraiteRespById(dto.getCedanteTraiteId());
        cedanteTraiteResp.setCessionsLegales(repTraiRepo.findCesLegsByCedTraiId(dto.getCedanteTraiteId()));
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
        cedanteTraite.setStatut(new Statut("ACT"));
        logService.logg("Retrait d'une cédante sur un traité", oldCedanteTraite, cedanteTraite, "CedanteTraite");
        repTraiRepo.findCesLegIdsByCedTraiId(cedanteTraiteId).forEach(repId-> repService.annulerRepartition(repId));
    }

    @Override
    public CedanteTraiteReq getEditDto(Long cedanteTraiteId)
    {
        CedanteTraiteReq dto = cedTraiRepo.getEditDto(cedanteTraiteId);
        dto.setCessionsLegales(repTraiRepo.findCesLegsByCedTraiId(cedanteTraiteId));
        return dto;
    }

    @Override
    public CedanteTraiteReq getEditDto(Long traiteNpId, Long cedId)
    {
        if(traiteNpId == null || !traiteRepo.existsById(traiteNpId)) throw new AppException("Traité non proportionnel introuvable");
        if(cedId == null || !cedRepo.existsById(cedId)) throw new AppException("Cédante introuvable");
        CedanteTraiteReq dto = new CedanteTraiteReq(traiteNpId, cedId);
        dto.setCessionsLegales(repTraiRepo.findCesLegsByTraiIdAndCedId(traiteNpId, cedId));
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
}
