package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.dao.RisqueCouvertRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.RisqueMapper;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.CreateRisqueCouvertReq;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.RisqueCouvertResp;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.UpdateRisqueCouvertReq;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteResp;
import com.pixel.synchronre.sychronremodule.model.entities.Activite;
import com.pixel.synchronre.sychronremodule.model.entities.Couverture;
import com.pixel.synchronre.sychronremodule.model.entities.RisqueCouvert;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceRisque;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.List;

@Service @RequiredArgsConstructor
public class RisqueService implements IServiceRisque
{
    private final RisqueCouvertRepository  risqueRepo;
    private final RisqueMapper risqueMapper;
    private final ILogService logService;
    private final ObjectCopier<RisqueCouvert> risqueCopier;
    @Override @Transactional
    public RisqueCouvertResp create(CreateRisqueCouvertReq dto) throws UnknownHostException {
        RisqueCouvert risqueCouvert = risqueMapper.mapToRisqueCouvert(dto);
        risqueCouvert = risqueRepo.save(risqueCouvert);
        logService.logg("Création d'un risque", new RisqueCouvert(), risqueCouvert, "RisqueCouvert");
        return risqueMapper.mapToRisqueCouvertResp(risqueCouvert);
    }

    @Override @Transactional
    public RisqueCouvertResp update(UpdateRisqueCouvertReq dto) throws UnknownHostException {
        RisqueCouvert risqueCouvert = risqueRepo.findById(dto.getRisqueId()).orElseThrow(()->new AppException("Risque introuvable"));
        RisqueCouvert oldRisqueCouvert = risqueCopier.copy(risqueCouvert);
        risqueCouvert.setCouverture(new Couverture(dto.getCouId()));
        risqueCouvert.setActivite(new Activite(dto.getActiviteId()));
        risqueCouvert.setDescription(dto.getDescription());
        logService.logg("Modification d'un risque", oldRisqueCouvert, risqueCouvert, "RisqueCouvert");
        return risqueMapper.mapToRisqueCouvertResp(risqueCouvert);
    }

    @Override
    public Page<RisqueCouvertResp> search(Long traiId, String key, Pageable pageable)
    {
        Page<RisqueCouvertResp> risquePage = risqueRepo.search(traiId, key, pageable);
        return risquePage;
    }

    @Override @Transactional
    public boolean delete(Long risqueId) throws UnknownHostException {
        if(risqueId == null) throw new AppException("Veuillez fournir l'ID du risque introuvable");
        RisqueCouvert risqueCouvert = risqueRepo.findById(risqueId).orElseThrow(()->new AppException("Risque introuvable"));
        RisqueCouvert oldRisqueCouvert = risqueCopier.copy(risqueCouvert);
        risqueCouvert.setStatut(new Statut("SUP"));
        logService.logg("Suppression d'un risque", oldRisqueCouvert, risqueCouvert, "RisqueCouvert");
        return false;
    }
}
