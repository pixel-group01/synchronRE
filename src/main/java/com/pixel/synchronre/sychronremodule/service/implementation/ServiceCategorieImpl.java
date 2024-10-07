package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.dao.*;
import com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieReq;
import com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieResp;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.CategorieMapper;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.RisqueCouvertResp;
import com.pixel.synchronre.sychronremodule.model.dto.traite.response.TraiteNPResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCategorie;
import com.pixel.synchronre.sychronremodule.service.interfac.ITrancheCedanteService;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.entities.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class ServiceCategorieImpl implements IServiceCategorie
{
    private final CategorieRepository catRepo;
    private final CategorieCedanteRepository catCedRepo;
    private final ILogService logService;
    private final CategorieMapper catMapper;
    private final TraiteNPRepository traiRepo;
    private final CedRepo cedRepo;
    private final IJwtService jwtService;
    private final ObjectCopier<Association> catCedCopier;
    private final ObjectCopier<Categorie> catCopier;
    private final TypeRepo typeRepo;
    private final ITrancheCedanteService trancheCedanteService;

    @Override @Transactional
    public CategorieResp create(CategorieReq dto){
        Categorie categorie = catMapper.mapToCategorie(dto);
        categorie = catRepo.save(categorie);
        final Long catId = categorie.getCategorieId();
        logService.logg("Création d'une catégorie", new Categorie(), categorie, "Categorie");
        dto.getCedIds().forEach(cedId->
        {
            this.addCedanteToCategorie(cedId, catId);
        });
        TraiteNPResp traiteNPResp = traiRepo.getShortTraiteById(dto.getTraiteNpId());
        List<ReadCedanteDTO> cedantes = cedRepo.getShortCedantesByIds(dto.getCedIds());
        return catMapper.mapToCategorieResp(categorie, traiteNPResp, cedantes);
    }

    @Override @Transactional
    public CategorieResp update(CategorieReq dto)
    {
        if(dto.getCategorieId() == null) throw new AppException("Veuillez sélectionner la catégorie à modifier");
        Categorie categorie = catRepo.findById(dto.getCategorieId()).orElseThrow(()->new AppException("Catégorie introuvable"));
        Categorie oldCategorie = catCopier.copy(categorie);
        categorie.setCategorieLibelle(dto.getCategorieLibelle());
        categorie.setCategorieCapacite(dto.getCategorieCapacite());
        logService.logg("Modification d'une catégorie", oldCategorie, categorie, "Categorie");
        TraiteNPResp traiteNPResp = traiRepo.getShortTraiteById(dto.getTraiteNpId());
        List<ReadCedanteDTO> cedantes = cedRepo.getShortCedantesByIds(dto.getCedIds());
        CategorieResp categorieResp = catMapper.mapToCategorieResp(categorie, traiteNPResp, cedantes);
        if(dto.getCedIds() == null) return categorieResp;
        List<Long> cedIdsToAdd = catCedRepo.getCedIdsToAdd(dto.getCategorieId(), dto.getCedIds());
        List<Long> cedIdsToRemove = catCedRepo.getCedIdsToRemove(dto.getCategorieId(), dto.getCedIds());
        cedIdsToAdd.forEach(cedId->this.addCedanteToCategorie(cedId, dto.getCategorieId()));
        cedIdsToRemove.forEach(cedId->this.removeCedanteToCategorie(cedId, dto.getCategorieId()));
        return categorieResp;
    }

    @Override @Transactional
    public CategorieResp save(CategorieReq dto)
    {
        if(dto.getCategorieId() == null) return this.create(dto);
        return this.update(dto);
    }

    @Override
    public boolean delete(Long catId)
    {
        if(catId == null) throw new AppException("Veuillez sélectionner la catégorie à supprimer");
        Categorie categorie = catRepo.findById(catId).orElseThrow(()->new AppException("Catégorie introuvable"));
        Categorie oldCategorie = catCopier.copy(categorie);
        categorie.setStatut(new Statut("SUP"));
        logService.logg("Suppression d'une catégorie", oldCategorie, categorie, "Categorie");
        return true;
    }

    @Override
    public Page<CategorieResp> search(Long traiId, String key, Pageable pageable)
    {
        key = StringUtils.stripAccentsToUpperCase(key);
        Page<CategorieResp> categoriesPage = catRepo.search(traiId, key, pageable);
        List<CategorieResp> categoriesList = categoriesPage.stream()
                .peek(c->c.setCedantes(catCedRepo.getShortCedantesByCatId(c.getCategorieId())))
                .toList();
        return new PageImpl<>(categoriesList, pageable, categoriesPage.getTotalElements());
    }

    @Override
    public List<CategorieResp> getCategorieList(Long traiteNpId) {
        List<CategorieResp> categorieListe = catRepo.getCategorieList(traiteNpId)
                .stream()
                .filter(Objects::nonNull)
                .peek(this::setLibellesCedantes)
                .toList();
        return categorieListe;
    }

    private void removeCedanteToCategorie(Long cedId, Long categorieId)
    {
        if(!catCedRepo.exitsByCedIdAndCatId(cedId, categorieId)) return;
        Association categorieCedante = catCedRepo.findByCedIdAndCatId(cedId, categorieId);
        Association oldCategorieCedante = catCedCopier.copy(categorieCedante);
        catCedRepo.deleteByCedIdAndCatId(cedId, categorieId);
        logService.logg("Retrait d'une cédante sur une catégorie", categorieCedante, new Association(), "Association");
        trancheCedanteService.onAddOrRemoveCedanteToCategorie(cedId, categorieId);
    }

    private void addCedanteToCategorie(Long cedId, Long catId)
    {
        if(catCedRepo.exitsByCedIdAndCatId(cedId, catId)) return;
        Type type = typeRepo.findByUniqueCode("CAT-CED").orElseThrow(()->new AppException("Type d'assocition introuvable"));
        Association categorieCedante = new Association(new Categorie(catId), new Cedante(cedId),type);
        categorieCedante = catCedRepo.save(categorieCedante);
        logService.logg("Ajout d'une cédante à une catégorie", new Association(), categorieCedante, "Association");
        trancheCedanteService.onAddOrRemoveCedanteToCategorie(cedId, catId);
    }

    private void setLibellesCedantes(CategorieResp c) {
        List<String> libellesCedantes = catCedRepo.getLibellesCedantesByCatId(c.getCategorieId());
        String concatLibellesCedantes = libellesCedantes == null ? "" : libellesCedantes.stream().collect(Collectors.joining(", "));
        c.setLibellesCedantes(concatLibellesCedantes);
    }
}