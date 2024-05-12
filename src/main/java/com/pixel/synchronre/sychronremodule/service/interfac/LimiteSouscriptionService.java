package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.dao.LimiteSouscriptionRepository;
import com.pixel.synchronre.sychronremodule.model.dao.TrancheRepository;
import com.pixel.synchronre.sychronremodule.model.dto.limitesouscription.LimiteSouscriptionReq;
import com.pixel.synchronre.sychronremodule.model.dto.limitesouscription.LimiteSouscriptionResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.LimiteSouscriptionMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.TrancheMapper;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheReq;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LimiteSouscriptionService implements IServiceLimiteSouscription
{
    private final ILogService logService;
    private final LimiteSouscriptionMapper lsMapper;
    private final ObjectCopier<LimiteSouscription> lsCopier;
    private final LimiteSouscriptionRepository lsRepo;
    @Override
    public LimiteSouscriptionResp save(LimiteSouscriptionReq dto)
    {
        if(dto.getLimiteSouscriptionId() == null) return this.create(dto);
        return this.update(dto);
    }

    @Override
    public boolean delete(Long limiteSouscriptionId)
    {
        if(limiteSouscriptionId == null) throw new AppException("Veuillez sélectionner la limite de souscription");
        LimiteSouscription limiteSouscription = lsRepo.findById(limiteSouscriptionId).orElseThrow(()->new AppException("Limite de souscription introuvable"));
        LimiteSouscription oldLimiteSouscription = lsCopier.copy(limiteSouscription);
        limiteSouscription.setStatut(new Statut("SUP"));
        logService.logg("Suppression d'une limite de souscription", oldLimiteSouscription, limiteSouscription, "LimiteSouscription");
        return true;
    }

    @Override
    public Page<LimiteSouscriptionResp> search(Long traiId, String key, Pageable pageable)
    {
        return lsRepo.search(traiId, key, pageable);
    }

    @Override @Transactional
    public LimiteSouscriptionResp create(LimiteSouscriptionReq dto)
    {
        LimiteSouscription limiteSouscription = lsMapper.mapToLimiteSouscription(dto);
        limiteSouscription = lsRepo.save(limiteSouscription);
        logService.logg("Création d'une limite de souscription", new LimiteSouscription(), limiteSouscription, "LimiteSouscription");
        return lsRepo.findLimiteSouscriptionRespById(dto.getLimiteSouscriptionId());
    }

    @Override @Transactional
    public LimiteSouscriptionResp update(LimiteSouscriptionReq dto)
    {
        if(dto.getLimiteSouscriptionId() == null) throw new AppException("Veuillez sélectionner la limite de souscription");
        LimiteSouscription limiteSouscription = lsRepo.findById(dto.getLimiteSouscriptionId()).orElseThrow(()->new AppException("Limite de souscription introuvable"));
        LimiteSouscription oldLimiteSouscription = lsCopier.copy(limiteSouscription);
        limiteSouscription.setLimSousMontant(dto.getLimSousMontant());
        limiteSouscription.setRisqueCouvert(new RisqueCouvert(dto.getRisqueId()));
        limiteSouscription.setCedanteTraite(new CedanteTraite(dto.getCedanteTraiteId()));
        limiteSouscription.setTranche(new Tranche(dto.getTrancheId()));
        logService.logg("Modification d'une limite de souscription", oldLimiteSouscription, limiteSouscription, "LimiteSouscription");
        return lsRepo.findLimiteSouscriptionRespById(dto.getLimiteSouscriptionId());
    }
}
