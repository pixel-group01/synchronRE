package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.dao.CategorieCedanteRepository;
import com.pixel.synchronre.sychronremodule.model.dao.LimiteSouscriptionCouvertureRepo;
import com.pixel.synchronre.sychronremodule.model.dao.LimiteSouscriptionRepository;
import com.pixel.synchronre.sychronremodule.model.dto.limitesouscription.LimiteSouscriptionReq;
import com.pixel.synchronre.sychronremodule.model.dto.limitesouscription.LimiteSouscriptionResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.LimiteSouscriptionMapper;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceLimiteSouscription;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LimiteSouscriptionService implements IServiceLimiteSouscription
{
    private final ILogService logService;
    private final LimiteSouscriptionMapper lsMapper;
    private final ObjectCopier<LimiteSouscription> lsCopier;
    private final LimiteSouscriptionRepository lsRepo;
    private final CategorieCedanteRepository catCedRepo;
    private final TypeRepo typeRepo;
    private final LimiteSouscriptionCouvertureRepo lscRepo;

    @Override
    public LimiteSouscriptionResp save(LimiteSouscriptionReq dto)
    {
        if(dto.getLimiteSouscriptionId() == null) return this.create(dto);
        return this.update(dto);
    }

    @Override @Transactional
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
    public Page<LimiteSouscriptionResp> search(Long traiteNpId, String key, Pageable pageable)
    {
        Page<LimiteSouscriptionResp> lsPage = lsRepo.search(traiteNpId, key, pageable);
        List<LimiteSouscriptionResp> lsList = lsPage.stream().peek(this::setLibellesCedantes).toList();
        return new PageImpl(lsList, pageable, lsPage.getTotalElements());
    }

    @Override @Transactional
    public LimiteSouscriptionResp create(LimiteSouscriptionReq dto)
    {
        List<Long> couIds = dto.getCouIds();
        couIds = couIds == null ? List.of() : couIds;
        Optional<LimiteSouscription> limiteSouscription$ = lsRepo.findByRisqueIdAndCatId(dto.getRisqueId(), dto.getCategorieId());
        LimiteSouscription limiteSouscription;
        if(limiteSouscription$.isPresent())
        {
            limiteSouscription = limiteSouscription$.get();
            dto.setLimiteSouscriptionId(limiteSouscription.getLimiteSouscriptionId());
            return this.update(dto);
        }
        limiteSouscription = lsMapper.mapToLimiteSouscription(dto);
        limiteSouscription.setLimSousMontant(dto.getLimSousMontant());
        limiteSouscription = lsRepo.save(limiteSouscription);
        final LimiteSouscription finalLimiteSouscription = limiteSouscription;
        Type type = typeRepo.findByUniqueCode("LIM-SOU-COUV").orElseThrow(()->new AppException("Type inconnu"));
        couIds.forEach(couId ->
        {
            this.addCouvertureToLimite(finalLimiteSouscription, couId, type);
        });
        logService.logg("Création d'une limite de souscription", new LimiteSouscription(), limiteSouscription, "LimiteSouscription");
        return lsRepo.findLimiteSouscriptionRespById(dto.getLimiteSouscriptionId());
    }

    @Override @Transactional
    public LimiteSouscriptionResp update(LimiteSouscriptionReq dto)
    {
        List<Long> couIds = dto.getCouIds();
        couIds = couIds == null ? List.of() : couIds;
        
        if(dto.getLimiteSouscriptionId() == null) throw new AppException("Veuillez sélectionner la limite de souscription");
        LimiteSouscription limiteSouscription = lsRepo.findById(dto.getLimiteSouscriptionId()).orElseThrow(()->new AppException("Limite de souscription introuvable"));
        LimiteSouscription oldLimiteSouscription = lsCopier.copy(limiteSouscription);
        limiteSouscription.setLimSousMontant(dto.getLimSousMontant());
        limiteSouscription.setRisqueCouvert(new RisqueCouvert(dto.getRisqueId()));
        limiteSouscription.setCategorie(new Categorie(dto.getCategorieId()));
        
        List<Long> couIdsToRemove = lscRepo.getCouIdsToRemove(limiteSouscription.getLimiteSouscriptionId(), couIds);
        List<Long> couIdsToAdd = lscRepo.getCouIdsToAdd(limiteSouscription.getLimiteSouscriptionId(), couIds);

        Type type = typeRepo.findByUniqueCode("LIM-SOU-COUV").orElseThrow(()->new AppException("Type inconnu"));
        couIdsToRemove.forEach(couId-> lscRepo.removeCouvertureOnLimite(limiteSouscription.getLimiteSouscriptionId(), couId));
        couIdsToAdd.forEach(couId ->this.addCouvertureToLimite(limiteSouscription, couId, type));
        logService.logg("Modification d'une limite de souscription", oldLimiteSouscription, limiteSouscription, "LimiteSouscription");
        return lsRepo.findLimiteSouscriptionRespById(dto.getLimiteSouscriptionId());
    }

    private void addCouvertureToLimite(LimiteSouscription limiteSouscription, Long couId, Type type)
    {
        Association association = new Association(limiteSouscription, new Couverture(couId), type);
        lscRepo.save(association);
    }

    @Override
    public LimiteSouscriptionReq edit(Long limiteSouscriptionId){
        return lsRepo.getEditDtoById(limiteSouscriptionId);
    }

    private void setLibellesCedantes(LimiteSouscriptionResp ls) {
        List<String> libellesCedantes = catCedRepo.getLibellesCedantesByCatId(ls.getCategorieId());
        String concatLibellesCedantes = libellesCedantes == null ? "" : libellesCedantes.stream().collect(Collectors.joining(", "));
        ls.setLibellesCedantes(concatLibellesCedantes);
    }
}
