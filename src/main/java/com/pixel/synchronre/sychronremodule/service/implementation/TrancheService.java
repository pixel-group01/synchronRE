package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.dao.TrancheCategorieRepository;
import com.pixel.synchronre.sychronremodule.model.dao.TrancheRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.TrancheMapper;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheReq;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceTranche;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class TrancheService implements IServiceTranche
{
    private final TrancheRepository trancheRepo;
    private final ILogService logService;
    private final TrancheMapper trancheMapper;
    private final ObjectCopier<Tranche> trancheCopier;
    private final TrancheCategorieRepository trancheCatRepo;
    @Override
    public TrancheResp save(TrancheReq dto)
    {
        if(dto.getTrancheId() == null) return this.create(dto);
        return this.update(dto);
    }

    @Override @Transactional
    public boolean delete(Long trancheId) {
        if(trancheId == null) throw new AppException("Veuillez sélectionner la catégorie à supprimer");
        Tranche tranche = trancheRepo.findById(trancheId).orElseThrow(()->new AppException("Tranche introuvable"));
        Tranche oldTrancheId = trancheCopier.copy(tranche);
        tranche.setStatut(new Statut("SUP"));
        logService.logg("Suppression d'une tranche", oldTrancheId, tranche, "Tranche");
        return true;
    }

    @Override
    public Page<TrancheResp> search(Long traiId, String key, Pageable pageable)
    {
        key = StringUtils.stripAccentsToUpperCase(key);
        Page<TrancheResp> trancheRespPage = trancheRepo.search(traiId, key, pageable);
        return trancheRespPage;
    }

    @Override @Transactional
    public TrancheResp create(TrancheReq dto)
    {
        Tranche tranche = trancheMapper.mapToTranche(dto);
        tranche = trancheRepo.save(tranche);
        logService.logg("Création d'une tranche", new Tranche(), tranche, "Tranche");
        final Tranche finalTranche = tranche;
        if(dto.getCategorieIds() != null)
        {
            dto.getCategorieIds().forEach(catCedId->this.addCategorie(finalTranche, catCedId));
        }
        return trancheRepo.getTrancheResp(dto.getTrancheId());
    }

    @Override @Transactional
    public TrancheResp update(TrancheReq dto)
    {
        if(dto.getRisqueId() == null) throw new AppException("Veuillez sélectionner la tranche à modifier");
        Tranche tranche = trancheRepo.findById(dto.getTrancheId()).orElseThrow(()->new AppException("Tranche introuvable"));
        Tranche oldTranche = trancheCopier.copy(tranche);
        BeanUtils.copyProperties(dto, tranche);
        tranche.setRisqueCouvert(new RisqueCouvert(dto.getRisqueId()));
        logService.logg("Modification d'une tranche", oldTranche, tranche, "Tranche");
        final Tranche finalTranche = tranche;
        if(dto.getCategorieIds() != null )
        {
            List<Long> catIdsToAdd = trancheCatRepo.getCatIdsToAdd(dto.getTrancheId(), dto.getCategorieIds());
            List<Long> catIdsToRemove = dto.getCategorieIds().isEmpty() ?
                    trancheCatRepo.getCatIdsByTrancheId(tranche.getTrancheId()) :
                    trancheCatRepo.getCatIdsToRemove(dto.getTrancheId(), dto.getCategorieIds());

            catIdsToAdd.forEach(catCedId->this.addCategorie(finalTranche, catCedId));
            catIdsToRemove.forEach(catCedId->this.removeCategorie(finalTranche, catCedId));
        }
        return trancheRepo.getTrancheResp(dto.getTrancheId());
    }

    private void removeCategorie(Tranche tranche, Long catId)
    {
        if(!trancheCatRepo.trancheHasCat(tranche.getTrancheId(),catId )) return ;
        TrancheCategorie trancheCategorie = trancheCatRepo.findByTrancheIdAndCatId(tranche.getTrancheId(), catId);
        logService.logg("Retrait d'une catégorie à une tranche", new TrancheCategorie(), trancheCategorie, "TrancheCategorie");
        trancheCatRepo.deleteById(trancheCategorie.getTrancheCategorieId());
    }

    private void addCategorie(Tranche tranche, Long catId)
    {
        if(trancheCatRepo.trancheHasCat(tranche.getTrancheId(),catId )) return ;
        //TODO Vérifier que la catégorie appartient au traité
        TrancheCategorie trancheCategorie = trancheCatRepo.save(new TrancheCategorie(null, tranche, new Categorie(catId)));
        logService.logg("Ajout d'une catégorie à une tranche", new TrancheCategorie(), trancheCategorie, "TrancheCategorie");
    }

    @Override
    public TrancheReq edit(Long trancheId)
    {
        TrancheReq dto = trancheRepo.getEditDtoById(trancheId);
        if(dto != null) dto.setCategorieIds(trancheCatRepo.getCatIdsByTrancheId(trancheId));
        return trancheRepo.getEditDtoById(trancheId);
    }
}