package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.dao.CouvertureRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RisqueCouvertRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RisqueDetailsRepo;
import com.pixel.synchronre.sychronremodule.model.dto.association.response.ActivitesResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.RisqueMapper;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.CreateRisqueCouvertReq;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.RisqueCouvertResp;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.UpdateRisqueCouvertReq;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceRisque;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service @RequiredArgsConstructor
public class RisqueService implements IServiceRisque
{
    private final RisqueCouvertRepository  risqueRepo;
    private final RisqueMapper risqueMapper;
    private final ILogService logService;
    private final ObjectCopier<RisqueCouvert> risqueCopier;
    private final RisqueDetailsRepo risqueDetailsRepo;
    private final CouvertureRepository couRepo;
    private final TypeRepo typeRepo;

    @Override @Transactional
    public RisqueCouvertResp create(CreateRisqueCouvertReq dto)
    {
        Optional<RisqueCouvert> risqueCouvert$ = risqueRepo.findByTraiteAndCouverture(dto.getTraiteNpId(), dto.getCouId());
        if(risqueCouvert$.isPresent())
        {
            RisqueCouvert risqueCouvert = risqueCouvert$.get();
            risqueCouvert.setDescription(dto.getDescription());
            if(dto.getSousCouIds() != null && !dto.getSousCouIds().isEmpty())
            {
                List<Long> couIdsToAdd = risqueDetailsRepo.getCouIdsToAdd(risqueCouvert.getRisqueId(), dto.getSousCouIds());
                couIdsToAdd.forEach(scId->this.addSousCouverture(risqueCouvert, scId));
            }
            RisqueCouvertResp risqueCouvertResps = risqueRepo.getFullRisqueCouvertById(risqueCouvert.getRisqueId());
            risqueCouvertResps.setSousCouvertures(couRepo.getShortCouverturesByIds(dto.getSousCouIds()));
            return risqueCouvertResps;
        }
        RisqueCouvert risqueCouvert = risqueMapper.mapToRisqueCouvert(dto);
        risqueCouvert = risqueRepo.save(risqueCouvert);
        final RisqueCouvert finalRisqueCouvert = risqueCouvert;
        if(dto.getSousCouIds() != null && !dto.getSousCouIds().isEmpty())
        {
            dto.getSousCouIds().forEach(scId->this.addSousCouverture(finalRisqueCouvert, scId));
        }
        logService.logg("Création d'un risque", new RisqueCouvert(), risqueCouvert, "RisqueCouvert");
        RisqueCouvertResp risqueCouvertResps = risqueRepo.getFullRisqueCouvertById(risqueCouvert.getRisqueId());
        risqueCouvertResps.setSousCouvertures(couRepo.getShortCouverturesByIds(dto.getSousCouIds()));
        return risqueCouvertResps;
    }

    @Override @Transactional
    public RisqueCouvertResp update(UpdateRisqueCouvertReq dto)
    {
        RisqueCouvert risqueCouvert = risqueRepo.findById(dto.getRisqueId()).orElseThrow(()->new AppException("Risque introuvable"));
        RisqueCouvert oldRisqueCouvert = risqueCopier.copy(risqueCouvert);
        risqueCouvert.setDescription(dto.getDescription());
        logService.logg("Modification d'un risque", oldRisqueCouvert, risqueCouvert, "RisqueCouvert");
        final RisqueCouvert finalRisqueCouvert = risqueCouvert;
        if(dto.getSousCouIds() != null && !dto.getSousCouIds().isEmpty())
        {
            List<Long> couIdsToAdd = risqueDetailsRepo.getCouIdsToAdd(dto.getRisqueId(), dto.getSousCouIds());
            List<Long> couIdsToRemove = risqueDetailsRepo.getCouIdsToRemove(dto.getRisqueId(), dto.getSousCouIds());
            couIdsToAdd.forEach(scId->this.addSousCouverture(finalRisqueCouvert, scId));
            couIdsToRemove.forEach(scId->this.removeSousCouverture(finalRisqueCouvert, scId));
        }
        RisqueCouvertResp risqueCouvertResps = risqueRepo.getFullRisqueCouvertById(risqueCouvert.getRisqueId());
        risqueCouvertResps.setSousCouvertures(couRepo.getShortCouverturesByIds(dto.getSousCouIds()));
        return risqueCouvertResps;
    }

    @Override
    public Page<RisqueCouvertResp> search(Long traiteNpId, String key, Pageable pageable)
    {
        key = StringUtils.stripAccentsToUpperCase(key);
        Page<RisqueCouvertResp> risquePage = risqueRepo.search(traiteNpId, key, pageable);
        List<RisqueCouvertResp> risqueList = risquePage
                .stream()
                .filter(Objects::nonNull)
                .peek(t->t.setSousCouvertures(risqueRepo.getActivitesByrisqueId(t.getRisqueId())))
                .toList();
        return new PageImpl<>(risqueList, pageable, risquePage.getTotalElements());
    }


    @Override @Transactional
    public boolean delete(Long risqueId)
    {
        if(risqueId == null) throw new AppException("Veuillez fournir l'ID du risque introuvable");
        RisqueCouvert risqueCouvert = risqueRepo.findById(risqueId).orElseThrow(()->new AppException("Risque introuvable"));
        RisqueCouvert oldRisqueCouvert = risqueCopier.copy(risqueCouvert);
        risqueCouvert.setStatut(new Statut("SUP"));
        logService.logg("Suppression d'un risque", oldRisqueCouvert, risqueCouvert, "RisqueCouvert");
        return true;
    }

    @Override
    public UpdateRisqueCouvertReq getEditDto(Long risqueId)
    {
        UpdateRisqueCouvertReq dto = risqueRepo.getEditDto(risqueId);
        dto.setSousCouIds(risqueDetailsRepo.getSousCouIds(risqueId));
        return dto;
    }

    @Override //Affiche la liste des risques saisis à l'écran
    public List<RisqueCouvertResp> getRisqueList(Long traiteNpId) {
        return risqueRepo.getRisqueList(traiteNpId);
    }

    @Override
    public List<ActivitesResp> getActivites(Long risqueId) {
        return risqueRepo.getActivite(risqueId);
    }

    @Override //Renvoie la liste des couvertures parents qui ont au moins une sous couverture qui n'est pas sur une sous limite du traité
    public List<RisqueCouvertResp> getCouvertureParent(Long traiteNpId) {
        return risqueRepo.getCouvertureParent(traiteNpId);
    }

    private void addSousCouverture(RisqueCouvert risque, Long scId)
    {
        if(risqueDetailsRepo.risqueHasSousCouverture(risque.getRisqueId(), scId)) return;
        Type type = typeRepo.findByUniqueCode("RISQ-DET").orElseThrow(()->new AppException("Type d'association inconnu"));
        Association risqueCouvertDetails = new Association(risque, new Couverture(scId),type);
        risqueCouvertDetails = risqueDetailsRepo.save(risqueCouvertDetails);
        logService.logg("Ajout d'une sous couverture à un risque", new Association(), risqueCouvertDetails, "Association");
    }

    private void removeSousCouverture(RisqueCouvert risque, Long scId)
    {
        if(!risqueDetailsRepo.risqueHasSousCouverture(risque.getRisqueId(), scId)) return;
        Association risqueCouvertDetails = risqueDetailsRepo.findByRisqueIdAndSousCouId(risque.getRisqueId(), scId);
        logService.logg("Retrait d'une sous couverture sur un risque", risqueCouvertDetails, new Association(), "Association");
        risqueDetailsRepo.deleteById(risqueCouvertDetails.getAssoId());
    }
}