package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.dao.TerritorialiteDetailsRepository;
import com.pixel.synchronre.sychronremodule.model.dao.TerritorialiteRepository;
import com.pixel.synchronre.sychronremodule.model.dao.TraiteRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.TerritorialiteMapper;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceTerritorialite;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class TerritorialiteService implements IServiceTerritorialite
{
    private final TerritorialiteRepository terrRepo;
    private final ILogService logService;
    private final TerritorialiteMapper terrMapper;
    private final TraiteRepository traiRepo;
    private final TerritorialiteDetailsRepository terrDetRepo;
    private final ObjectCopier<Territorialite> terrCopier;
    @Override @Transactional
    public TerritorialiteResp create(TerritorialiteReq dto) throws UnknownHostException
    {
        TraiteNonProportionnel traite = traiRepo.findById(dto.getTraiteNPId()).orElseThrow(()->new AppException("Traité introuvable"));
        Territorialite territorialite = terrRepo.save(terrMapper.mapToTerritorialite(dto));
        logService.logg("Création d'une territorialité", null, territorialite, "Territorialite");
        dto.getPaysCodes().forEach(p->
        {
            TerritorialiteDetails terrDetails = terrDetRepo.save(new TerritorialiteDetails(null, new Pays(p), territorialite));
            try {
                logService.logg("Ajout d'un pays à une territorialité", null, terrDetails, "Territorialite");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });

        dto.getOrgCodes().forEach(o->
        {
            TerritorialiteDetails terrDetails = terrDetRepo.save(new TerritorialiteDetails(new Organisation(o), null, territorialite));
            try {
                logService.logg("Ajout d'une organisation à une territorialité", null, terrDetails, "TerritorialiteDetails");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });
        TerritorialiteResp territorialiteResp = terrMapper.mapToTerritorialiteResp(dto, traite);
        territorialiteResp.setTerrId(territorialite.getTerrId());
        return territorialiteResp;
    }

    @Override  @Transactional
    public TerritorialiteResp update(TerritorialiteReq dto) throws UnknownHostException {
        Territorialite territorialite = terrRepo.findById(dto.getTerrId()).orElseThrow(()->new AppException("Territorialité introuvable"));
        Territorialite oldTerritorialite = terrCopier.copy(territorialite);
        territorialite.setTerrLibelle(dto.getTerrLibelle());
        territorialite.setTerrTaux(dto.getTerrTaux());
        territorialite.setTerrDescription(dto.getTerrDescription());
        logService.logg("Modification d'une territorialité", oldTerritorialite, territorialite, "Territorialite");

        List<String> paysToRemove = terrDetRepo.getPaysCodesToRemove(dto.getTerrId(), dto.getPaysCodes());
        List<String> paysToAdd = terrDetRepo.getPaysCodesToAdd(dto.getTerrId(), dto.getPaysCodes());
        paysToRemove.forEach(paysCode-> {
            this.removePaysFromTerritorialite(dto.getTerrId(), paysCode);
        });
        paysToAdd.forEach(paysCode-> {
            this.addPaysToTerritorialite(dto.getTerrId(), paysCode);
        });

        List<String> orgsToRemove = terrDetRepo.getOrgCodesToRemove(dto.getTerrId(), dto.getOrgCodes());
        List<String> orgsToAdd = terrDetRepo.getOrgCodesToAdd(dto.getTerrId(), dto.getOrgCodes());
        orgsToRemove.forEach(orgCode-> {
            this.removeOrgFromTerritorialite(dto.getTerrId(), orgCode);
        });
        orgsToAdd.forEach(orgCode-> {
            this.addOrgToTerritorialite(dto.getTerrId(), orgCode);
        });

        return terrMapper.mapToTerritorialiteResp(dto, territorialite.getTraiteNonProportionnel());
    }

    private void addOrgToTerritorialite(Long terrId, String orgCode)
    {
        if(terrDetRepo.terrHasOrg(terrId, orgCode)) return ;
        Territorialite territorialite = terrRepo.findById(terrId).orElseThrow(()->new AppException("Territorialité introuvable"));
        TerritorialiteDetails terreDet = terrDetRepo.save(new TerritorialiteDetails(new Organisation(orgCode), null, territorialite));
        try {
            logService.logg("Ajout d'une organisation à une territorialité", null, terreDet, "TerritorialiteDetails");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private void removeOrgFromTerritorialite(Long terrId, String orgCode)
    {
        if(!terrDetRepo.terrHasOrg(terrId, orgCode)) return ;
        TerritorialiteDetails terreDet = terrDetRepo.findByTerrIdAndOrgCode(terrId, orgCode);
        terrDetRepo.deleteByTerrIdAndPOrgCode(terrId, orgCode);
        try {
            logService.logg("Suppression d'une organisation sur une territorialité", terreDet, new TerritorialiteDetails(), "TerritorialiteDetails");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override  @Transactional
    public Page<TerritorialiteResp> search(Long traiId, String key, Pageable pageable)
    {
        if(traiId == null || !traiRepo.existsById(traiId)) throw new AppException("Traité introuvable");
        Page<TerritorialiteResp> territorialitePage = terrRepo.search(traiId, key, pageable);
        List<TerritorialiteResp> territorialiteList = territorialitePage.stream().peek(t->
        {
            t.setPaysList(terrDetRepo.getPaysByTerrId(t.getTerrId()));
            t.setOrganisationList(terrDetRepo.getOrgCodesByTerrId(t.getTerrId()).stream()
                    .filter(Objects::nonNull).collect(java.util.stream.Collectors.joining(", ")));
        }).collect(Collectors.toList());

        return new PageImpl<>(territorialiteList, pageable, territorialitePage.getTotalElements());
    }

    private void addPaysToTerritorialite(Long terrId, String paysCode)
    {
        if(terrDetRepo.terrHasPays(terrId, paysCode)) return ;
        Territorialite territorialite = terrRepo.findById(terrId).orElseThrow(()->new AppException("Territorialité introuvable"));
        TerritorialiteDetails terreDet = terrDetRepo.save(new TerritorialiteDetails(null, new Pays(paysCode), territorialite));
        try {
            logService.logg("Ajout d'un pays à une territorialité", null, terreDet, "TerritorialiteDetails");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private void removePaysFromTerritorialite(Long terrId, String paysCode)
    {
        if(!terrDetRepo.terrHasPays(terrId, paysCode)) return ;
        TerritorialiteDetails terreDet = terrDetRepo.findByTerrIdAndPaysCode(terrId, paysCode);
        terrDetRepo.deleteByTerrIdAndPaysCode(terrId, paysCode);
        try {
            logService.logg("Suppression d'un pays sur une territorialité", terreDet, new TerritorialiteDetails(), "TerritorialiteDetails");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
