package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.dao.CouvertureRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RisqueCouvertRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.RisqueMapper;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.CreateRisqueCouvertReq;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.RisqueCouvertResp;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.UpdateRisqueCouvertReq;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceRisque;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class RisqueService implements IServiceRisque
{
    private final RisqueCouvertRepository  risqueRepo;
    private final RisqueMapper risqueMapper;
    private final ILogService logService;
    private final ObjectCopier<RisqueCouvert> risqueCopier;
    private final RisqueDetailsRepo risqueDetailsRepo;
    private final CouvertureRepository couRepo;

    @Override @Transactional
    public RisqueCouvertResp create(CreateRisqueCouvertReq dto)
    {
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
        risqueCouvert.setCouverture(new Couverture(dto.getCouId()));
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
    public Page<RisqueCouvertResp> search(Long traiId, String key, Pageable pageable)
    {
        key = StringUtils.stripAccentsToUpperCase(key);
        Page<RisqueCouvertResp> risquePage = risqueRepo.search(traiId, key, pageable);
        return risquePage;
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

    @Override
    public List<RisqueCouvertResp> getRisqueList(Long traiteNpId) {
        return risqueRepo.getRisqueList(traiteNpId);
    }

    private void addSousCouverture(RisqueCouvert risque, Long scId)
    {
        if(risqueDetailsRepo.risqueHasSousCouverture(risque.getRisqueId(), scId)) return;
        RisqueCouvertDetails risqueCouvertDetails = new RisqueCouvertDetails(risque, new Couverture(scId));
        risqueCouvertDetails = risqueDetailsRepo.save(risqueCouvertDetails);
        logService.logg("Ajout d'une sous couverture à un risque", new RisqueCouvertDetails(), risqueCouvertDetails, "RisqueCouvertDetails");
    }

    private void removeSousCouverture(RisqueCouvert risque, Long scId)
    {
        if(!risqueDetailsRepo.risqueHasSousCouverture(risque.getRisqueId(), scId)) return;
        RisqueCouvertDetails risqueCouvertDetails = risqueDetailsRepo.findByRisqueIdAndSousCouId(risque.getRisqueId(), scId);
        logService.logg("Retrait d'une sous couverture sur un risque", risqueCouvertDetails, new RisqueCouvertDetails(), "RisqueCouvertDetails");
        risqueDetailsRepo.deleteById(risqueCouvertDetails.getRisqueDetailsId());
    }
}