package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.dao.CedanteTraiteRepository;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.CedanteTraiteMapper;
import com.pixel.synchronre.sychronremodule.model.entities.Cedante;
import com.pixel.synchronre.sychronremodule.model.entities.CedanteTraite;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCedanteTraite;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceRepartition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class CedanteTraiteService implements IServiceCedanteTraite
{
    private final CedanteTraiteRepository cedTraiRepo;
    private final IserviceRepartition repService;
    private final CedanteTraiteMapper cedTraiMapper;
    private final ILogService logService;
    private final ObjectCopier<CedanteTraite> cedTraiCopier;

    @Override @Transactional
    public CedanteTraiteResp create(CedanteTraiteReq dto)
    {
        CedanteTraite cedanteTraite = cedTraiMapper.mapToCedanteTraite(dto);
        cedanteTraite = cedTraiRepo.save(cedanteTraite);
        logService.logg("Ajout d'une cédante sur un traité", new CedanteTraite(), cedanteTraite, "CedanteTraite");
        dto.getCessionsLegales().forEach(cesLeg->
        {
            repService.createRepartitionCesLegTraite(cesLeg);
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
}
