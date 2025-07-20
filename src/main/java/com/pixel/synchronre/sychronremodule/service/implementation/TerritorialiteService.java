package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.dao.TerritorialiteDetailsRepository;
import com.pixel.synchronre.sychronremodule.model.dao.TerritorialiteRepository;
import com.pixel.synchronre.sychronremodule.model.dao.TraiteRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.TerritorialiteMapper;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceTerritorialite;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.ArrayList;
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
    private final TypeRepo typeRepo;
    @Override @Transactional
    public TerritorialiteResp create(TerritorialiteReq dto)
    {
        Type type = typeRepo.findByUniqueCode("TER-DET").orElseThrow(()->new AppException("Type d'association inconnu"));
        TraiteNonProportionnel traite = traiRepo.findById(dto.getTraiteNpId()).orElseThrow(()->new AppException("Traité introuvable"));
        Territorialite territorialite = terrRepo.save(terrMapper.mapToTerritorialite(dto));
        logService.logg("Création d'une territorialité", null, territorialite, "Association");
        if(dto.getPaysCodes() == null || dto.getPaysCodes().isEmpty())  throw new AppException("Veuillez sélectionner les pays");
        dto.getPaysCodes().stream().filter(code->code != null && !code.trim().equals("")).distinct().forEach(p->
        {
            Association terrDetails = terrDetRepo.save(new Association(null, new Pays(p), territorialite,type));
            logService.logg("Ajout d'un pays à une territorialité", null, terrDetails, "Association");
        });

        if(dto.getOrgCodes() != null && !dto.getOrgCodes().isEmpty())
        {
            dto.getOrgCodes().stream().distinct().forEach(o->
            {

                Association terrDetails = terrDetRepo.save(new Association(new Organisation(o), null, territorialite,type));
                logService.logg("Ajout d'une organisation à une territorialité", null, terrDetails, "Association");
            });
        }
        TerritorialiteResp territorialiteResp = terrMapper.mapToTerritorialiteResp(dto, traite);
        territorialiteResp.setTerrId(territorialite.getTerrId());
        return territorialiteResp;
    }

    @Override  @Transactional
    public TerritorialiteResp update(TerritorialiteReq dto){
        if(dto.getOrgCodes() == null) dto.setOrgCodes(new ArrayList<>());
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
        Type type = typeRepo.findByUniqueCode("TER-DET").orElseThrow(()->new AppException("Type d'association inconnu"));
        Territorialite territorialite = terrRepo.findById(terrId).orElseThrow(()->new AppException("Territorialité introuvable"));
        Association terreDet = terrDetRepo.save(new Association(new Organisation(orgCode), null, territorialite,type));
        logService.logg("Ajout d'une organisation à une territorialité", null, terreDet, "Association");
    }

    private void removeOrgFromTerritorialite(Long terrId, String orgCode)
    {
        if(!terrDetRepo.terrHasOrg(terrId, orgCode)) return ;
        Association terreDet = terrDetRepo.findByTerrIdAndOrgCode(terrId, orgCode);
        terrDetRepo.deleteByTerrIdAndPOrgCode(terrId, orgCode);
        logService.logg("Suppression d'une organisation sur une territorialité", terreDet, new Association(), "Association");
    }

    @Override  @Transactional
    public Page<TerritorialiteResp> search(Long traiId, String key, Pageable pageable)
    {
        key = StringUtils.stripAccentsToUpperCase(key);
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
        Type type = typeRepo.findByUniqueCode("TER-DET").orElseThrow(()->new AppException("Type d'association inconnu"));
        Territorialite territorialite = terrRepo.findById(terrId).orElseThrow(()->new AppException("Territorialité introuvable"));
        Association terreDet = terrDetRepo.save(new Association(null, new Pays(paysCode), territorialite,type));
        logService.logg("Ajout d'un pays à une territorialité", null, terreDet, "Association");
    }

    private void removePaysFromTerritorialite(Long terrId, String paysCode)
    {
        if(!terrDetRepo.terrHasPays(terrId, paysCode)) return ;
        Association terreDet = terrDetRepo.findByTerrIdAndPaysCode(terrId, paysCode);
        terrDetRepo.deleteByTerrIdAndPaysCode(terrId, paysCode);
        logService.logg("Suppression d'un pays sur une territorialité", terreDet, new Association(), "Association");
    }
    @Override
    public TerritorialiteReq edit(Long terrId){
        return terrRepo.getEditDtoById(terrId);

    }
}
