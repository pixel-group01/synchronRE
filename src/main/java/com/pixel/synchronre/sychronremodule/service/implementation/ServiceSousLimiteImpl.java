package com.pixel.synchronre.sychronremodule.service.implementation;


import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.CouvertureRepository;
import com.pixel.synchronre.sychronremodule.model.dao.SousLimiteCouvertureRepo;
import com.pixel.synchronre.sychronremodule.model.dao.SousLimiteRepository;
import com.pixel.synchronre.sychronremodule.model.dao.VSousLimiteRepository;
import com.pixel.synchronre.sychronremodule.model.dto.association.response.ActivitesResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.SousLimiteMapper;
import com.pixel.synchronre.sychronremodule.model.dto.souslimite.request.CreateSousLimiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.souslimite.request.UpdateSousLimite;
import com.pixel.synchronre.sychronremodule.model.dto.souslimite.response.SousLimiteDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.model.views.VSousLimite;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceSousLimite;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceSousLimiteImpl implements IServiceSousLimite {
  private final ILogService logService;
  private final SousLimiteRepository sslRepo;
  private final SousLimiteMapper sslMapper;
  private final ObjectCopier<SousLimite> sousLimiteCopier;
    private final TypeRepo typeRepo;
    private final CouvertureRepository couvertureRepo;
    private final SousLimiteCouvertureRepo slcRepo;
    private final VSousLimiteRepository vslRepo;

    @Override @Transactional
    public SousLimiteDetailsResp create(CreateSousLimiteReq dto) throws UnknownHostException {
        // 🔐 0. Normalisation des couIds (unicité)
        Set<Long> requestedCouIds = new HashSet<>(dto.getCouIds());

        // 1️⃣ Recherche d’une SousLimite existante avec EXACTEMENT ces couIds
        List<Long> existingSousLimiteIds =
                slcRepo.findSousLimiteIdsByExactCouIds(
                        dto.getTraiteNpId(),
                        requestedCouIds,
                        requestedCouIds.size()
                );

        if (!existingSousLimiteIds.isEmpty()) {

            SousLimite existing = sslRepo.findById(existingSousLimiteIds.get(0))
                    .orElseThrow(() -> new IllegalStateException("Sous-limite introuvable"));

            // 1️⃣.a Même montant → on ne fait rien
            if (existing.getSousLimMontant().compareTo(dto.getSousLimMontant()) == 0) {

                logService.logg(
                        "Sous-limite existante identique (aucune modification)",
                        null,
                        existing,
                        "SousLimite"
                );

                return sslMapper.mapToSousLimiteResp(existing);
            }

            // 1️⃣.b Même couvertures mais montant différent → update du montant uniquement
            existing.setSousLimMontant(dto.getSousLimMontant());
            sslRepo.save(existing);

            logService.logg(
                    "Mise à jour du montant de la sous-limite",
                    null,
                    existing,
                    "SousLimite"
            );

            return sslMapper.mapToSousLimiteResp(existing);
        }

        // 2️⃣ Aucune SousLimite identique → création complète
        SousLimite ssl = sslMapper.mapToSousLimite(dto);
        ssl = sslRepo.save(ssl);

        // 3️⃣ Type
        Type type = typeRepo.findByUniqueCode("SOU-LIM-COUV")
                .orElseThrow(() -> new IllegalArgumentException("Type SOU-LIM-COUV introuvable"));

        // 4️⃣ Chargement batch des couvertures
        List<Couverture> couvertures = couvertureRepo.findByCouIdIn(requestedCouIds);

        if (couvertures.size() != requestedCouIds.size()) {
            throw new IllegalArgumentException("Une ou plusieurs couvertures sont introuvables");
        }

        // 5️⃣ Création batch des associations
        final SousLimite finalSsl = ssl; // 🔥 obligatoire pour la lambda

        List<Association> associations = couvertures.stream()
                .map(c -> {
                    Association a = new Association();
                    a.setSousLimite(finalSsl);
                    a.setCouverture(c);
                    a.setType(type);
                    return a;
                })
                .toList();

        slcRepo.saveAll(associations);

        // 6️⃣ Log
        logService.logg(
                "Création d'une nouvelle sous-limite",
                null,
                ssl,
                "SousLimite"
        );

        return sslMapper.mapToSousLimiteResp(ssl);
    }




    @Override
    public Page<VSousLimite> search(String key, Long traiteNpId, Pageable pageable) {
        key = StringUtils.stripAccentsToUpperCase(key);

        Page<VSousLimite> sslResps = vslRepo.search(key, traiteNpId, pageable);
        return sslResps;
    }


    @Override @Transactional
    public SousLimiteDetailsResp update(UpdateSousLimite dto) throws UnknownHostException {
        SousLimite ssl = sslRepo.findById(dto.getSousLimiteSouscriptionId()).orElseThrow(()->new AppException("Sous-Limite introuvable"));
        SousLimite oldSsl = sousLimiteCopier.copy(ssl);
        BeanUtils.copyProperties(dto,ssl);
        ssl = sslRepo.save(ssl);
        // 3. Sécurisation des couIds (doublons front)
        Set<Long> requestedCouIds = new HashSet<>(dto.getCouIds());

        // 4. Récupération du Type
        Type type = typeRepo.findByUniqueCode("SOU-LIM-COUV")
                .orElseThrow(() -> new IllegalArgumentException("Type SOU-LIM-COUV introuvable"));

        Long typeId = type.getTypeId();

        // 5. ➕ Couvertures À AJOUTER
        List<Long> couIdsToAdd = slcRepo.findExistingCouIds(
                ssl.getSousLimiteSouscriptionId(),
                new ArrayList<>(requestedCouIds)
        );

        // 6. ➖ Couvertures À SUPPRIMER
        List<Long> couIdsToRemove = slcRepo.getCouIdsToRemove(
                ssl.getSousLimiteSouscriptionId(),
                new ArrayList<>(requestedCouIds)
        );

        // 7. Suppression des associations décochées
        for (Long couId : couIdsToRemove) {
            slcRepo.removeCouvertureOnSousLimite(
                    ssl.getSousLimiteSouscriptionId(),
                    couId,
                    typeId
            );
        }

        // 8. Ajout des nouvelles associations
        if (!couIdsToAdd.isEmpty()) {

            List<Couverture> couvertures = couvertureRepo.findByCouIdIn(couIdsToAdd);

            Map<Long, Couverture> couvertureMap = couvertures.stream()
                    .collect(Collectors.toMap(Couverture::getCouId, Function.identity()));

            if (couvertureMap.size() != couIdsToAdd.size()) {
                throw new IllegalArgumentException("Une ou plusieurs couvertures sont introuvables");
            }

            List<Association> associations = new ArrayList<>();

            for (Long couId : couIdsToAdd) {
                Association a = new Association();
                a.setSousLimite(ssl);
                a.setCouverture(couvertureMap.get(couId));
                a.setType(type);
                associations.add(a);
            }

            slcRepo.saveAll(associations);
        }
        logService.logg("Modification d'une sous-limite", oldSsl, ssl, "SousLimite");
        SousLimiteDetailsResp sslResp = sslMapper.mapToSousLimiteResp(ssl);
        return sslResp;
    }

    public UpdateSousLimite edit(Long sousLimiteSouscriptionId){
        return sslRepo.getEditDtoById(sousLimiteSouscriptionId);
    }

   public void delete(Long sousLimiteSouscriptionId){
       SousLimite sousLimite = sslRepo.findById(sousLimiteSouscriptionId).orElseThrow(()->new AppException("Sous-Limite introuvable"));
       SousLimite oldSousLimite = sousLimiteCopier.copy(sousLimite);
       sslRepo.delete(sousLimite);
       logService.logg(SynchronReActions.DELETE_SOUS_LIMITE, oldSousLimite, new SousLimite(), SynchronReTables.SOUS_LIMITE);
   }

    @Override
    public List<ActivitesResp> getActivites(Long traiteNpId) {
        return sslRepo.getActivite(traiteNpId);
    }
}
