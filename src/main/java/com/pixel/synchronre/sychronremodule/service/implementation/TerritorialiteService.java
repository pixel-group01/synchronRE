package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.TerritorialiteDetailsRepository;
import com.pixel.synchronre.sychronremodule.model.dao.TerritorialiteRepository;
import com.pixel.synchronre.sychronremodule.model.dao.TraiteRepository;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.CreateTerritorialiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteMapper;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteResp;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.UpdateTerritorialiteReq;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceTerritorialite;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.List;

@Service @RequiredArgsConstructor
public class TerritorialiteService implements IServiceTerritorialite
{
    private final TerritorialiteRepository terrRepo;
    private final ILogService logService;
    private final TerritorialiteMapper terrMapper;
    private final TraiteRepository traiRepo;
    private final TerritorialiteDetailsRepository terrDetRepo;
    @Override @Transactional
    public TerritorialiteResp create(CreateTerritorialiteReq dto) throws UnknownHostException
    {
        TraiteNonProportionnel traite = traiRepo.findById(dto.getTraiteNPId()).orElseThrow(()->new AppException("Traité introuvable"));
        Territorialite territorialite = terrRepo.save(terrMapper.mapToTerritorialite(dto));
        logService.logg("Création d'une territorialité", null, territorialite, "Territorialite");
        dto.getPaysCodes().forEach(p->
        {
            TerritorialiteDetails terrDetails = terrDetRepo.save(new TerritorialiteDetails(null, new Pays(p), territorialite));
            try {
                logService.logg("Ajout d'un pays à une territorialité", null, terrDetails, "Territorialite");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });

        dto.getOrgCodes().forEach(o->
        {
            TerritorialiteDetails terrDetails = terrDetRepo.save(new TerritorialiteDetails(new Organisation(o), null, territorialite));
            try {
                logService.logg("Ajout d'une organisation à une territorialité", null, terrDetails, "Territorialite");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });
        TerritorialiteResp territorialiteResp = terrMapper.mapToTerritorialiteResp(dto, traite);
        territorialiteResp.setTerrId(territorialite.getTerrId());
        return territorialiteResp;
    }

    @Override  @Transactional
    public TerritorialiteResp update(UpdateTerritorialiteReq dto) {
        return null;
    }

    @Override  @Transactional
    public Page<TerritorialiteResp> search(String key, Long fncId, Long userId, Long cedId, List<String> staCodes, Long exeCode, PageRequest of) {
        return null;
    }
}
