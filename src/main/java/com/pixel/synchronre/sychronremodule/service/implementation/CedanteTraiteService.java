package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.dao.CedanteTraiteRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteResp;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CesLeg;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.CedanteTraiteMapper;
import com.pixel.synchronre.sychronremodule.model.entities.Cedante;
import com.pixel.synchronre.sychronremodule.model.entities.CedanteTraite;
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
    private final RepartitionRepository repRepo;

    @Override @Transactional
    public CedanteTraiteResp create(CedanteTraiteReq dto)
    {
        CedanteTraite cedanteTraite = cedTraiMapper.mapToCedanteTraite(dto);
        final Long cedTraiId = cedanteTraite.getCedanteTraiteId();
        cedanteTraite = cedTraiRepo.save(cedanteTraite);
        logService.logg("Ajout d'une cédante sur un traité", new CedanteTraite(), cedanteTraite, "CedanteTraite");
        dto.getCessionsLegales().forEach(cesLeg->
        {
            repService.createRepartitionCesLegTraite(cesLeg, cedTraiId);
        });
        return cedTraiMapper.mapToCedanteTraiteResp(cedanteTraite);
    }

    @Override @Transactional
    public CedanteTraiteResp update(CedanteTraiteReq dto)
    {
        if(dto.getCedanteTraiteId() == null) throw new AppException("CedanteTraite null");
        CedanteTraite cedanteTraite = cedTraiRepo.findById(dto.getCedanteTraiteId()).orElseThrow(()->new AppException("CedanteTraite introuvable"));
        CedanteTraite oldCedanteTraite = cedTraiCopier.copy(cedanteTraite);
        cedanteTraite = cedTraiRepo.save(cedanteTraite);
        cedanteTraite.setCedante(new Cedante(dto.getCedId()));
        cedanteTraite.setAssiettePrime(dto.getAssiettePrime());
        cedanteTraite.setPmd(dto.getPmd());
        cedanteTraite.setTauxPrime(dto.getTauxPrime());
        logService.logg("Modification d'une cédante sur un traité", oldCedanteTraite, cedanteTraite, "CedanteTraite");
        dto.getCessionsLegales().forEach(cesLeg->
        {
            repService.updateRepartitionCesLegTraite(cesLeg);
        });
        return cedTraiMapper.mapToCedanteTraiteResp(cedanteTraite);
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
        Page<CedanteTraiteResp> cedanteTraitePage = cedTraiRepo.search(traiId, key, pageable);
        List<CedanteTraiteResp> cedanteTraiteList = cedanteTraitePage.stream()
                .peek(cedTrai-> {
                    List<CesLeg> repartitions = repRepo.findByCedTraiId(cedTrai.getCedanteTraiteId());

                    cedTrai.setCessionsLegales(repartitions);
                }).toList();

        return new PageImpl<>(cedanteTraiteList, pageable, cedanteTraitePage.getTotalElements());
    }
}
