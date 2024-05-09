package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.dao.*;
import com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieReq;
import com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.CategorieMapper;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteMapper;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCategorie;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceTerritorialite;
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
    private final CategorieTraiteRepository catTraiRepo;
    private final ILogService logService;
    private final CategorieMapper catMapper;
    private final TraiteRepository traiRepo;
    private final TerritorialiteDetailsRepository terrDetRepo;
    private final ObjectCopier<Territorialite> terrCopier;

    @Override
    public CategorieResp create(CategorieReq dto) throws UnknownHostException {
        Categorie categorie = catRepo.save(new Categorie());
        logService.logg("Création d'une catégorie", null, categorie, "Categorie");

        TraiteNonProportionnel traite = traiRepo.findById(dto.getTraiteNPId()).orElseThrow(()->new AppException("Traité introuvable"));
        CategorieTraite categorieTraite = catTraiRepo.save(catMapper.mapToCategorieTraite(dto));
        return null;
    }

//    TraiteNonProportionnel traite = traiRepo.findById(dto.getTraiteNPId()).orElseThrow(()->new AppException("Traité introuvable"));
//    Territorialite territorialite = terrRepo.save(terrMapper.mapToTerritorialite(dto));
//        logService.logg("Création d'une territorialité", null, territorialite, "Territorialite");
//        dto.getPaysCodes().forEach(p->
//    {
//        TerritorialiteDetails terrDetails = terrDetRepo.save(new TerritorialiteDetails(null, new Pays(p), territorialite));
//        try {
//            logService.logg("Ajout d'un pays à une territorialité", null, terrDetails, "Territorialite");
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//    });
//
//        dto.getOrgCodes().forEach(o->
//    {
//        TerritorialiteDetails terrDetails = terrDetRepo.save(new TerritorialiteDetails(new Organisation(o), null, territorialite));
//        try {
//            logService.logg("Ajout d'une organisation à une territorialité", null, terrDetails, "TerritorialiteDetails");
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//    });
//    TerritorialiteResp territorialiteResp = terrMapper.mapToTerritorialiteResp(dto, traite);
//        territorialiteResp.setTerrId(territorialite.getTerrId());
//        return territorialiteResp;

    @Override
    public CategorieResp update(CategorieReq dto) {
        return null;
    }

    @Override
    public Page<CategorieResp> search(Long traiId, String key, PageRequest of) {
        return null;
    }
}
