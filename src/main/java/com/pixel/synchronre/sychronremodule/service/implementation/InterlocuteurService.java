package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReActions;
import com.pixel.synchronre.sychronremodule.model.constants.SynchronReTables;
import com.pixel.synchronre.sychronremodule.model.dao.InterlocuteurRepository;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.request.CreateInterlocuteurReq;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.request.UpdateInterlocuteurReq;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.response.InterlocuteurListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.InterlocuteurMapper;
import com.pixel.synchronre.sychronremodule.model.entities.Interlocuteur;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceInterlocuteur;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public List<InterlocuteurListResp> getInterlocuteurByCessionnaire(Long cesId) {
        return null;
    }

    @Override
    public List<InterlocuteurListResp> getInterlocuteurByPlacement(Long repId)
    {
        InterlocuteurListResp interlocuteurPrincipal = interRepo.getInterlocuteursPrincipal(repId);
        String autreIntIds = interRepo.getAutreInterlocuteursIdsByPlacement(repId);
        if(autreIntIds == null || autreIntIds.trim().equals("")) return Collections.singletonList(interlocuteurPrincipal);
        List<InterlocuteurListResp> interlocuteurs = Stream.concat(Stream.of(interlocuteurPrincipal), Arrays.stream(autreIntIds.split(",")).map(Long::valueOf).map(interRepo::findInterlocuteursById)).collect(Collectors.toList());
        return interlocuteurs;
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
