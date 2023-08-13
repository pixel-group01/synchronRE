package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.CedRepo;
import com.pixel.synchronre.sychronremodule.model.dao.CessionnaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.InterlocuteurRepository;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.CreateCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.UpdateCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.request.CreateInterlocuteurReq;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.request.UpdateInterlocuteurReq;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.response.InterlocuteurListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.CedMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.InterlocuteurMapper;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Cedante;
import com.pixel.synchronre.sychronremodule.model.entities.Interlocuteur;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import com.pixel.synchronre.sychronremodule.service.interfac.ICedanteService;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceInterlocuteur;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceMouvement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;

@Service @RequiredArgsConstructor
public class InterlocuteurService implements IServiceInterlocuteur
{

    private final InterlocuteurMapper interMapper;
    private final InterlocuteurRepository interRepo;
    private final ILogService logService;
    //private final IServiceMouvement mvtService;
    private final ObjectCopier<Interlocuteur> intCopier;

    @Override @Transactional
    public InterlocuteurListResp createInterlocuteur(CreateInterlocuteurReq dto) throws UnknownHostException {
        Interlocuteur interlocuteur = interMapper.mapToInterlocuteur(dto);
        interlocuteur = interRepo.save(interlocuteur);
        logService.logg(SynchronReActions.CREATE_INTERLOCUTEUR, null, interlocuteur, SynchronReTables.INTERLOCUTEUR);
        //cedante.setCessionnaire(cesRepo.findById(dto.getCedCesId()).orElse(new Cessionnaire(dto.getCedCesId())));
        return interMapper.mapToInterlocuteurListResp(interlocuteur); }

    @Override @Transactional
    public InterlocuteurListResp updateInterlocuteur(UpdateInterlocuteurReq dto) throws UnknownHostException {
        Interlocuteur oldInter = intCopier.copy(interRepo.findById(dto.getIntId()).orElseThrow(()->new AppException("Interlocuteur introuvable")));
        Interlocuteur interlocuteur = interMapper.mapToInterlocuteur(dto);
        interlocuteur = interRepo.save(interlocuteur);
        logService.logg(SynchronReActions.UPDATE_INTERLOCUTEUR, oldInter, interlocuteur, SynchronReTables.INTERLOCUTEUR);
        return interMapper.mapToInterlocuteurListResp(interlocuteur);
    }

    @Override
    public Page<InterlocuteurListResp> searchInterlocuteur(String key, Long cesId, Pageable pageable) {
        return interRepo.searchInterlocuteur(StringUtils.stripAccentsToUpperCase(key), cesId, pageable);
    }

    @Override
    public void deleteInterlocuteur(Long intId) throws UnknownHostException {
        boolean intExists = interRepo.interlocuteurExists(intId);
        if(intExists)
        {
            Interlocuteur interlocuteur = interRepo.findById(intId).orElse(null);
            Interlocuteur oldInterlocuteur = intCopier.copy(interlocuteur);
            interlocuteur.setStatut(new Statut("SUPP"));
            interRepo.save(interlocuteur);
            logService.logg(SynchronReActions.DELETE_PLACEMENT, oldInterlocuteur, new Interlocuteur(),SynchronReTables.INTERLOCUTEUR);
        }
    }
}
