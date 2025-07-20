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
        List<LimiteSouscription> limiteSouscriptions = lsRepo.findByRisqueIdAndCatId(dto.getRisqueId(), dto.getCategorieId());
        /**
         * J'ai une contrainte métier selon laquelle le même couId ne peut pas être associé
         * à deux limite de souscription différentes ayant le même risqueId et le même catId.
         * Donc, si dans la liste des couIds, il y a un seul qui est déjà associé
         * à une limite de souscription du même risqueId et du même catId que celui du dto,
         * On évite de créer une nouvelle limite de souscription. Dans ce caas,
         * on récupère la limite de souscription en question puis on met à jour ses couIds
         * Si par contre, il existe au moins deux couIds qui sont associés à des limites de soucription différentes
         * mais qui ont le même risqueId et la même catId lever une exception
         */
        LimiteSouscription limiteSouscription;
        if(!limiteSouscriptions.isEmpty())
        {
            // Map to track which LimiteSouscription each couId is associated with
            java.util.Map<Long, LimiteSouscription> couIdToLimiteSouscription = new java.util.HashMap<>();

            // For each couId in the input list, check if it's already associated with any LimiteSouscription
            for(Long couId : couIds)
            {
                for(LimiteSouscription ls : limiteSouscriptions)
                {
                    // Check if the couId is already associated with this LimiteSouscription
                    // We use getCouIdsToAdd to check if the couId needs to be added (i.e., it's not already associated)
                    List<Long> couIdsToAdd = lscRepo.getCouIdsToAdd(ls.getLimiteSouscriptionId(), java.util.List.of(couId));

                    // If couIdsToAdd is empty, it means the couId is already associated with this LimiteSouscription
                    if(couIdsToAdd.isEmpty())
                    {
                        couIdToLimiteSouscription.put(couId, ls);
                        break;
                    }
                }
            }

            // If no couIds are associated with any LimiteSouscription, create a new one
            if(!couIdToLimiteSouscription.isEmpty())
            {
                // Check if all couIds are associated with the same LimiteSouscription
                java.util.Set<LimiteSouscription> uniqueLimiteSouscriptions = new java.util.HashSet<>(couIdToLimiteSouscription.values());

                if(uniqueLimiteSouscriptions.size() > 1)
                {
                    // If couIds are associated with different LimiteSouscriptions, throw an exception
                    throw new AppException("Impossible de créer une limite de souscription car certains couIds sont déjà associés à différentes limites de souscription avec le même risqueId et categorieId");
                }
                else
                {
                    // If all couIds are associated with the same LimiteSouscription, update that LimiteSouscription
                    LimiteSouscription existingLimiteSouscription = uniqueLimiteSouscriptions.iterator().next();
                    dto.setLimiteSouscriptionId(existingLimiteSouscription.getLimiteSouscriptionId());
                    return this.update(dto);
                }
            }
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
